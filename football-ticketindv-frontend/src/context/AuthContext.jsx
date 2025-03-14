// AuthContext.js
import React, { createContext, useContext, useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext(null);

export const AuthProvider = ({ children, LoadingComponent = () => <div>Loading...</div> }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  
  // Refs for managing refresh state
  const refreshInProgress = useRef(false);
  const failedRequestQueue = useRef([]);

  async function testRefreshToken() {
    try {
      const response = await fetch('http://localhost:8080/tokens/refresh', {
        method: 'POST',
        credentials: 'include',
      });
  
      console.log('Refresh token response:', response);
  
      if (response.ok) {
        const data = await response.json();
        console.log('Refresh token response data:', data);
      } else {
        console.error('Refresh token request failed:', response.status);
      }
    } catch (error) {
      console.error('Error calling refresh token endpoint:', error);
    }
  };


  // Function to handle token refresh
  const refreshAccessToken = async () => {
    try {
      if (refreshInProgress.current) {
        return new Promise((resolve, reject) => {
          failedRequestQueue.current.push({ resolve, reject });
          
        });
      }
      
      refreshInProgress.current = true;
      const response = await fetch('http://localhost:8080/tokens/refresh', {
        method: 'POST',
        credentials: 'include',
      });

      if (response.ok) {
        const userData = await response.json();
        setUser(userData);
        return true;
      }
      return false;
    } catch (error) {
      console.error('Token refresh failed:', error);
      return false;
    } finally {
      refreshInProgress.current = false;
    }
  };

  const checkAuthStatus = async () => {

    
    try {
      const refreshTokenResponse = await testRefreshToken();

      if (refreshTokenResponse) {
        const userData = await refreshTokenResponse.json();
        setUser(userData);
        return;
      }
  
      if (refreshTokenResponse && refreshTokenResponse.ok) {
        const userData = await refreshTokenResponse.json();
        setUser(userData);
        return; 
      }
  
      // Only try validate if refresh failed
      try {
        const response = await fetch('http://localhost:8080/tokens/validate', {
          method: 'GET',
          credentials: 'include',
        });
  
        if (response.ok) {
          const userData = await response.json();
          setUser(userData);
        } else if (response.status === 401) {
          const refreshSuccess = await refreshAccessToken();
          if (!refreshSuccess) {
            setUser(null);
            if (window.location.pathname !== '/login' && window.location.pathname !== '/register') {
              navigate('/login');
            }
          }
        } else {
          setUser(null);
          if (window.location.pathname !== '/login' && window.location.pathname !== '/register') {
            navigate('/login');
          }
        }
      } catch (error) {
        console.error('Validate check failed:', error);
        setUser(null);
        if (window.location.pathname !== '/login' && window.location.pathname !== '/register') {
          navigate('/login');
        }
      }
    } catch (error) {
      console.error('Auth check failed:', error);
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  // Login function
  const login = async (email, password) => {
    try {
      const response = await fetch('http://localhost:8080/tokens', {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        throw new Error('Login failed');
      }

      const userData = await response.json();
      setUser(userData);
      navigate('/');
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  };

  // Logout function
  const logout = async () => {
    try {
      await fetch('http://localhost:8080/tokens/logout', {
        method: 'POST',
        credentials: 'include',
      });
      setUser(null);
      navigate('/login');
    } catch (error) {
      console.error('Logout error:', error);
      throw error;
    }
  };

  // Check auth status on mount
  useEffect(() => {
    checkAuthStatus();
  }, [navigate]);

  // Set up request interceptor for automatic token refresh
  useEffect(() => {
    const processQueue = (error = null) => {
      failedRequestQueue.current.forEach(prom => {
        if (error) {
          prom.reject(error);
        } else {
          prom.resolve();
        }
      });
      failedRequestQueue.current = [];
    };

    const interceptor = async (request) => {
      try {
        const response = await originalFetch(request);
        
        let clone;
        try {
          clone = response.clone();
        } catch (error) {
          console.error('Failed to clone response:', error);
          return response;
        }

        if (clone.status === 401) {
          // Check for refresh token before attempting refresh
          const cookies = document.cookie;
          const hasRefreshToken = cookies.includes('refresh_token='); // Changed to match your config
          
          if (!hasRefreshToken) {
            setUser(null);
            if (window.location.pathname !== '/login' && window.location.pathname !== '/register') {
              navigate('/login');
            }
            throw new Error('No refresh token available');
          }

          if (refreshInProgress.current) {
            try {
              await new Promise((resolve, reject) => {
                failedRequestQueue.current.push({ resolve, reject });
              });
              // After waiting for refresh, retry the request
              const newRequest = new Request(request.url, {
                method: request.method,
                headers: request.headers,
                body: request.body,
                credentials: 'include',
              });
              return originalFetch(newRequest);
            } catch (error) {
              if (window.location.pathname !== '/login' && window.location.pathname !== '/register') {
                setUser(null);
                navigate('/login');
              }
              throw error;
            }
          }

          refreshInProgress.current = true;

          try {
            const refreshSuccess = await refreshAccessToken();
            refreshInProgress.current = false;

            if (refreshSuccess) {
              processQueue();
              // Create a new request with updated credentials
              const newRequest = new Request(request.url, {
                method: request.method,
                headers: request.headers,
                body: request.body,
                credentials: 'include',
              });
              return originalFetch(newRequest);
            } else {
              const error = new Error('Token refresh failed');
              processQueue(error);
              setUser(null);
              if (window.location.pathname !== '/login' && window.location.pathname !== '/register') {
                navigate('/login');
              }
              throw error;
            }
          } catch (error) {
            refreshInProgress.current = false;
            processQueue(error);
            setUser(null);
            if (window.location.pathname !== '/login' && window.location.pathname !== '/register') {
              navigate('/login');
            }
            throw error;
          }
        }
        return response;
      } catch (error) {
        console.error('Request failed:', error);
        throw error;
      }
    };

    // Override fetch to add interceptor
    const originalFetch = window.fetch;
    window.fetch = async (...args) => {
      const request = new Request(...args);
      return interceptor(request);
    };

    return () => {
      window.fetch = originalFetch;
      refreshInProgress.current = false;
      failedRequestQueue.current = [];
    };
  }, [navigate]);

  if (loading) {
    return <LoadingComponent />;
  }

  return (
    <AuthContext.Provider value={{
      user,
      login,
      logout,
      isAuthenticated: !!user,
      isAdmin: user?.role === 'ADMIN'
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
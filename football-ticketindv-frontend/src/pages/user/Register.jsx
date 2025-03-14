import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import UserForm from '../../components/user/UserForm';
import userApi from '../../services/userApi';
import Layout from '../../components/layout/Layout';

const Register = () => {
  const navigate = useNavigate();
  const [error, setError] = useState(null);

  const handleRegister = async (userData) => {
    try {
      setError(null);
      const { role, ...registrationData } = userData;
      await userApi.register(registrationData);
      navigate('/login');
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

        setError(errorMessage || 'Error during registration');
        console.log('Error response:', error.response?.data); 
    }
  };

  return (
    <Layout>
      <div className="container-fluid px-4 py-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fs-2 fw-bold mb-0">Create Account</h2>
          <button 
            onClick={() => navigate('/login')} 
            className="btn btn-outline-secondary"
          >
            Back to Login
          </button>
        </div>
        
        {error && (
          <div className="alert alert-danger" role="alert">
            {error}
          </div>
        )}

        <div className="card shadow-sm">
          <div className="card-body">
            <UserForm 
              onSubmit={handleRegister}
              initialData={{ role: 'USER' }}
              showRoleSelect={false}
            />
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default Register;
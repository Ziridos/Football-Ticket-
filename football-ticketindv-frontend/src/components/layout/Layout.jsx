import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import logo from '../../images/logo.png';

const Layout = ({ children }) => {
  const { user, isAuthenticated, logout } = useAuth();

  const handleLogout = () => {
    logout();
  };

  return (
    <div className="d-flex flex-column vh-100">
      <nav className="navbar navbar-expand-lg navbar-light bg-white shadow-sm sticky-top">
        <div className="container">
          <Link to="/" className="navbar-brand d-flex align-items-center">
            <img src={logo} alt="Logo" className="me-2" style={{ height: "32px" }} />
            <span className="fs-5 fw-bold">Football Ticket System</span>
          </Link>
          
          <div className="navbar-nav ms-auto gap-3">
            {isAuthenticated && user?.role === 'ADMIN' && (
              <>
                <Link to="/users" className="nav-link text-danger fw-bold">USERS</Link>
                <Link to="/competitions" className="nav-link text-danger fw-bold">COMPETITIONS</Link>
                <Link to="/stadiums" className="nav-link text-danger fw-bold">STADIUMS</Link>
                <Link to="/clubs" className="nav-link text-danger fw-bold">CLUBS</Link>
                <Link to="/matches" className="nav-link text-danger fw-bold">MATCHES</Link>
                <Link to="/tickets/statistics" className="nav-link text-danger fw-bold">STATISTICS</Link>
              </>
            )}
            {isAuthenticated && (
              <>
              <Link to="/my-tickets" className="nav-link text-danger fw-bold">MY TICKETS</Link>
              <Link to={`/profile/${user?.userId}`} className="nav-link text-danger fw-bold">PROFILE</Link>
              </>
            )}
            {isAuthenticated ? (
              <button 
                onClick={handleLogout} 
                className="nav-link text-danger fw-bold border-0 bg-transparent"
              >
                LOGOUT
              </button>
            ) : (
              <Link to="/login" className="nav-link text-danger fw-bold">LOGIN</Link>
            )}
          </div>
        </div>
      </nav>

      <div className="flex-grow-1">
        {children}
      </div>
    </div>
  );
};

export default Layout;
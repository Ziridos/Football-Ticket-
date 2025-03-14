import React from 'react';
import LoginForm from '../components/auth/LoginForm';
import logo from '../images/logo.png';
import { useNavigate, Link } from 'react-router-dom';

const LoginPage = () => {
  const navigate = useNavigate();

  const handleLoginSuccess = () => {
    navigate('/');
  };

  return (
    <div className="min-vh-100 bg-light">
      <nav className="navbar navbar-light bg-white shadow">
        <div className="container">
          <div className="navbar-brand d-flex align-items-center">
            <img src={logo} alt="Logo" className="me-2" style={{ height: "32px" }} />
            <span className="fw-bold fs-4">Football Ticket System</span>
          </div>
        </div>
      </nav>
      
      <div className="container mt-5">
        <div className="row justify-content-center">
          <div className="col-12 col-md-6 col-lg-4">
            <div className="card shadow-lg">
              <div className="card-body p-4">
                <h1 className="text-center fw-bold mb-4">
                  Login to Football Ticket System
                </h1>
                <LoginForm onLoginSuccess={handleLoginSuccess} />
                <div className="text-center mt-3">
                  Don't have an account? <Link to="/register" className="text-primary text-decoration-none">Register here</Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
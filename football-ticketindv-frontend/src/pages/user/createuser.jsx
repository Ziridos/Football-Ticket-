import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import UserForm from '../../components/user/UserForm';
import userApi from '../../services/userApi';
import Layout from '../../components/layout/Layout';
import SuccessNotification from '../../components/common/SuccessNotification';


const CreateUser = () => {
  const navigate = useNavigate();
  const { user: authUser } = useAuth();
  const [error, setError] = useState(null);
  const [showSuccess, setShowSuccess] = useState(false);

  useEffect(() => {
    if (authUser?.role !== 'ADMIN') {
      navigate('/');
      return;
    }
  }, [authUser, navigate]);

  const handleCreateUser = async (userData) => {
    try {
      setError(null);
      await userApi.createUser(userData);
      setShowSuccess(true); // Show success message
      setTimeout(() => {
        navigate('/users');
      }, 1500); // Navigate after showing success message
    } catch (error) {
      if (error.response?.status === 401) {
        navigate('/login');
      }
      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;
        
      setError(errorMessage || 'Failed to create User. Please try again.');
      console.error('Error creating user:', error);
    }
  };

  if (error) {
    return (
      <Layout>
        <div className="container-fluid px-4 py-4">
          <div className="alert alert-danger" role="alert">
            {error}
          </div>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      
      <div className="container-fluid px-4 py-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fs-2 fw-bold mb-0">Create User</h2>
          <button 
            onClick={() => navigate('/users')} 
            className="btn btn-outline-secondary"
          >
            Back to Users
          </button>
          <SuccessNotification 
        message="User created successfully!"
        isVisible={showSuccess}
        onClose={() => setShowSuccess(false)}
      />
        </div>
        
        <div className="card shadow-sm">
          <div className="card-body">
            <UserForm onSubmit={handleCreateUser} />
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default CreateUser;
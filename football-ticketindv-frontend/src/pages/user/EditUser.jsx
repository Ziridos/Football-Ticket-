import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import UserForm from '../../components/user/UserForm';
import userApi from '../../services/userApi';
import Layout from '../../components/layout/Layout';
import SuccessNotification from '../../components/common/SuccessNotification';

const EditUser = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user: authUser } = useAuth();
  const [userData, setUserData] = useState(null);
  const [error, setError] = useState(null);
  const [showSuccess, setShowSuccess] = useState(false);

  useEffect(() => {
    if (authUser?.role !== 'ADMIN') {
      navigate('/');
      return;
    }
    fetchUser(id);
  }, [id, authUser, navigate]);

  const fetchUser = async (userId) => {
    try {
      setError(null);
      const user = await userApi.getUserById(userId);
      setUserData(user);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      setError(errorMessage || 'Error fetching user data');
      if (error.response?.status === 401) {
        navigate('/login');
      }
      console.error('Error fetching user:', error);
    }
  };

  const handleUpdateUser = async (updatedUserData) => {
    try {
      setError(null);
      await userApi.updateUser(id, updatedUserData);
      setShowSuccess(true);
      setTimeout(() => {
        navigate('/users');
      }, 1500);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      if (error.response?.status === 401) {
        navigate('/login');
      }
      setError(errorMessage || 'Error updating user');
      console.error('Error updating user:', error);
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
          <h2 className="fs-2 fw-bold mb-0">Edit User</h2>
          <button 
            onClick={() => navigate('/users')} 
            className="btn btn-outline-secondary"
          >
            Back to Users
          </button>
          <SuccessNotification 
        message="User edited successfully!"
        isVisible={showSuccess}
        onClose={() => setShowSuccess(false)}
      />
        </div>
        
        {userData ? (
          <div className="card shadow-sm">
            <div className="card-body">
              <UserForm onSubmit={handleUpdateUser} initialData={userData} />
            </div>
          </div>
        ) : (
          <div className="d-flex justify-content-center py-5">
            <div className="spinner-border text-danger" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        )}
      </div>
    </Layout>
  );
};

export default EditUser;
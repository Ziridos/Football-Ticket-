import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import StadiumForm from '../../components/stadium/StadiumForm';
import stadiumApi from '../../services/stadiumApi';
import Layout from '../../components/layout/Layout';
import SuccessNotification from '../../components/common/SuccessNotification';

const CreateStadium = () => {
  const navigate = useNavigate();
  const [error, setError] = useState(null);
  const { isAuthenticated } = useAuth();
  const [showSuccess, setShowSuccess] = useState(false);


  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
  }, [isAuthenticated, navigate]);

  const handleCreateStadium = async (stadiumData) => {
    try {
      await stadiumApi.createStadium(stadiumData);
      setShowSuccess(true); // Show success message
      setTimeout(() => {
        navigate('/stadiums');
      }, 1500);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      setError(errorMessage || 'Failed to create stadium');
      if (error.message.includes('Please login')) {
        navigate('/login');
      }
    }
  };

  if (!isAuthenticated) return null;

  return (
    <Layout>
      <div className="container-fluid px-4 py-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fs-2 fw-bold mb-0">Create Stadium</h2>
          <Link to="/stadiums" className="btn btn-outline-secondary">
            Back to Stadiums
          </Link>
          <SuccessNotification 
        message="Stadium created successfully!"
        isVisible={showSuccess}
        onClose={() => setShowSuccess(false)}
      />
        </div>

        {error && (
          <div className="alert alert-danger mb-4" role="alert">
            {error}
          </div>
        )}

        <StadiumForm onSubmit={handleCreateStadium} />
      </div>
    </Layout>
  );
};

export default CreateStadium;
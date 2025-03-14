import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import StadiumForm from '../../components/stadium/StadiumForm';
import stadiumApi from '../../services/stadiumApi';
import Layout from '../../components/layout/Layout';
import SuccessNotification from '../../components/common/SuccessNotification';

const EditStadium = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [stadiumData, setStadiumData] = useState(null);
  const [error, setError] = useState(null);
  const { isAuthenticated } = useAuth();
  const [showSuccess, setShowSuccess] = useState(false);


  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    fetchStadium();
  }, [id, isAuthenticated, navigate]);

  const fetchStadium = async () => {
    try {
      const stadium = await stadiumApi.getStadiumById(id);
      setStadiumData(stadium);
      setError(null);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      setError(errorMessage || 'Failed to load stadium data. Please try again later.');
      if (error.message.includes('Please login')) {
        navigate('/login');
      }
    }
  };

  const handleUpdateStadium = async (updatedStadiumData) => {
    try {
      await stadiumApi.updateStadium(id, updatedStadiumData);
      setShowSuccess(true); // Show success message
      setTimeout(() => {
        navigate('/stadiums');
      }, 1500);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      setError(errorMessage || 'Failed to update stadium');
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
          <h2 className="fs-2 fw-bold mb-0">Edit Stadium</h2>
          <Link to="/stadiums" className="btn btn-outline-secondary">
            Back to Stadiums
          </Link>
          <SuccessNotification 
        message="Stadium edited successfully!"
        isVisible={showSuccess}
        onClose={() => setShowSuccess(false)}
      />
        </div>

        {error && (
          <div className="alert alert-danger mb-4" role="alert">
            {error}
          </div>
        )}

        {stadiumData ? (
          <StadiumForm onSubmit={handleUpdateStadium} initialData={stadiumData} />
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

export default EditStadium;
import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import ClubForm from '../../components/club/ClubForm';
import clubApi from '../../services/clubApi';
import stadiumApi from '../../services/stadiumApi';
import Layout from '../../components/layout/Layout';
import SuccessNotification from '../../components/common/SuccessNotification';

const CreateClub = () => {
  const navigate = useNavigate();
  const { user: authUser } = useAuth();
  const [error, setError] = useState(null);
  const [stadiums, setStadiums] = useState([]);
  const [isLoadingStadiums, setIsLoadingStadiums] = useState(true);
  const [stadiumError, setStadiumError] = useState(null);
  const [showSuccess, setShowSuccess] = useState(false);


  useEffect(() => {
    if (authUser?.role !== 'ADMIN') {
      navigate('/');
      return;
    }
    fetchStadiums();
  }, [authUser, navigate]);

  const fetchStadiums = async () => {
    try {
      setIsLoadingStadiums(true);
      setStadiumError(null);
      const stadiumsList = await stadiumApi.getAllStadiumsList();
      setStadiums(stadiumsList);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      console.error('Error fetching stadiums:', error);
      if (error.response?.status === 401) {
        navigate('/login');
        return;
      }
      setStadiumError(errorMessage || 'Failed to load stadiums. Please try again later.');
    } finally {
      setIsLoadingStadiums(false);
    }
  };

  const handleCreateClub = async (clubData) => {
    try {
      setError(null);
      
      const response = await clubApi.createClub({
        clubName: clubData.clubName,
        stadiumId: clubData.stadiumId
      });

      
      if (clubData.logoFile) {
        try {
          await clubApi.uploadLogo(response.clubId, clubData.logoFile);
        } catch (logoError) {
          console.error('Error uploading logo:', logoError);
          
        }
      }

      setShowSuccess(true); // Show success message
      setTimeout(() => {
        navigate('/clubs');
      }, 1500);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      console.error('Error creating club:', error);
      if (error.response?.status === 401) {
        navigate('/login');
        return;
      }
      setError(errorMessage || 'Failed to create club. Please try again.');
      console.log('Error response:', error.response?.data.message);
    }
  };

  if (authUser?.role !== 'ADMIN') return null;

  return (
    <Layout>
      <div className="container-fluid px-4 py-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fs-2 fw-bold mb-0">Create Club</h2>
          <Link to="/clubs" className="btn btn-outline-secondary">
            Back to Clubs
          </Link>
          <SuccessNotification 
        message="Club created successfully!"
        isVisible={showSuccess}
        onClose={() => setShowSuccess(false)}
      />
        </div>

        {error && (
          <div className="alert alert-danger mb-4" role="alert">
            {error}
          </div>
        )}

        <ClubForm 
          onSubmit={handleCreateClub}
          stadiums={stadiums}
          isLoadingStadiums={isLoadingStadiums}
          stadiumError={stadiumError}
        />
      </div>
    </Layout>
  );
};

export default CreateClub;
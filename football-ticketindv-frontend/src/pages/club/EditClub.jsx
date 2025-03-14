import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import ClubForm from '../../components/club/ClubForm';
import clubApi from '../../services/clubApi';
import stadiumApi from '../../services/stadiumApi';
import Layout from '../../components/layout/Layout';
import SuccessNotification from '../../components/common/SuccessNotification';

const EditClub = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user: authUser } = useAuth();
  const [clubData, setClubData] = useState(null);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [stadiums, setStadiums] = useState([]);
  const [isLoadingStadiums, setIsLoadingStadiums] = useState(true);
  const [stadiumError, setStadiumError] = useState(null);
  const [showSuccess, setShowSuccess] = useState(false);


  useEffect(() => {
    if (authUser?.role !== 'ADMIN') {
      navigate('/');
      return;
    }
    fetchInitialData();
  }, [id, authUser, navigate]);

  const fetchInitialData = async () => {
    try {
      setIsLoading(true);
      setIsLoadingStadiums(true);
      setError(null);
      setStadiumError(null);
  
      const [clubResponse, stadiumsList] = await Promise.all([
        clubApi.getClubById(id),
        stadiumApi.getAllStadiumsList()
      ]);

      
  
      setStadiums(stadiumsList);
  
      const transformedClubData = {
        ...clubResponse,
        stadiumId: clubResponse.stadium?.id
      };
      
  
      setClubData(transformedClubData);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      console.error('Error fetching data:', error);
      if (error.response?.status === 401) {
        navigate('/login');
        return;
      }
      setError(errorMessage || 'Failed to load data. Please try again later.');
    } finally {
      setIsLoading(false);
      setIsLoadingStadiums(false);
    }
  };

  const handleUpdateClub = async (updatedClubData) => {
    try {
      setError(null);
      
      await clubApi.updateClub(id, {
        clubName: updatedClubData.clubName,
        stadiumId: updatedClubData.stadiumId
      });

      if (updatedClubData.logoFile) {
        try {
          await clubApi.uploadLogo(id, updatedClubData.logoFile);
        } catch (logoError) {
          console.error('Error uploading logo:', logoError);
        }
      }

      setShowSuccess(true); 
      setTimeout(() => {
        navigate('/clubs');
      }, 1500);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      console.error('Error updating club:', error);
      if (error.response?.status === 401) {
        navigate('/login');
        return;
      }
      setError( errorMessage || 'Failed to update club. Please try again.');
    }
  };

  if (authUser?.role !== 'ADMIN') return null;

  return (
    <Layout>
      <div className="container-fluid px-4 py-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fs-2 fw-bold mb-0">Edit Club</h2>
          <Link to="/clubs" className="btn btn-outline-secondary">
            Back to Clubs
          </Link>
          <SuccessNotification 
        message="Club edited successfully!"
        isVisible={showSuccess}
        onClose={() => setShowSuccess(false)}
      />
        </div>

        {error && (
          <div className="alert alert-danger mb-4" role="alert">
            {error}
          </div>
        )}

        {isLoading || isLoadingStadiums ? (
          <div className="d-flex justify-content-center py-5">
            <div className="spinner-border text-danger" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        ) : (
          clubData && (
            <ClubForm 
              onSubmit={handleUpdateClub} 
              initialData={clubData}
              stadiums={stadiums}
              isLoadingStadiums={isLoadingStadiums}
              stadiumError={stadiumError}
            />
          )
        )}
      </div>
    </Layout>
  );
};

export default EditClub;
import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import MatchForm from '../../components/match/MatchForm';
import matchApi from '../../services/matchApi';
import clubApi from '../../services/clubApi';
import competitionApi from '../../services/competitionApi';
import Layout from '../../components/layout/Layout';
import SuccessNotification from '../../components/common/SuccessNotification';

const CreateMatch = () => {
  const [error, setError] = useState(null);
  const [clubs, setClubs] = useState([]);
  const [competitions, setCompetitions] = useState([]);
  const [isLoadingClubs, setIsLoadingClubs] = useState(true);
  const [isLoadingCompetitions, setIsLoadingCompetitions] = useState(true);
  const [clubsError, setClubsError] = useState(null);
  const [competitionsError, setCompetitionsError] = useState(null);
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [showSuccess, setShowSuccess] = useState(false);


  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    fetchInitialData();
  }, [isAuthenticated, navigate]);

  const fetchInitialData = async () => {
    await Promise.all([
      fetchClubs(),
      fetchCompetitions()
    ]);
  };

  const fetchClubs = async () => {
    try {
      setIsLoadingClubs(true);
      setClubsError(null);
      const data = await clubApi.getAllClubsList();
      setClubs(data);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      console.error(errorMessage || 'Error fetching clubs:', error);
      setClubsError('Failed to load clubs. Please try again later.');
      if (error.message.includes('Please login')) {
        navigate('/login');
      }
    } finally {
      setIsLoadingClubs(false);
    }
  };

  const fetchCompetitions = async () => {
    try {
      setIsLoadingCompetitions(true);
      setCompetitionsError(null);
      const data = await competitionApi.getAllCompetitionsList();
      setCompetitions(data);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;
        
      console.error('Error fetching competitions:', error);
      setCompetitionsError(errorMessage || 'Failed to load competitions. Please try again later.');
      if (error.message.includes('Please login')) {
        navigate('/login');
      }
    } finally {
      setIsLoadingCompetitions(false);
    }
  };

  const handleCreateMatch = async (matchData) => {
    try {
      const newMatch = await matchApi.createMatch(matchData);
      console.log('Match created:', newMatch);
      setShowSuccess(true);
      setTimeout(() => {
        navigate('/matches');
      }, 1500);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      console.error('Error creating match:', error);
      setError(errorMessage || 'Failed to create match. Please try again.');
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
          <h2 className="fs-2 fw-bold mb-0">Create Match</h2>
          <Link to="/matches" className="btn btn-outline-secondary">
            Back to Matches
          </Link>
          <SuccessNotification 
        message="Match created successfully!"
        isVisible={showSuccess}
        onClose={() => setShowSuccess(false)}
      />
        </div>

        {error && (
          <div className="alert alert-danger mb-4" role="alert">
            {error}
          </div>
        )}

        {(isLoadingClubs || isLoadingCompetitions) ? (
          <div className="d-flex justify-content-center py-5">
            <div className="spinner-border text-danger" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        ) : (
          <MatchForm 
            onSubmit={handleCreateMatch}
            clubs={clubs}
            competitions={competitions}
            isLoadingClubs={isLoadingClubs}
            isLoadingCompetitions={isLoadingCompetitions}
            clubsError={clubsError}
            competitionsError={competitionsError}
          />
        )}
      </div>
    </Layout>
  );
};

export default CreateMatch;
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import MatchForm from '../../components/match/MatchForm';
import matchApi from '../../services/matchApi';
import clubApi from '../../services/clubApi';
import competitionApi from '../../services/competitionApi';
import Layout from '../../components/layout/Layout';
import SuccessNotification from '../../components/common/SuccessNotification';

const EditMatch = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [matchData, setMatchData] = useState(null);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [clubs, setClubs] = useState([]);
  const [competitions, setCompetitions] = useState([]);
  const [isLoadingClubs, setIsLoadingClubs] = useState(true);
  const [isLoadingCompetitions, setIsLoadingCompetitions] = useState(true);
  const [clubsError, setClubsError] = useState(null);
  const [competitionsError, setCompetitionsError] = useState(null);
  const [showSuccess, setShowSuccess] = useState(false);


  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    fetchInitialData();
  }, [id, isAuthenticated, navigate]);

  const fetchInitialData = async () => {
    try {
      setIsLoading(true);
      const [match, clubsData, competitionsData] = await Promise.all([
        matchApi.getMatchById(id),
        clubApi.getAllClubs(),
        competitionApi.getAllCompetitions()
      ]);

      setMatchData({
        homeClubName: match.homeClub.clubName,
        awayClubName: match.awayClub.clubName,
        competitionName: match.competition.competitionName,
        matchDateTime: match.matchDateTime,
      });
      setClubs(clubsData);
      setCompetitions(competitionsData);
      setError(null);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      console.error(errorMessage || 'Error fetching data:', error);
      if (error.message.includes('Please login')) {
        navigate('/login');
        return;
      }
      setError(errorMessage || 'Failed to load data. Please try again later.');
    } finally {
      setIsLoading(false);
      setIsLoadingClubs(false);
      setIsLoadingCompetitions(false);
    }
  };

  const handleUpdateMatch = async (updatedMatchData) => {
    try {
      const updatedMatch = await matchApi.updateMatch(id, updatedMatchData);
      console.log('Match updated:', updatedMatch);
      setShowSuccess(true); 
      setTimeout(() => {
        navigate('/matches');
      }, 1500);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      console.error(errorMessage || 'Error updating match:', error);
      setError(errorMessage || 'Failed to update match. Please try again.');
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
          <h2 className="fs-2 fw-bold mb-0">Edit Match</h2>
          <Link to="/matches" className="btn btn-outline-secondary">
            Back to Matches
          </Link>
          <SuccessNotification 
        message="Match edited successfully!"
        isVisible={showSuccess}
        onClose={() => setShowSuccess(false)}
      />
        </div>

        {error && (
          <div className="alert alert-danger mb-4" role="alert">
            {error}
          </div>
        )}

        {isLoading || isLoadingClubs || isLoadingCompetitions ? (
          <div className="d-flex justify-content-center py-5">
            <div className="spinner-border text-danger" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        ) : (
          matchData && (
            <MatchForm 
              onSubmit={handleUpdateMatch}
              initialData={matchData}
              clubs={clubs}
              competitions={competitions}
              isLoadingClubs={isLoadingClubs}
              isLoadingCompetitions={isLoadingCompetitions}
              clubsError={clubsError}
              competitionsError={competitionsError}
            />
          )
        )}
      </div>
    </Layout>
  );
};

export default EditMatch;
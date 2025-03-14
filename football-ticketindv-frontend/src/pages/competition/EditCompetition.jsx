import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import CompetitionForm from '../../components/competition/CompetitionForm';
import competitionApi from '../../services/competitionApi';
import Layout from '../../components/layout/Layout';
import SuccessNotification from '../../components/common/SuccessNotification';

const EditCompetition = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user: authUser } = useAuth();
  const [competitionData, setCompetitionData] = useState(null);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [showSuccess, setShowSuccess] = useState(false);

  useEffect(() => {
    if (authUser?.role !== 'ADMIN') {
      navigate('/');
      return;
    }
    fetchCompetition();
  }, [id, authUser, navigate]);

  const fetchCompetition = async () => {
    try {
      setIsLoading(true);
      setError(null);
      const data = await competitionApi.getCompetitionById(id);
      setCompetitionData(data);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      if (error.response?.status === 401) {
        navigate('/login');
      }
      setError(errorMessage || 'Failed to load competition data. Please try again later.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleUpdateCompetition = async (updatedCompetitionData) => {
    try {
      setError(null);
      await competitionApi.updateCompetition(id, updatedCompetitionData);
      setShowSuccess(true); // Show success message
      setTimeout(() => {
        navigate('/competitions');
      }, 1500);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      if (error.response?.status === 401) {
        navigate('/login');
      }
      setError(errorMessage || 'Failed to update competition. Please try again.');
    }
  };

  if (authUser?.role !== 'ADMIN') return null;

  return (
    <Layout>
      <div className="container-fluid px-4 py-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fs-2 fw-bold mb-0">Edit Competition</h2>
          <Link to="/competitions" className="btn btn-outline-secondary">
            Back to Competitions
          </Link>
          <SuccessNotification 
        message="Competition edited successfully!"
        isVisible={showSuccess}
        onClose={() => setShowSuccess(false)}
      />
        </div>

        {error && (
          <div className="alert alert-danger mb-4" role="alert">
            {error}
          </div>
        )}

        {isLoading ? (
          <div className="d-flex justify-content-center py-5">
            <div className="spinner-border text-danger" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        ) : (
          competitionData && <CompetitionForm onSubmit={handleUpdateCompetition} initialData={competitionData} />
        )}
      </div>
    </Layout>
  );
};

export default EditCompetition;
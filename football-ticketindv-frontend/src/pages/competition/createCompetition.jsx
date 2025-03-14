import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import CompetitionForm from '../../components/competition/CompetitionForm';
import competitionApi from '../../services/competitionApi';
import Layout from '../../components/layout/Layout';
import SuccessNotification from '../../components/common/SuccessNotification';

const CreateCompetition = () => {
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { user: authUser } = useAuth();
  const [showSuccess, setShowSuccess] = useState(false);


  useEffect(() => {
    if (authUser?.role !== 'ADMIN') {
      navigate('/');
      return;
    }
  }, [authUser, navigate]);

  const handleCreateCompetition = async (competitionData) => {
    try {
      setError(null);
      await competitionApi.createCompetition(competitionData);
      setShowSuccess(true); // Show success message
      setTimeout(() => {
        navigate('/competitions');
      }, 1500);
    } catch (error) {
      if (error.response?.status === 401) {
        navigate('/login');
      }
      
      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;
        
      setError(errorMessage || 'Failed to create competition. Please try again.');

    }
  };

  if (authUser?.role !== 'ADMIN') return null;

  return (
    <Layout>
      <div className="container-fluid px-4 py-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fs-2 fw-bold mb-0">Create Competition</h2>
          <Link to="/competitions" className="btn btn-outline-secondary">
            Back to Competitions
          </Link>
          <SuccessNotification 
        message="Competition created successfully!"
        isVisible={showSuccess}
        onClose={() => setShowSuccess(false)}
      />
        </div>

        {error && (
          <div className="alert alert-danger mb-4" role="alert">
            {error}
          </div>
        )}

        <CompetitionForm onSubmit={handleCreateCompetition} />
      </div>
    </Layout>
  );
};

export default CreateCompetition;
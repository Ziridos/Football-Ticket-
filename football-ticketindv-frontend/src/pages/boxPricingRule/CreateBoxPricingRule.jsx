import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import BoxPricingRuleForm from '../../components/boxPricingRule/BoxPricingRuleForm';
import boxPricingRuleApi from '../../services/boxPricingRuleApi';
import Layout from '../../components/layout/Layout';

const CreateBoxPricingRule = () => {
  const { stadiumId } = useParams();
  const navigate = useNavigate();
  const { user: authUser } = useAuth();
  const [error, setError] = useState(null);

  useEffect(() => {
    if (authUser?.role !== 'ADMIN') {
      navigate('/');
      return;
    }
  }, [authUser, navigate]);

  const handleCreateRule = async (ruleData) => {
    try {
      setError(null);
      await boxPricingRuleApi.createRule({
        ...ruleData,
        stadiumId: parseInt(stadiumId)
      });
      navigate(`/box-pricing-rules/${stadiumId}`);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      if (error.response?.status === 401) {
        navigate('/login');
      }
      setError(errorMessage || 'Failed to create pricing rule. Please try again.');
      console.error('Error creating rule:', error);
    }
  };

  if (authUser?.role !== 'ADMIN') return null;

  return (
    <Layout>
      <div className="container-fluid px-4 py-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fs-2 fw-bold mb-0">Create Box Pricing Rule</h2>
          <Link 
            to={`/box-pricing-rules/${stadiumId}`} 
            className="btn btn-outline-secondary"
          >
            Back to Rules
          </Link>
        </div>

        {error && (
          <div className="alert alert-danger mb-4" role="alert">
            {error}
          </div>
        )}

        <BoxPricingRuleForm onSubmit={handleCreateRule} />
      </div>
    </Layout>
  );
};

export default CreateBoxPricingRule;
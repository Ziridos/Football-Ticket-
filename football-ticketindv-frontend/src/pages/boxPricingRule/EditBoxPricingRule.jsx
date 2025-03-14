import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import BoxPricingRuleForm from '../../components/boxPricingRule/BoxPricingRuleForm';
import boxPricingRuleApi from '../../services/boxPricingRuleApi';
import Layout from '../../components/layout/Layout';

const EditBoxPricingRule = () => {
  const { stadiumId, ruleId } = useParams();
  const navigate = useNavigate();
  const { user: authUser } = useAuth();
  const [rule, setRule] = useState(null);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (authUser?.role !== 'ADMIN') {
      navigate('/');
      return;
    }
    fetchRule();
  }, [ruleId, authUser, navigate]);

  const fetchRule = async () => {
    try {
      setIsLoading(true);
      setError(null);
      const data = await boxPricingRuleApi.getRuleById(ruleId);
      setRule(data);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      setError(errorMessage || 'Failed to load pricing rule data. Please try again later.');
      if (error.response?.status === 401) {
        navigate('/login');
      }
      console.error('Error fetching rule:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleUpdateRule = async (ruleData) => {
    try {
      setError(null);
      await boxPricingRuleApi.updateRule(ruleId, {
        ...ruleData,
        id: parseInt(ruleId),
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
      setError(errorMessage || 'Failed to update pricing rule. Please try again.');
      console.error('Error updating rule:', error);
    }
  };

  if (authUser?.role !== 'ADMIN') return null;

  return (
    <Layout>
      <div className="container-fluid px-4 py-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fs-2 fw-bold mb-0">Edit Box Pricing Rule</h2>
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

        {isLoading ? (
          <div className="d-flex justify-content-center py-5">
            <div className="spinner-border text-danger" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        ) : (
          rule && <BoxPricingRuleForm onSubmit={handleUpdateRule} initialData={rule} />
        )}
      </div>
    </Layout>
  );
};

export default EditBoxPricingRule;
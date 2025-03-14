import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import BoxPricingRuleList from '../../components/boxPricingRule/BoxPricingRuleList';
import boxPricingRuleApi from '../../services/boxPricingRuleApi';
import Layout from '../../components/layout/Layout';

const BoxPricingRules = () => {
  const { stadiumId } = useParams();
  const [rules, setRules] = useState([]);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    fetchRules();
  }, [stadiumId, isAuthenticated, navigate]);

  const fetchRules = async () => {
    try {
      setIsLoading(true);
      setError(null);
      const data = await boxPricingRuleApi.getRulesByStadium(stadiumId);
      setRules(data);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      setError(errorMessage || 'Failed to load pricing rules. Please try again later.');
      if (error.message.includes('Please login')) {
        navigate('/login');
      }
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteRule = async (ruleId) => {
    try {
      setError(null);
      await boxPricingRuleApi.deleteRule(ruleId);
      setRules(rules.filter(rule => rule.id !== ruleId));
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      setError(errorMessage || 'Failed to delete pricing rule. Please try again.');
      if (error.message.includes('Please login')) {
        navigate('/login');
      }
    }
  };

  if (!isAuthenticated) return null;

  return (
    <Layout>
      <div className="container-fluid px-4 py-4" style={{ maxHeight: 'calc(100vh - 56px)', overflowY: 'auto' }}>
        <div className="d-flex justify-content-between align-items-center mb-4">
          <div>
            <h2 className="fs-2 fw-bold mb-1">Box Pricing Rules</h2>
            <p className="text-muted mb-0">Stadium ID: {stadiumId}</p>
          </div>
          <div>
            <Link 
              to={`/create-box-pricing-rule/${stadiumId}`} 
              className="btn btn-danger me-2"
            >
              Create New Rule
            </Link>
            <Link 
              to="/stadiums" 
              className="btn btn-outline-secondary"
            >
              Back to Stadiums
            </Link>
          </div>
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
          <BoxPricingRuleList 
            rules={rules} 
            stadiumId={stadiumId} 
            onDelete={handleDeleteRule} 
          />
        )}
      </div>
    </Layout>
  );
};

export default BoxPricingRules;
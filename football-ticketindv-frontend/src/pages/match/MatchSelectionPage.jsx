import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import MatchFilter from '../../components/match/MatchFilter';
import MatchCard from '../../components/match/MatchCard';
import Pagination from '../../components/pagination/Pagination';
import matchApi from '../../services/matchApi';
import Layout from '../../components/layout/Layout';
import TicketPurchaseStepper from '../../components/ticket/TicketPurchaseStepper';

const MatchSelectionPage = () => {
  const [matches, setMatches] = useState([]);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [filters, setFilters] = useState({});
  const [pagination, setPagination] = useState({
    currentPage: 0,
    totalPages: 0,
    totalElements: 0
  });
  const [pageSize, setPageSize] = useState(12);
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    fetchMatches();
  }, [isAuthenticated, navigate, filters, pagination.currentPage, pageSize]);

  const fetchMatches = async () => {
    try {
      setIsLoading(true);
      const response = await matchApi.getAllMatches({
        ...filters,
        page: pagination.currentPage,
        size: pageSize
      });
      setMatches(response.matches);
      setPagination({
        currentPage: response.currentPage,
        totalPages: response.totalPages,
        totalElements: response.totalElements
      });
      setError(null);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      console.error('Error fetching matches:', error);
      setError(errorMessage || 'Failed to load matches. Please try again later.');
      if (error.message.includes('Please login')) {
        navigate('/login');
      }
    } finally {
      setIsLoading(false);
    }
  };

  const handleMatchSelect = (match) => {
    console.log('Selected match:', match);
    navigate(`/ticket-purchase/boxes/${match.matchId}`, { state: { match } });
  };

  const handleFilterChange = (newFilters) => {
    setFilters(newFilters);
    setPagination(prev => ({ ...prev, currentPage: 0 }));
  };

  const handlePageChange = (newPage) => {
    setPagination(prev => ({ ...prev, currentPage: newPage }));
  };

  const handlePageSizeChange = (event) => {
    setPageSize(parseInt(event.target.value));
    setPagination(prev => ({ ...prev, currentPage: 0 }));
  };

  const isPastMatch = (matchDateTime) => {
    return new Date(matchDateTime) < new Date();
  };

  if (!isAuthenticated) return null;

  return (
    <Layout>
      <div className="container-fluid px-4 py-4" style={{ maxHeight: 'calc(100vh - 56px)', overflowY: 'auto' }}>
      <TicketPurchaseStepper currentStep={1} />
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fs-2 fw-bold mb-0">Select a Match</h2>
          <select
            className="form-select w-auto"
            value={pageSize}
            onChange={handlePageSizeChange}
            disabled={isLoading}
          >
            <option value="6">6 per page</option>
            <option value="12">12 per page</option>
            <option value="24">24 per page</option>
          </select>
        </div>

        <MatchFilter onFilterChange={handleFilterChange} />

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
          <>
            <div className="row g-4">
              {matches.map((match) => (
                <div key={match.matchId} className="col-md-6 col-lg-4">
                  <MatchCard
                    match={match}
                    onSelect={handleMatchSelect}
                    isPast={isPastMatch(match.matchDateTime)}
                  />
                </div>
              ))}
              {matches.length === 0 && !isLoading && (
                <div className="col-12 text-center py-5">
                  <p className="text-muted">No matches found</p>
                </div>
              )}
            </div>

            {pagination.totalPages > 1 && (
              <div className="mt-4">
                <Pagination
                  currentPage={pagination.currentPage}
                  totalPages={pagination.totalPages}
                  onPageChange={handlePageChange}
                />
                <div className="text-muted text-center mt-3">
                  Showing {matches.length} of {pagination.totalElements} matches
                </div>
              </div>
            )}
          </>
        )}
      </div>
    </Layout>
  );
};

export default MatchSelectionPage;
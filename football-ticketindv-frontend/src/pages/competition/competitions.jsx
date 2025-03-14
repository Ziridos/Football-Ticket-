import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import CompetitionList from '../../components/competition/CompetitionList';
import CompetitionFilter from '../../components/competition/CompetitionFilter';
import Pagination from '../../components/pagination/Pagination';
import competitionApi from '../../services/competitionApi';
import Layout from '../../components/layout/Layout';

const Competitions = () => {
  const [competitions, setCompetitions] = useState([]);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [filters, setFilters] = useState({});
  const [pagination, setPagination] = useState({
    currentPage: 0,
    totalPages: 0,
    totalElements: 0
  });
  const [pageSize, setPageSize] = useState(10);
  const navigate = useNavigate();
  const { user: authUser } = useAuth();

  useEffect(() => {
    if (authUser?.role !== 'ADMIN') {
      navigate('/');
      return;
    }
    fetchCompetitions();
  }, [authUser, navigate, filters, pagination.currentPage, pageSize]);

  const fetchCompetitions = async () => {
    try {
      setIsLoading(true);
      setError(null);
      const response = await competitionApi.getAllCompetitions({
        ...filters,
        page: pagination.currentPage,
        size: pageSize
      });
      setCompetitions(response.competitions);
      setPagination({
        currentPage: response.currentPage,
        totalPages: response.totalPages,
        totalElements: response.totalElements
      });
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      if (error.response?.status === 401) {
        navigate('/login');
      }
      setError(errorMessage || 'Failed to load competitions. Please try again later.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleFilterChange = (newFilters) => {
    setFilters(newFilters);
    setPagination(prev => ({ ...prev, currentPage: 0 })); // Reset to first page on filter change
  };

  const handleDelete = async (competitionId) => {
    try {
      setError(null);
      await competitionApi.deleteCompetition(competitionId);
      fetchCompetitions(); // Refresh the current page after deletion
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      if (error.response?.status === 401) {
        navigate('/login');
      }
      setError(errorMessage || 'Failed to delete competition. Please try again.');
    }
  };

  const handlePageChange = (newPage) => {
    setPagination(prev => ({ ...prev, currentPage: newPage }));
  };

  const handlePageSizeChange = (event) => {
    setPageSize(parseInt(event.target.value));
    setPagination(prev => ({ ...prev, currentPage: 0 }));
  };

  if (authUser?.role !== 'ADMIN') return null;

  return (
    <Layout>
      <div className="container-fluid px-4 py-4" style={{ maxHeight: 'calc(100vh - 56px)', overflowY: 'auto' }}>
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fs-2 fw-bold mb-0">Competitions Management</h2>
          <div>
            <Link to="/create-competition" className="btn btn-danger me-2">
              Create New Competition
            </Link>
            <Link to="/" className="btn btn-outline-secondary">
              Back to Home
            </Link>
          </div>
        </div>

        <CompetitionFilter onFilterChange={handleFilterChange} />

        {error && (
          <div className="alert alert-danger mb-4" role="alert">
            {error}
          </div>
        )}

        <div className="card shadow-sm">
          <div className="card-body">
            <div className="d-flex justify-content-end mb-3">
              <select
                className="form-select w-auto"
                value={pageSize}
                onChange={handlePageSizeChange}
                disabled={isLoading}
              >
                <option value="5">5 per page</option>
                <option value="10">10 per page</option>
                <option value="20">20 per page</option>
                <option value="50">50 per page</option>
              </select>
            </div>

            {isLoading ? (
              <div className="d-flex justify-content-center py-5">
                <div className="spinner-border text-danger" role="status">
                  <span className="visually-hidden">Loading...</span>
                </div>
              </div>
            ) : (
              <>
                <CompetitionList competitions={competitions} onDelete={handleDelete} />
                
                {pagination.totalPages > 1 && (
                  <Pagination
                    currentPage={pagination.currentPage}
                    totalPages={pagination.totalPages}
                    onPageChange={handlePageChange}
                  />
                )}
                
                <div className="text-muted text-center mt-3">
                  Showing {competitions.length} of {pagination.totalElements} competitions
                </div>
              </>
            )}
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default Competitions;
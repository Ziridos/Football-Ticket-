import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import ClubList from '../../components/club/ClubList';
import ClubFilter from '../../components/club/ClubFilter';
import Pagination from '../../components/pagination/Pagination';
import clubApi from '../../services/clubApi';
import Layout from '../../components/layout/Layout';

const Clubs = () => {
  const [clubs, setClubs] = useState([]);
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
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    fetchClubs();
  }, [isAuthenticated, navigate, filters, pagination.currentPage, pageSize]);

  const fetchClubs = async () => {
    try {
      setIsLoading(true);
      setError(null);
      const response = await clubApi.getAllClubs({
        ...filters,
        page: pagination.currentPage,
        size: pageSize
      });
      setClubs(response.clubs);
      setPagination({
        currentPage: response.currentPage,
        totalPages: response.totalPages,
        totalElements: response.totalElements
      });
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      setError(errorMessage || 'Failed to load clubs. Please try again later.');
      if (error.message.includes('Please login')) {
        navigate('/login');
      }
    } finally {
      setIsLoading(false);
    }
  };

  const handleFilterChange = (newFilters) => {
    setFilters(newFilters);
    setPagination(prev => ({ ...prev, currentPage: 0 }));
  };

  const handleDelete = async (clubId) => {
    try {
      setError(null);
      await clubApi.deleteClub(clubId);
      fetchClubs(); // Refresh the current page after deletion
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      setError(errorMessage || 'Failed to delete club. Please try again.');
      if (error.message.includes('Please login')) {
        navigate('/login');
      }
    }
  };

  const handlePageChange = (newPage) => {
    setPagination(prev => ({ ...prev, currentPage: newPage }));
  };

  const handlePageSizeChange = (event) => {
    setPageSize(parseInt(event.target.value));
    setPagination(prev => ({ ...prev, currentPage: 0 }));
  };

  if (!isAuthenticated) return null;

  return (
    <Layout>
      <div className="container-fluid px-4 py-4" style={{ maxHeight: 'calc(100vh - 56px)', overflowY: 'auto' }}>
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fs-2 fw-bold mb-0">Clubs Management</h2>
          <div>
            <Link to="/create-club" className="btn btn-danger me-2">
              Create New Club
            </Link>
            <Link to="/" className="btn btn-outline-secondary">
              Back to Home
            </Link>
          </div>
        </div>

        <ClubFilter onFilterChange={handleFilterChange} />

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
            ) : clubs.length > 0 ? (
              <>
                <ClubList clubs={clubs} onDelete={handleDelete} />
                
                {pagination.totalPages > 1 && (
                  <Pagination
                    currentPage={pagination.currentPage}
                    totalPages={pagination.totalPages}
                    onPageChange={handlePageChange}
                  />
                )}
                
                <div className="text-muted text-center mt-3">
                  Showing {clubs.length} of {pagination.totalElements} clubs
                </div>
              </>
            ) : (
              <div className="text-center py-5">
                <p className="text-muted">No clubs found</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default Clubs;
import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import StadiumList from '../../components/stadium/StadiumList';
import StadiumFilter from '../../components/stadium/StadiumFilter';
import Pagination from '../../components/pagination/Pagination';
import stadiumApi from '../../services/stadiumApi';
import Layout from '../../components/layout/Layout';

const Stadiums = () => {
  const [stadiums, setStadiums] = useState([]);
  const [error, setError] = useState(null);
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
    fetchStadiums();
  }, [isAuthenticated, navigate, filters, pagination.currentPage, pageSize]);

  const fetchStadiums = async () => {
    try {
      const response = await stadiumApi.getAllStadiums({
        ...filters,
        page: pagination.currentPage,
        size: pageSize
      });
      setStadiums(response.stadiums);
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

      setError(errorMessage || 'Failed to load stadiums. Please try again later.');
      if (error.message.includes('Please login')) {
        navigate('/login');
      }
    }
  };

  const handleFilterChange = (newFilters) => {
    setFilters(newFilters);
    setPagination(prev => ({ ...prev, currentPage: 0 }));
  };

  const handleDeleteStadium = async (stadiumId) => {
    try {
      await stadiumApi.deleteStadium(stadiumId);
      fetchStadiums(); // Refresh the current page after deletion
      setError(null);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      setError(errorMessage || 'Failed to delete stadium');
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
          <h2 className="fs-2 fw-bold mb-0">Stadiums Management</h2>
          <div>
            <Link to="/create-stadium" className="btn btn-danger me-2">
              Create New Stadium
            </Link>
            <Link to="/" className="btn btn-outline-secondary">
              Back to Home
            </Link>
          </div>
        </div>

        <StadiumFilter onFilterChange={handleFilterChange} />

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
              >
                <option value="5">5 per page</option>
                <option value="10">10 per page</option>
                <option value="20">20 per page</option>
                <option value="50">50 per page</option>
              </select>
            </div>

            <StadiumList stadiums={stadiums} onDelete={handleDeleteStadium} />

            {pagination.totalPages > 1 && (
              <Pagination
                currentPage={pagination.currentPage}
                totalPages={pagination.totalPages}
                onPageChange={handlePageChange}
              />
            )}

            <div className="text-muted text-center mt-3">
              Showing {stadiums.length} of {pagination.totalElements} stadiums
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default Stadiums;
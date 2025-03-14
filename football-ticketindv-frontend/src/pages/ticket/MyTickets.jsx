
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import TicketList from '../../components/ticket/TicketList';
import TicketFilter from '../../components/ticket/TicketFilter';
import Pagination from '../../components/pagination/Pagination';
import ticketApi from '../../services/ticketApi';
import { useAuth } from '../../context/AuthContext';
import Layout from '../../components/layout/Layout';

const MyTickets = () => {
  const [tickets, setTickets] = useState([]);
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
  const { isAuthenticated, user } = useAuth();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }

    if (!user?.userId) {
      console.error('User ID is missing');
      setError('Unable to fetch tickets. Please try logging in again.');
      setIsLoading(false);
      return;
    }

    fetchUserTickets();
  }, [isAuthenticated, navigate, user, filters, pagination.currentPage, pageSize]);

  const fetchUserTickets = async () => {
    try {
      setIsLoading(true);
      setError(null);

      const response = await ticketApi.getUserTickets(user.userId, {
        ...filters,
        page: pagination.currentPage,
        size: pageSize
      });

      setTickets(response.tickets);
      setPagination({
        currentPage: response.currentPage,
        totalPages: response.totalPages,
        totalElements: response.totalElements
      });
    } catch (error) {
      console.error('Error fetching tickets:', error);
      if (error.response?.status === 401) {
        navigate('/login');
        return;
      }
      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;
        
      setError(errorMessage || 'Failed to load tickets. Please try again later.');
    } finally {
      setIsLoading(false);
    }
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

  return (
    <Layout>
      <div className="container-fluid px-4 py-4" style={{ maxHeight: 'calc(100vh - 56px)', overflowY: 'auto' }}>
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fs-2 fw-bold mb-0">My Tickets</h2>
        </div>

        {error && (
          <div className="alert alert-danger mb-4" role="alert">
            {error}
          </div>
        )}

        <TicketFilter onFilterChange={handleFilterChange} />

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

            {isLoading ? (
              <div className="d-flex justify-content-center py-5">
                <div className="spinner-border text-danger" role="status">
                  <span className="visually-hidden">Loading...</span>
                </div>
              </div>
            ) : (
              <>
                <TicketList tickets={tickets} />
                
                {pagination.totalPages > 1 && (
                  <Pagination
                    currentPage={pagination.currentPage}
                    totalPages={pagination.totalPages}
                    onPageChange={handlePageChange}
                  />
                )}
                
                <div className="text-muted text-center mt-3">
                  Showing {tickets.length} of {pagination.totalElements} tickets
                </div>
              </>
            )}
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default MyTickets;
import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import UserList from '../../components/user/UserList';
import UserFilter from '../../components/user/UserFilter';
import Pagination from '../../components/pagination/Pagination';
import userApi from '../../services/userApi';
import { useAuth } from '../../context/AuthContext';
import Layout from '../../components/layout/Layout';

const Users = () => {
  const [users, setUsers] = useState([]);
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
    fetchUsers();
  }, [isAuthenticated, navigate, filters, pagination.currentPage, pageSize]);

  const fetchUsers = async () => {
    try {
      setError(null);
      const response = await userApi.getAllUsers({
        ...filters,
        page: pagination.currentPage,
        size: pageSize
      });
      setUsers(response.users);
      setPagination({
        currentPage: response.currentPage,
        totalPages: response.totalPages,
        totalElements: response.totalElements
      });
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      setError(errorMessage || error.message);
      if (error.message.includes('Please login')) {
        navigate('/login');
      }
    }
  };

  const handleFilterChange = (newFilters) => {
    setFilters(newFilters);
    setPagination(prev => ({ ...prev, currentPage: 0 })); // Reset to first page on filter change
  };

  const handleDelete = async (userId) => {
    try {
      setError(null);
      await userApi.deleteUser(userId);
      fetchUsers(); // Refresh the current page after deletion
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      setError(errorMessage || error.message);
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

  return (
    <Layout>
      <div className="container-fluid px-4 py-4" style={{ maxHeight: 'calc(100vh - 56px)', overflowY: 'auto' }}>
        {error ? (
          <div className="alert alert-danger" role="alert">
            {error}
          </div>
        ) : (
          <>
            <div className="d-flex justify-content-between align-items-center mb-4">
              <h2 className="fs-2 fw-bold mb-0">Users Management</h2>
              <div>
                <Link to="/create-user" className="btn btn-danger me-2">Create New User</Link>
                <Link to="/" className="btn btn-outline-secondary">Back to Home</Link>
              </div>
            </div>
            
            <UserFilter onFilterChange={handleFilterChange} />
            
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
                
                <UserList users={users} onDelete={handleDelete} />
                
                {pagination.totalPages > 1 && (
                  <Pagination
                    currentPage={pagination.currentPage}
                    totalPages={pagination.totalPages}
                    onPageChange={handlePageChange}
                  />
                )}
                
                <div className="text-muted text-center mt-3">
                  Showing {users.length} of {pagination.totalElements} users
                </div>
              </div>
            </div>
          </>
        )}
      </div>
    </Layout>
  );
};

export default Users;
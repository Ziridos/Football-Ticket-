import React, { useState } from 'react';

const UserFilter = ({ onFilterChange }) => {
  const [filters, setFilters] = useState({
    name: '',
    email: '',
    role: ''
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    const updatedFilters = {
      ...filters,
      [name]: value
    };
    setFilters(updatedFilters);
    onFilterChange(updatedFilters);
  };

  const handleClearFilters = () => {
    const clearedFilters = {
      name: '',
      email: '',
      role: ''
    };
    setFilters(clearedFilters);
    onFilterChange(clearedFilters);
  };

  return (
    <div className="card mb-4 shadow-sm">
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h5 className="card-title mb-0">Filter Users</h5>
          {(filters.name || filters.email || filters.role) && (
            <button 
              className="btn btn-outline-secondary btn-sm"
              onClick={handleClearFilters}
            >
              Clear Filters
            </button>
          )}
        </div>
        <div className="row g-3">
          <div className="col-md-4">
            <div className="form-group">
              <label htmlFor="name" className="form-label">Name</label>
              <input
                type="text"
                className="form-control"
                id="name"
                name="name"
                value={filters.name}
                onChange={handleInputChange}
                placeholder="Filter by name"
              />
            </div>
          </div>
          <div className="col-md-4">
            <div className="form-group">
              <label htmlFor="email" className="form-label">Email</label>
              <input
                type="text"
                className="form-control"
                id="email"
                name="email"
                value={filters.email}
                onChange={handleInputChange}
                placeholder="Filter by email"
              />
            </div>
          </div>
          <div className="col-md-4">
            <div className="form-group">
              <label htmlFor="role" className="form-label">Role</label>
              <select
                className="form-select"
                id="role"
                name="role"
                value={filters.role}
                onChange={handleInputChange}
              >
                <option value="">All Roles</option>
                <option value="ADMIN">Admin</option>
                <option value="USER">User</option>
              </select>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserFilter;
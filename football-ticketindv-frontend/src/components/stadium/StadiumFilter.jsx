import React, { useState } from 'react';

const StadiumFilter = ({ onFilterChange }) => {
  const [filters, setFilters] = useState({
    name: '',
    city: '',
    country: ''
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
      city: '',
      country: ''
    };
    setFilters(clearedFilters);
    onFilterChange(clearedFilters);
  };

  return (
    <div className="card mb-4 shadow-sm">
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h5 className="card-title mb-0">Filter Stadiums</h5>
          {(filters.name || filters.city || filters.country) && (
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
              <label htmlFor="name" className="form-label">Stadium Name</label>
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
              <label htmlFor="city" className="form-label">City</label>
              <input
                type="text"
                className="form-control"
                id="city"
                name="city"
                value={filters.city}
                onChange={handleInputChange}
                placeholder="Filter by city"
              />
            </div>
          </div>
          <div className="col-md-4">
            <div className="form-group">
              <label htmlFor="country" className="form-label">Country</label>
              <input
                type="text"
                className="form-control"
                id="country"
                name="country"
                value={filters.country}
                onChange={handleInputChange}
                placeholder="Filter by country"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default StadiumFilter;
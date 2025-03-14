import React, { useState } from 'react';

const ClubFilter = ({ onFilterChange }) => {
  const [filters, setFilters] = useState({
    name: '',
    stadiumName: '',
    stadiumCity: '',
    stadiumCountry: ''
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
      stadiumName: '',
      stadiumCity: '',
      stadiumCountry: ''
    };
    setFilters(clearedFilters);
    onFilterChange(clearedFilters);
  };

  return (
    <div className="card mb-4 shadow-sm">
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h5 className="card-title mb-0">Filter Clubs</h5>
          {(filters.name || filters.stadiumName || filters.stadiumCity || filters.stadiumCountry) && (
            <button 
              className="btn btn-outline-secondary btn-sm"
              onClick={handleClearFilters}
            >
              Clear Filters
            </button>
          )}
        </div>
        <div className="row g-3">
          <div className="col-md-3">
            <div className="form-group">
              <label htmlFor="name" className="form-label">Club Name</label>
              <input
                type="text"
                className="form-control"
                id="name"
                name="name"
                value={filters.name}
                onChange={handleInputChange}
                placeholder="Filter by club name"
              />
            </div>
          </div>
          <div className="col-md-3">
            <div className="form-group">
              <label htmlFor="stadiumName" className="form-label">Stadium Name</label>
              <input
                type="text"
                className="form-control"
                id="stadiumName"
                name="stadiumName"
                value={filters.stadiumName}
                onChange={handleInputChange}
                placeholder="Filter by stadium name"
              />
            </div>
          </div>
          <div className="col-md-3">
            <div className="form-group">
              <label htmlFor="stadiumCity" className="form-label">Stadium City</label>
              <input
                type="text"
                className="form-control"
                id="stadiumCity"
                name="stadiumCity"
                value={filters.stadiumCity}
                onChange={handleInputChange}
                placeholder="Filter by stadium city"
              />
            </div>
          </div>
          <div className="col-md-3">
            <div className="form-group">
              <label htmlFor="stadiumCountry" className="form-label">Stadium Country</label>
              <input
                type="text"
                className="form-control"
                id="stadiumCountry"
                name="stadiumCountry"
                value={filters.stadiumCountry}
                onChange={handleInputChange}
                placeholder="Filter by stadium country"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ClubFilter;
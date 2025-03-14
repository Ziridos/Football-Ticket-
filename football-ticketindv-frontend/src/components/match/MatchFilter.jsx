import React, { useState } from 'react';

const MatchFilter = ({ onFilterChange }) => {
  const [filters, setFilters] = useState({
    homeClubName: '',
    awayClubName: '',
    competitionName: '',
    date: ''
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
      homeClubName: '',
      awayClubName: '',
      competitionName: '',
      date: ''
    };
    setFilters(clearedFilters);
    onFilterChange(clearedFilters);
  };

  return (
    <div className="card mb-4 shadow-sm">
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h5 className="card-title mb-0">Filter Matches</h5>
          {(filters.homeClubName || filters.awayClubName || filters.competitionName || filters.date) && (
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
              <label htmlFor="homeClubName" className="form-label">Home Club</label>
              <input
                type="text"
                className="form-control"
                id="homeClubName"
                name="homeClubName"
                value={filters.homeClubName}
                onChange={handleInputChange}
                placeholder="Filter by home club"
              />
            </div>
          </div>
          <div className="col-md-3">
            <div className="form-group">
              <label htmlFor="awayClubName" className="form-label">Away Club</label>
              <input
                type="text"
                className="form-control"
                id="awayClubName"
                name="awayClubName"
                value={filters.awayClubName}
                onChange={handleInputChange}
                placeholder="Filter by away club"
              />
            </div>
          </div>
          <div className="col-md-3">
            <div className="form-group">
              <label htmlFor="competitionName" className="form-label">Competition</label>
              <input
                type="text"
                className="form-control"
                id="competitionName"
                name="competitionName"
                value={filters.competitionName}
                onChange={handleInputChange}
                placeholder="Filter by competition"
              />
            </div>
          </div>
          <div className="col-md-3">
            <div className="form-group">
              <label htmlFor="date" className="form-label">Match Date</label>
              <input
                type="date"
                className="form-control"
                id="date"
                name="date"
                value={filters.date}
                onChange={handleInputChange}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MatchFilter;
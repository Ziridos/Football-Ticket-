import React, { useState } from 'react';

const CompetitionFilter = ({ onFilterChange }) => {
  const [filters, setFilters] = useState({
    name: ''
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
      name: ''
    };
    setFilters(clearedFilters);
    onFilterChange(clearedFilters);
  };

  return (
    <div className="card mb-4 shadow-sm">
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h5 className="card-title mb-0">Filter Competitions</h5>
          {filters.name && (
            <button 
              className="btn btn-outline-secondary btn-sm"
              onClick={handleClearFilters}
            >
              Clear Filter
            </button>
          )}
        </div>
        <div className="row">
          <div className="col-md-6">
            <div className="form-group">
              <label htmlFor="name" className="form-label">Competition Name</label>
              <input
                type="text"
                className="form-control"
                id="name"
                name="name"
                value={filters.name}
                onChange={handleInputChange}
                placeholder="Filter by competition name"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CompetitionFilter;
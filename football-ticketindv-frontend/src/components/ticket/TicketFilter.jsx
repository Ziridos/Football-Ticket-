
import React, { useState, useEffect } from 'react';

const TicketFilter = ({ onFilterChange }) => {
  const [filters, setFilters] = useState({
    year: '',
    quarter: ''
  });

  const currentYear = new Date().getFullYear();
  const years = Array.from({ length: 5 }, (_, i) => currentYear - i);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    const updatedFilters = {
      ...filters,
      [name]: value === '' ? null : Number(value)
    };
    setFilters(updatedFilters);
    onFilterChange(updatedFilters);
  };

  const handleClearFilters = () => {
    const clearedFilters = {
      year: '',
      quarter: ''
    };
    setFilters(clearedFilters);
    onFilterChange(clearedFilters);
  };

  return (
    <div className="card mb-4 shadow-sm">
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h5 className="card-title mb-0">Filter Tickets</h5>
          {(filters.year || filters.quarter) && (
            <button 
              className="btn btn-outline-secondary btn-sm"
              onClick={handleClearFilters}
            >
              Clear Filters
            </button>
          )}
        </div>
        <div className="row g-3">
          <div className="col-md-6">
            <div className="form-group">
              <label htmlFor="year" className="form-label">Year</label>
              <select
                className="form-select"
                id="year"
                name="year"
                value={filters.year}
                onChange={handleInputChange}
              >
                <option value="">All Years</option>
                {years.map(year => (
                  <option key={year} value={year}>{year}</option>
                ))}
              </select>
            </div>
          </div>
          <div className="col-md-6">
            <div className="form-group">
              <label htmlFor="quarter" className="form-label">Quarter</label>
              <select
                className="form-select"
                id="quarter"
                name="quarter"
                value={filters.quarter}
                onChange={handleInputChange}
              >
                <option value="">All Quarters</option>
                <option value="1">Q1 (Jan-Mar)</option>
                <option value="2">Q2 (Apr-Jun)</option>
                <option value="3">Q3 (Jul-Sep)</option>
                <option value="4">Q4 (Oct-Dec)</option>
              </select>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TicketFilter;
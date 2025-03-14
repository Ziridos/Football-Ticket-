import React, { useState } from 'react';
import ticketSalesApi from '../../services/ticketSalesApi';
import SalesChart from './SalesChart';

const QuarterlyStats = () => {
  const [year, setYear] = useState(new Date().getFullYear());
  const [quarterlyData, setQuarterlyData] = useState([]);
  const [error, setError] = useState(null);

  const fetchQuarterlyData = async () => {
    try {
      const quarterlyStats = await Promise.all(
        Array.from({ length: 4 }, async (_, i) => {
          const response = await ticketSalesApi.getQuarterlyStatistics(year, i + 1);
          return {
            label: `Q${i + 1}`,
            revenue: response.statistics.totalRevenue
          };
        })
      );
      setQuarterlyData(quarterlyStats);
    } catch (error) {
      console.error('Error fetching quarterly data:', error);
      setError('Failed to fetch quarterly data');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setError(null);
      await fetchQuarterlyData();
    } catch (error) {
      setError(error.message);
    }
  };

  return (
    <div>
      <form onSubmit={handleSubmit} className="mb-4">
        <div className="row g-3">
          <div className="col-md-10">
            <label className="form-label">Year</label>
            <input
              type="number"
              className="form-control"
              value={year}
              onChange={(e) => setYear(e.target.value)}
              min="2000"
              max="2100"
              required
            />
          </div>
          <div className="col-md-2 d-flex align-items-end">
            <button type="submit" className="btn btn-primary w-100">
              Get Statistics
            </button>
          </div>
        </div>
      </form>

      {error && (
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
      )}

      {quarterlyData.length > 0 && (
        <div className="card mb-4">
          <div className="card-body">
            <h5 className="card-title">Quarterly Revenue Overview - {year}</h5>
            <SalesChart data={quarterlyData} />
          </div>
        </div>
      )}
    </div>
  );
};

export default QuarterlyStats;
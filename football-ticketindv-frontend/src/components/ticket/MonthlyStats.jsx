import React, { useState } from 'react';
import ticketSalesApi from '../../services/ticketSalesApi';
import SalesChart from './SalesChart';

const MonthlyStats = () => {
  const [year, setYear] = useState(new Date().getFullYear());
  const [monthlyData, setMonthlyData] = useState([]);
  const [statistics, setStatistics] = useState(null);
  const [error, setError] = useState(null);

  const fetchMonthlyData = async () => {
    try {
      const monthlyStats = await Promise.all(
        Array.from({ length: 12 }, async (_, i) => {
          const response = await ticketSalesApi.getMonthlyStatistics(year, i + 1);
          return {
            label: new Date(2000, i).toLocaleString('default', { month: 'short' }),
            revenue: response.statistics.totalRevenue
          };
        })
      );
      setMonthlyData(monthlyStats);
    } catch (error) {
      console.error('Error fetching monthly data:', error);
      setError('Failed to fetch monthly data');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setError(null);
      await fetchMonthlyData();
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

      {monthlyData.length > 0 && (
        <>
        
          <div className="card mb-4">
            <div className="card-body">
              <h5 className="card-title">Monthly Revenue Overview - {year}</h5>
              <SalesChart data={monthlyData} />
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default MonthlyStats;
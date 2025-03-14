import React, { useState } from 'react';
import ticketSalesApi from '../../services/ticketSalesApi';
import StatisticsSummary from './StatisticsSummary';
import SalesChart from './SalesChart';

const StatisticsDatePicker = () => {
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [statistics, setStatistics] = useState(null);
  const [dailyData, setDailyData] = useState([]);
  const [error, setError] = useState(null);

  const getDatesInRange = (startDate, endDate) => {
    const dates = [];
    const currentDate = new Date(startDate);
    const end = new Date(endDate);

    while (currentDate <= end) {
      dates.push(new Date(currentDate));
      currentDate.setDate(currentDate.getDate() + 1);
    }

    return dates;
  };

  const fetchDailyData = async (startDate, endDate) => {
    try {
      const dates = getDatesInRange(startDate, endDate);
      
      
      const formattedDates = dates.map(date => ({
        label: date.toLocaleDateString('default', { month: 'short', day: 'numeric' }),
        fullDate: date.toISOString()
      }));

      
      const dailyStats = await Promise.all(
        formattedDates.map(async ({ label, fullDate }) => {
          const start = new Date(fullDate);
          start.setHours(0, 0, 0, 0);
          
          const end = new Date(fullDate);
          end.setHours(23, 59, 59, 999);

          try {
            const response = await ticketSalesApi.getTicketSalesStatistics(
              start.toISOString(),
              end.toISOString()
            );
            return {
              label,
              revenue: response.statistics.totalRevenue
            };
          } catch (error) {
            console.error(`Error fetching data for ${label}:`, error);
            return {
              label,
              revenue: 0
            };
          }
        })
      );

      setDailyData(dailyStats);
    } catch (error) {
      console.error('Error fetching daily data:', error);
      setError('Failed to fetch daily data');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setError(null);
      
      const formattedStartDate = new Date(startDate);
      const formattedEndDate = new Date(endDate);
      
      
      const data = await ticketSalesApi.getTicketSalesStatistics(
        formattedStartDate.toISOString(),
        formattedEndDate.toISOString()
      );
      setStatistics(data);

      
      await fetchDailyData(formattedStartDate, formattedEndDate);
    } catch (error) {
      setError(error.message);
      setStatistics(null);
      setDailyData([]);
    }
  };

  return (
    <div>
      <form onSubmit={handleSubmit} className="mb-4">
        <div className="row g-3">
          <div className="col-md-5">
            <label className="form-label">Start Date</label>
            <input
              type="datetime-local"
              className="form-control"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
              required
            />
          </div>
          <div className="col-md-5">
            <label className="form-label">End Date</label>
            <input
              type="datetime-local"
              className="form-control"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
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

      {statistics && (
        <>
          <StatisticsSummary statistics={statistics} />
          {dailyData.length > 0 && (
            <div className="card mt-4">
              <div className="card-body">
                <h5 className="card-title">Daily Revenue Overview</h5>
                <SalesChart data={dailyData} />
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default StatisticsDatePicker;
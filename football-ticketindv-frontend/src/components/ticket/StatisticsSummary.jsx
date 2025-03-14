import React from 'react';

const StatisticsSummary = ({ statistics }) => {
  const { statistics: stats, periodStart, periodEnd } = statistics;

  return (
    <div className="card">
      <div className="card-body">
        <h5 className="card-title mb-4">Sales Statistics</h5>
        <div className="row g-4">
          <div className="col-md-4">
            <div className="border rounded-3 p-3">
              <h6 className="text-muted mb-1">Total Tickets Sold</h6>
              <div className="fs-4 fw-bold">{stats.totalTickets}</div>
            </div>
          </div>
          <div className="col-md-4">
            <div className="border rounded-3 p-3">
              <h6 className="text-muted mb-1">Total Revenue</h6>
              <div className="fs-4 fw-bold">€{stats.totalRevenue.toFixed(2)}</div>
            </div>
          </div>
          <div className="col-md-4">
            <div className="border rounded-3 p-3">
              <h6 className="text-muted mb-1">Average Ticket Price</h6>
              <div className="fs-4 fw-bold">€{stats.averageTicketPrice.toFixed(2)}</div>
            </div>
          </div>
        </div>
        <div className="mt-4 text-muted">
          <small>Period: {periodStart} - {periodEnd}</small>
        </div>
      </div>
    </div>
  );
};

export default StatisticsSummary;
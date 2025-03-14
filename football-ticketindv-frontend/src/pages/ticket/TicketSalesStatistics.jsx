import React, { useState, useEffect  } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import Layout from '../../components/layout/Layout';
import StatisticsDatePicker from '../../components/ticket/StatisticsDatePicker';
import StatisticsSummary from '../../components/ticket/StatisticsSummary';
import QuarterlyStats from '../../components/ticket/QuarterlyStats';
import MonthlyStats from '../../components/ticket/MonthlyStats';

const TicketSalesStatistics = () => {
  const [activeTab, setActiveTab] = useState('custom');
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
    }
  }, [isAuthenticated, navigate]);

  return (
    <Layout>
      <div className="container-fluid px-4 py-4" style={{ maxHeight: 'calc(100vh - 56px)', overflowY: 'auto' }}>
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fs-2 fw-bold mb-0">Ticket Sales Statistics</h2>
        </div>

        <div className="card shadow-sm">
          <div className="card-body">
            <ul className="nav nav-tabs mb-4">
              <li className="nav-item">
                <button 
                  className={`nav-link ${activeTab === 'custom' ? 'active' : ''}`}
                  onClick={() => setActiveTab('custom')}
                >
                  Custom Date Range
                </button>
              </li>
              <li className="nav-item">
                <button 
                  className={`nav-link ${activeTab === 'quarterly' ? 'active' : ''}`}
                  onClick={() => setActiveTab('quarterly')}
                >
                  Quarterly Statistics
                </button>
              </li>
              <li className="nav-item">
                <button 
                  className={`nav-link ${activeTab === 'monthly' ? 'active' : ''}`}
                  onClick={() => setActiveTab('monthly')}
                >
                  Monthly Statistics
                </button>
              </li>
            </ul>

            {activeTab === 'custom' && <StatisticsDatePicker />}
            {activeTab === 'quarterly' && <QuarterlyStats />}
            {activeTab === 'monthly' && <MonthlyStats />}
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default TicketSalesStatistics;
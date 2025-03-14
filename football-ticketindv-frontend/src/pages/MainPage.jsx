import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Layout from '../components/layout/Layout';
import stadium from '../images/Stadium.png';

const MainPage = () => {
  const { user, isAuthenticated } = useAuth();

  return (
    <Layout>
      <div className="position-relative" style={{ height: "calc(100vh - 64px)" }}>
        <div className="position-absolute w-100 h-100">
          <img 
            src={stadium} 
            alt="Stadium" 
            className="w-100 h-100" 
            style={{ objectFit: "cover" }}
          />
          <div 
            className="position-absolute top-0 start-0 w-100 h-100" 
            style={{ backgroundColor: "rgba(0,0,0,0.6)" }}
          ></div>
        </div>
        
        <div className="position-relative h-100 container d-flex flex-column justify-content-end pb-5">
          <h1 className="display-1 fw-bold text-white mb-5" style={{ letterSpacing: "2px" }}>
            TICKET SALES
          </h1>

          {isAuthenticated && user?.role === 'ADMIN' && (
            <div className="mb-5">
              <h2 className="text-white fs-2 mb-4">Admin Management</h2>
              <div className="d-flex flex-wrap gap-2">
                <Link to="/users" className="btn btn-light fw-semibold px-4">Users</Link>
                <Link to="/competitions" className="btn btn-light fw-semibold px-4">Competitions</Link>
                <Link to="/stadiums" className="btn btn-light fw-semibold px-4">Stadiums</Link>
                <Link to="/clubs" className="btn btn-light fw-semibold px-4">Clubs</Link>
                <Link to="/matches" className="btn btn-light fw-semibold px-4">Matches</Link>
                <Link to="/tickets/statistics" className="btn btn-light fw-semibold px-4">Ticket Sales Statistics</Link>
              </div>
            </div>
          )}
          
          <div>
            <h2 className="text-white fs-2 mb-4">Ticket Purchase</h2>
            {!isAuthenticated ? (
              <>
                <p className="text-white fs-5 mb-4">
                  Want to order tickets? Click the button below for more information:
                </p>
                <Link
                  to="/login"
                  className="btn btn-danger btn-lg fw-semibold px-5 py-3"
                >
                  SALES OVERVIEW
                </Link>
              </>
            ) : (
              <Link
                to="/ticket-purchase"
                className="btn btn-danger btn-lg fw-semibold px-5 py-3"
              >
                PURCHASE TICKETS
              </Link>
            )}
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default MainPage;
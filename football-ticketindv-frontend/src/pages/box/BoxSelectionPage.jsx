import React, { useState, useEffect } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Stage, Layer } from 'react-konva';
import StadiumView from '../../components/stadium/StadiumView';
import boxApi from '../../services/boxApi';
import Layout from '../../components/layout/Layout';
import TicketPurchaseStepper from '../../components/ticket/TicketPurchaseStepper';


const BoxSelectionPage = () => {
  const { matchId } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [stadiumLayout, setStadiumLayout] = useState(null);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [stageSize, setStageSize] = useState({ 
    width: Math.min(window.innerWidth - 48, 1200),
    height: window.innerHeight - 300
  });

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
  }, [isAuthenticated, navigate]);

  useEffect(() => {
    const handleResize = () => {
      setStageSize({ 
        width: Math.min(window.innerWidth - 48, 1200),
        height: window.innerHeight - 300
      });
    };

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  useEffect(() => {
    if (!isAuthenticated) return;

    if (location.state?.match) {
      const stadium = location.state.match.homeClub.stadium;
      if (stadium?.id) {
        fetchStadiumLayout(stadium.id);
      } else {
        setError('Stadium ID is missing. Please try again or contact support.');
        setIsLoading(false);
      }
    } else {
      setError('Match data is missing. Please navigate back and try again.');
      setIsLoading(false);
    }
  }, [location.state, isAuthenticated]);
  
  const fetchStadiumLayout = async (stadiumId) => {
    try {
      setError(null);
      const formattedLayout = await boxApi.getBoxesByStadium(stadiumId);
      setStadiumLayout(formattedLayout);
    } catch (error) {
      if (error.message.includes('Please login')) {
        navigate('/login');
        return;
      }
      setError('Failed to load stadium layout. Please try again later.');
      console.error('Error fetching stadium layout:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleBoxSelect = (box) => {
    navigate(`/ticket-purchase/seats/${matchId}/${box.boxId}`, { 
      state: { match: location.state.match, box } 
    });
  };

  if (!isAuthenticated) return null;

  return (
    <Layout>
      <div className="container-fluid px-4 py-4">
      <TicketPurchaseStepper currentStep={2} />
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fs-2 fw-bold mb-0">Select a Box</h2>
        </div>

        {error && (
          <div className="alert alert-danger mb-4" role="alert">
            {error}
          </div>
        )}

        {isLoading ? (
          <div className="d-flex justify-content-center py-5">
            <div className="spinner-border text-danger" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        ) : (
          stadiumLayout && (
            <div className="card shadow-sm">
              <div className="card-body p-0">
                <div className="bg-light rounded" style={{ overflow: 'auto' }} data-testid="stadium-stage">
                  <Stage width={stageSize.width} height={stageSize.height}>
                    <Layer>
                      <StadiumView 
                        stadiumLayout={stadiumLayout} 
                        onSelectBox={handleBoxSelect} 
                      />
                    </Layer>
                  </Stage>
                </div>
              </div>
            </div>
          )
        )}
      </div>
    </Layout>
  );
};

export default BoxSelectionPage;
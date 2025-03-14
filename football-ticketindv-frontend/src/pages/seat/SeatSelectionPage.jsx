import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Stage, Layer } from 'react-konva';
import Block from '../../components/block/Block';
import TicketSummary from '../../components/ticket/TicketSummary';
import seatSelectionApi from '../../services/seatSelectionApi';
import ticketApi from '../../services/ticketApi';
import Layout from '../../components/layout/Layout';
import { webSocketService } from '../../services/webSocketService';
import Toast from '../../components/Toast/Toast';
import { loadStripe } from '@stripe/stripe-js';
import { Elements } from '@stripe/react-stripe-js';
import PaymentForm from '../../components/stripe/PaymentForm';
import TicketPurchaseStepper from '../../components/ticket/TicketPurchaseStepper';

const stripePromise = loadStripe(import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY);

const SeatSelectionPage = () => {
  const { matchId, boxId } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const { isAuthenticated, user } = useAuth();
  const [blocks, setBlocks] = useState([]);
  const [selectedSeats, setSelectedSeats] = useState([]);
  const [error, setError] = useState(null);
  const [stageSize, setStageSize] = useState({ 
    width: Math.min(window.innerWidth - 480, 1200),
    height: window.innerHeight - 300
  });
  const [match, setMatch] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [seatAvailability, setSeatAvailability] = useState({});
  const [boxPrice, setBoxPrice] = useState(0);
  const [temporaryLockedSeats, setTemporaryLockedSeats] = useState(new Set());
  const [toast, setToast] = useState(null);
  const [clientSecret, setClientSecret] = useState(null);


  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
  }, [isAuthenticated, navigate]);

  useEffect(() => {
    if (!isAuthenticated || !matchId || !user) return;

    const connectWebSocket = () => {
      webSocketService.connect(
        () => {
          console.log('Connected to WebSocket');
          webSocketService.subscribeToSeatUpdates(parseInt(matchId), handleSeatUpdate);
        },
        (error) => {
          console.error('WebSocket connection error:', error);
          setToast({
            message: 'Failed to connect to real-time updates. Please refresh the page.',
            type: 'danger'
          });
        }
      );
    };

    connectWebSocket();

    return () => {
      webSocketService.unsubscribeFromSeatUpdates(parseInt(matchId));
      webSocketService.disconnect();
    };
  }, [matchId, isAuthenticated, user]);

  useEffect(() => {
    const handleResize = () => {
      setStageSize({ 
        width: Math.min(window.innerWidth - 480, 1200),
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
        setMatch(location.state.match);
        
        const loadData = async () => {
          setIsLoading(true);
          try {
            const [boxesData, blocksData, availabilityData, selectedSeatsMap] = await Promise.all([
              seatSelectionApi.getBoxesByMatch(stadium.id, matchId),
              seatSelectionApi.getBlocksByBoxAndMatch(stadium.id, boxId, matchId),
              seatSelectionApi.getSeatAvailability(matchId),
              seatSelectionApi.getSelectedSeats(matchId)
            ]);
        
            const matchBox = boxesData.find(box => box.boxId === parseInt(boxId));
            if (matchBox) {
              setBoxPrice(matchBox.price);
            }
        
            const lockedSeats = new Set();
            const userSelectedSeats = [];
        
            Object.entries(selectedSeatsMap).forEach(([seatId, userId]) => {
              const parsedSeatId = parseInt(seatId);
              if (userId !== user?.userId) {
                lockedSeats.add(parsedSeatId);
              } else {
                for (const block of blocksData) {
                  const seat = block.seats?.find(s => s.seatId === parsedSeatId);
                  if (seat) {
                    userSelectedSeats.push({ ...seat, price: matchBox?.price || 0 });
                    break;
                  }
                }
              }
            });
        
            setBlocks(blocksData);
            setSeatAvailability(availabilityData);
            setTemporaryLockedSeats(lockedSeats);
            setSelectedSeats(userSelectedSeats);
            setError(null);
        
          } catch (error) {
            console.error('Error during initial data fetch:', error);
            setToast({
              message: 'Failed to load match data. Please try again later.',
              type: 'danger'
            });
          } finally {
            setIsLoading(false);
          }
        };

        loadData();
      } else {
        console.error('Stadium ID is undefined');
        setToast({
          message: 'Stadium ID is missing. Please try again or contact support.',
          type: 'danger'
        });
        setIsLoading(false);
      }
    } else {
      setToast({
        message: 'Match data is missing. Please go back and select a match.',
        type: 'danger'
      });
      setIsLoading(false);
    }
  }, [location.state, boxId, matchId, isAuthenticated]);

  useEffect(() => {
    if (toast) {
      const timer = setTimeout(() => {
        setToast(null);
      }, 3000);
      return () => clearTimeout(timer);
    }
  }, [toast]);

  const handleSeatUpdate = useCallback((update) => {
    console.log('Received seat update:', update);
    if (update.userId === user?.userId) {
      console.log('Ignoring own update');
      return;
    }

    setTemporaryLockedSeats(prev => {
      const newSet = new Set(prev);
      if (update.action === 'SELECT') {
        console.log(`Locking seat ${update.seatId} by user ${update.userId}`);
        newSet.add(update.seatId);
        setToast({
          message: `Seat ${update.seatId} has been selected by another user`,
          type: 'warning'
        });
      } else if (update.action === 'DESELECT') {
        console.log(`Unlocking seat ${update.seatId}`);
        newSet.delete(update.seatId);
        if (update.expired) {
          setToast({
            message: `Seat ${update.seatId} has been released (selection timed out)`,
            type: 'info'
          });
        } else {
          setToast({
            message: `Seat ${update.seatId} is now available`,
            type: 'success'
          });
        }
      }
      return newSet;
    });
  }, [user]);



  const handleSeatSelect = useCallback((seat) => {
    console.log('Attempting to select seat:', seat.seatId);
    
    if (temporaryLockedSeats.has(seat.seatId)) {
      console.log('Seat is locked by another user');
      setToast({
        message: 'This seat is currently being selected by another user',
        type: 'danger'
      });
      return;
    }

    setSelectedSeats(prevSeats => {
      const seatIndex = prevSeats.findIndex(s => s.seatId === seat.seatId);
      const action = seatIndex > -1 ? 'DESELECT' : 'SELECT';
      
      setToast({
        message: action === 'SELECT' 
          ? `Seat ${seat.seatId} has been selected` 
          : `Seat ${seat.seatId} has been deselected`,
        type: 'success'
      });

      webSocketService.sendSeatSelection(
        parseInt(matchId),
        seat.seatId,
        action,
        user?.userId
      );

      return seatIndex > -1
        ? prevSeats.filter(s => s.seatId !== seat.seatId)
        : [...prevSeats, { ...seat, price: boxPrice }];
    });
  }, [boxPrice, matchId, user, temporaryLockedSeats]);

  const handlePurchase = useCallback(async () => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }

    if (selectedSeats.length === 0 || !match) {
      setToast({
        message: 'Cannot purchase tickets: please select at least one seat.',
        type: 'danger'
      });
      return;
    }
  
    try {
      console.log('Creating payment intent with data:', {
        matchId: parseInt(matchId),
        seatIds: selectedSeats.map(seat => seat.seatId),
        userId: user.userId
      });

      const response = await ticketApi.createPaymentIntent({
        matchId: parseInt(matchId),
        seatIds: selectedSeats.map(seat => seat.seatId),
        userId: user.userId
      });
      
      console.log('Payment intent created:', response);
      setClientSecret(response.clientSecret);
    } catch (error) {
      console.error('Payment intent creation error:', error);
      setToast({
        message: `Failed to initiate payment: ${error.message}. Please try again.`,
        type: 'danger'
      });
      if (error.message.includes('Please login')) {
        navigate('/login');
      }
    }
  }, [selectedSeats, match, matchId, navigate, isAuthenticated, user]);

  const handlePaymentSuccess = useCallback(() => {
    selectedSeats.forEach(seat => {
      webSocketService.sendSeatSelection(
        parseInt(matchId),
        seat.seatId,
        'DESELECT',
        user?.userId
      );
    });
    setToast({
      message: 'Payment successful! Your tickets have been purchased.',
      type: 'success'
    });
    setTimeout(() => navigate('/'), 1500);
  }, [selectedSeats, matchId, user, navigate]);

  if (!isAuthenticated) return null;

  return (
    <Layout>
      <div className="container-fluid px-4 py-4 overflow-y-scroll" style={{ height: 'calc(100vh - 60px)' }}>
      <TicketPurchaseStepper 
          currentStep={3} 
          showPayment={clientSecret !== null} 
        />
        <div className="row">
          <div className="col-lg-8">
            <div className="d-flex justify-content-between align-items-center mb-4">
              <div>
                <h2 className="fs-2 fw-bold mb-1">Select Seats</h2>
                <p className="text-muted mb-0">
                  Box Price: ${boxPrice.toFixed(2)}
                </p>
              </div>
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
              <div className="card shadow-sm">
                <div className="card-body p-0">
                  <div className="bg-light rounded" style={{ overflow: 'auto' }} data-testid="seating-stage">
                    <Stage width={stageSize.width} height={stageSize.height}>
                      <Layer>
                        {blocks.map((block, index) => (
                          <Block
                            key={block.blockId || index}
                            blockId={block.blockId || index}
                            blockName={block.blockName}
                            x={block.x}
                            y={block.y}
                            width={block.width}
                            height={block.height}
                            seats={block.seats}
                            onSeatSelect={handleSeatSelect}
                            seatAvailability={seatAvailability}
                            selectedSeats={selectedSeats}
                            isCreationMode={false}
                            temporaryLockedSeats={temporaryLockedSeats}
                          />
                        ))}
                      </Layer>
                    </Stage>
                  </div>
                </div>
              </div>
            )}
          </div>

          <div className="col-lg-4">
            <div className="sticky-top" style={{ top: '1rem' }}>
              {selectedSeats.length > 0 && match ? (
                clientSecret ? (
                  <div className="card shadow-sm">
                    <div className="card-body">
                      <h3 className="fs-5 fw-bold mb-3">Complete Payment</h3>
                      <Elements 
                        stripe={stripePromise} 
                        options={{
                          clientSecret,
                          appearance: { theme: 'stripe' }
                        }}
                      >
                        <PaymentForm onSuccess={handlePaymentSuccess} />
                      </Elements>
                    </div>
                  </div>
                ) : (
                  <TicketSummary 
                    match={match}
                    seats={selectedSeats}
                    onPurchase={handlePurchase}
                  />
                )
              ) : (
                <div className="card shadow-sm">
                  <div className="card-body">
                    <p className="text-muted mb-0 text-center">
                      Please select at least one seat to see the ticket summary.
                    </p>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
        {toast && (
          <Toast
            message={toast.message}
            type={toast.type}
            onClose={() => setToast(null)}
          />
        )}
      </div>
    </Layout>
  );
};

export default SeatSelectionPage;
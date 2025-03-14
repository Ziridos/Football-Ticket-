import React from 'react';

const TicketSummary = ({ match, seats, onPurchase }) => {
  console.log('TicketSummary props:', { match, seats });

  if (!match || !seats || seats.length === 0) {
    console.warn('Match or seats data is missing in TicketSummary');
    return <div>Loading ticket summary...</div>;
  }

  const totalPrice = seats.reduce((sum, seat) => sum + (seat.price || 0), 0);

  return (
    <div className="card shadow-sm">
      <div className="card-body">
        <h2 className="fs-4 fw-bold mb-3">Ticket Summary</h2>
        <p>Match: {match.homeClub?.clubName} vs {match.awayClub?.clubName}</p>
        <p>Date: {match.matchDateTime}</p>
        <h3 className="fs-5 fw-bold mt-4 mb-2">Selected Seats:</h3>
        <ul className="list-unstyled">
          {seats.map(seat => (
            <li key={seat.seatId} className="mb-2">
              Seat {seat.seatNumber}: €{seat.price ? seat.price.toFixed(2) : 'Price not available'}
            </li>
          ))}
        </ul>
        <div className="border-top pt-3 mt-3">
          <p className="fs-5 fw-bold mb-3">Total Price: €{totalPrice.toFixed(2)}</p>
          <button 
            className="btn btn-primary w-100" 
            onClick={onPurchase}
          >
            Proceed to Payment
          </button>
        </div>
      </div>
    </div>
  );
};

export default TicketSummary;
import React from 'react';
import { format } from 'date-fns';
import ClubLogo from '../club/ClubLogo';

const TicketItem = ({ ticket }) => {
  return (
    <div className="list-group-item border rounded-3 shadow-sm">
      <div className="d-flex justify-content-between align-items-center p-3">
        <div className="flex-grow-1">
          <div className="d-flex justify-content-between mb-2">
            <div className="d-flex align-items-center gap-2">
              <div className="d-flex align-items-center">
                <ClubLogo 
                  club={ticket.match.homeClub} 
                  width="30px" 
                  height="30px" 
                  className="me-1"
                />
                <span className="fw-semibold">{ticket.match.homeClub.clubName}</span>
              </div>
              <span className="mx-2">vs</span>
              <div className="d-flex align-items-center">
                <ClubLogo 
                  club={ticket.match.awayClub} 
                  width="30px" 
                  height="30px" 
                  className="me-1"
                />
                <span className="fw-semibold">{ticket.match.awayClub.clubName}</span>
              </div>
            </div>
            <span className="badge bg-primary">
              {format(new Date(ticket.purchaseDateTime), 'dd MMM yyyy HH:mm')}
            </span>
          </div>
          <div className="text-muted mb-2">
            <i className="bi bi-calendar-event me-2"></i>
            Match Date: {format(new Date(ticket.match.matchDateTime), 'dd MMM yyyy HH:mm')}
          </div>
          <div className="mb-2">
            <strong>Competition:</strong> {ticket.match.competition.competitionName}
          </div>
          <div className="mb-2">
            <strong>Stadium:</strong> {ticket.match.homeClub.stadium.stadiumName}
          </div>
          <div className="mb-2">
            <strong>Seats:</strong> {ticket.seats.length} seats
            <ul className="list-unstyled ms-3 mb-0">
              {ticket.seats.map((seat) => (
                <li key={seat.id}>
                  Box: {seat.box.boxName}, Block: {seat.block.blockName}, Seat: {seat.seatNumber}
                </li>
              ))}
            </ul>
          </div>
          <div className="fs-5 fw-bold text-end text-danger">
            Total Price: €{ticket.totalPrice.toFixed(2)}
          </div>
        </div>
      </div>
    </div>
  );
};

export default TicketItem;
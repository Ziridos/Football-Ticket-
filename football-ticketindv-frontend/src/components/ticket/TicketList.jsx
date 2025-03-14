import React from 'react';
import TicketItem from './TicketItem';

const TicketList = ({ tickets }) => {
  return (
    <div>
      <div className="list-group gap-3">
        {tickets.length === 0 ? (
          <div className="text-center py-5 text-muted">
            <i className="bi bi-ticket-perforated fs-1 mb-3 d-block"></i>
            <p className="mb-0">No tickets found</p>
          </div>
        ) : (
          tickets.map((ticket) => (
            <TicketItem key={ticket.id} ticket={ticket} />
          ))
        )}
      </div>
    </div>
  );
};

export default TicketList;
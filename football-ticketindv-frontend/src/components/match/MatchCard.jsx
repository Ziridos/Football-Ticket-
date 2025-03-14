import React from 'react';
import ClubLogo from '../club/ClubLogo';

const MatchCard = ({ match, onSelect, isPast }) => {
  const matchDate = new Date(match.matchDateTime);

  return (
    <div 
      className="card h-100 shadow-sm" 
      style={{ 
        opacity: isPast ? 0.6 : 1,
        cursor: isPast ? 'not-allowed' : 'default'
      }}
    >
      <div className="card-body">
        <div className="d-flex align-items-center gap-2 mb-3">
          <div className="d-flex align-items-center">
            <ClubLogo 
              club={match.homeClub} 
              width="40px" 
              height="40px" 
              className="me-1"
            />
            <span className="fw-semibold">{match.homeClub.clubName}</span>
          </div>
          <span>vs</span>
          <div className="d-flex align-items-center">
            <ClubLogo 
              club={match.awayClub} 
              width="40px" 
              height="40px" 
              className="me-1"
            />
            <span className="fw-semibold">{match.awayClub.clubName}</span>
          </div>
        </div>
        <div className="card-text">
          <p className="mb-1">
            <strong>Competition:</strong> {match.competition.competitionName}
          </p>
          <p className="mb-1">
            <strong>Date:</strong> {matchDate.toLocaleDateString()}
          </p>
          <p className="mb-1">
            <strong>Time:</strong> {matchDate.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
          </p>
          <p className="mb-1">
            <strong>Venue:</strong> {match.homeClub.stadium.stadiumName}
          </p>
        </div>
      </div>
      <div className="card-footer bg-transparent border-top-0">
        <button
          className="btn btn-primary w-100"
          onClick={() => onSelect(match)}
          disabled={isPast}
        >
          {isPast ? 'Match Completed' : 'Select Seats'}
        </button>
      </div>
    </div>
  );
};

export default MatchCard;
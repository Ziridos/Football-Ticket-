import React from 'react';
import { Link } from 'react-router-dom';
import ClubLogo from '../club/ClubLogo';
import ConfirmationDialog from '../common/ConfirmationDialog';

const MatchItem = ({ match, onDelete }) => {
  const { homeClub, awayClub, matchDateTime, competition } = match;

  return (
    <div className="card mb-3">
      <div className="card-body">
        <div className="d-flex align-items-center mb-2">
          <div className="d-flex align-items-center">
            <ClubLogo 
              club={homeClub} 
              width="30px" 
              height="30px" 
              className="me-1"
            />
            <span className="fw-semibold">{homeClub.clubName}</span>
          </div>
          <span className="mx-2">vs</span>
          <div className="d-flex align-items-center">
            <ClubLogo 
              club={awayClub} 
              width="30px" 
              height="30px" 
              className="me-1"
            />
            <span className="fw-semibold">{awayClub.clubName}</span>
          </div>
        </div>
        <h6 className="card-subtitle mb-2 text-muted">Date: {new Date(matchDateTime).toLocaleString()}</h6>
        <p>Competition: {competition.competitionName}</p>
        <div className="d-flex justify-content-between">
          <Link to={`/edit-match/${match.matchId}`} className="btn btn-warning">Edit</Link>
          <ConfirmationDialog
            trigger={
              <button className="btn btn-danger">
                Delete
              </button>
            }
            title="Delete Match"
            description={`Are you sure you want to delete the match between ${homeClub.clubName} and ${awayClub.clubName} scheduled for ${new Date(matchDateTime).toLocaleString()}? This action cannot be undone.`}
            confirmLabel="Delete"
            onConfirm={() => onDelete(match.matchId)}
            variant="danger"
          />
        </div>
      </div>
    </div>
  );
};

export default MatchItem;
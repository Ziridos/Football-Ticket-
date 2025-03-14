import React from 'react';
import { Link } from 'react-router-dom';
import ClubLogo from './ClubLogo';
import ConfirmationDialog from '../common/ConfirmationDialog';

const ClubItem = ({ club, onDelete }) => {
  const { stadium } = club;
  const stadiumName = stadium?.stadiumName || 'N/A';
  const city = stadium?.stadiumCity || 'N/A';
  const country = stadium?.stadiumCountry || 'N/A';

  return (
    <div className="card mb-3">
      <div className="card-body">
        <ClubLogo 
          club={club} 
          width="100px" 
          height="100px" 
          className="mb-2"
        />
        <h5 className="card-title">{club.clubName}</h5>
        <h6 className="card-subtitle mb-2 text-muted">ID: {club.clubId}</h6>
        <p>Stadium: {stadiumName}</p>
        <p>City: {city}</p>
        <p>Country: {country}</p>
        <div className="d-flex justify-content-between">
          <Link to={`/edit-club/${club.clubId}`} className="btn btn-warning">Edit</Link>
          <ConfirmationDialog
            trigger={
              <button className="btn btn-danger">
                Delete
              </button>
            }
            title="Delete Club"
            description={`Are you sure you want to delete ${club.clubName}? This action cannot be undone.`}
            confirmLabel="Delete"
            onConfirm={() => onDelete(club.clubId)}
            variant="danger"
          />
        </div>
      </div>
    </div>
  );
};

export default ClubItem;
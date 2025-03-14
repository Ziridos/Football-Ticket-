import React from 'react';
import { Link } from 'react-router-dom';
import ConfirmationDialog from '../common/ConfirmationDialog';

const CompetitionItem = ({ competition, onDelete }) => {
  return (
    <div className="card mb-3">
      <div className="card-body">
        <h5 className="card-title">{competition.competitionName}</h5>
        <h6 className="card-subtitle mb-2 text-muted">ID: {competition.competitionId}</h6>
        <div className="d-flex justify-content-between">
          <Link to={`/edit-competition/${competition.competitionId}`} className="btn btn-warning">Edit</Link>
          <ConfirmationDialog
            trigger={
              <button className="btn btn-danger">
                Delete
              </button>
            }
            title="Delete Competition"
            description={`Are you sure you want to delete ${competition.competitionName}? This action cannot be undone.`}
            confirmLabel="Delete"
            onConfirm={() => onDelete(competition.competitionId)}
            variant="danger"
          />
        </div>
      </div>
    </div>
  );
};

export default CompetitionItem;
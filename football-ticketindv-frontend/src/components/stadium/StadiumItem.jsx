import React from 'react';
import { Link } from 'react-router-dom';
import ConfirmationDialog from '../common/ConfirmationDialog';

const StadiumItem = ({ stadium, onDelete }) => {
  return (
    <div className="card mb-3">
      <div className="card-body">
        <h5 className="card-title">{stadium.stadiumName}</h5>
        <h6 className="card-subtitle mb-2 text-muted">ID: {stadium.stadiumId}</h6>
        <p>{stadium.stadiumAddress}, {stadium.stadiumPostalCode}, {stadium.stadiumCity}, {stadium.stadiumCountry}</p>
        <div className="d-flex justify-content-between">
          <Link to={`/edit-stadium/${stadium.stadiumId}`} className="btn btn-warning">Edit</Link>
          <Link to={`/admin-box-builder/${stadium.stadiumId}`} className="btn btn-primary">Edit Box</Link>
          <Link to={`/box-pricing-rules/${stadium.stadiumId}`} className="btn btn-info">Pricing Rules</Link>
          <ConfirmationDialog
            trigger={
              <button className="btn btn-danger">
                Delete
              </button>
            }
            title="Delete Stadium"
            description={`Are you sure you want to delete ${stadium.stadiumName}? This will also delete all associated boxes and pricing rules. This action cannot be undone.`}
            confirmLabel="Delete"
            onConfirm={() => onDelete(stadium.stadiumId)}
            variant="danger"
          />
        </div>
      </div>
    </div>
  );
};

export default StadiumItem;
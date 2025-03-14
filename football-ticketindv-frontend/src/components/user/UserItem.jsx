import React from 'react';
import { Link } from 'react-router-dom';
import ConfirmationDialog from '../common/ConfirmationDialog';

const UserItem = ({ user, onDelete }) => {
  return (
    <div className="list-group-item border rounded-3 shadow-sm">
      <div className="d-flex justify-content-between align-items-center p-3">
        <div>
          <h5 className="fs-5 fw-semibold mb-1">{user.name}</h5>
          <div className="text-muted mb-1">ID: {user.id}</div>
          <div className="mb-1"><strong>Email:</strong> {user.email}</div>
          <div><strong>Country:</strong> {user.country}</div>
        </div>
        <div className="d-flex gap-2">
          <Link 
            to={`/edit-user/${user.id}`} 
            className="btn btn-warning"
          >
            Edit
          </Link>
          <ConfirmationDialog
            trigger={
              <button className="btn btn-danger">
                Delete
              </button>
            }
            title="Delete User"
            description={`Are you sure you want to delete ${user.name}? This action cannot be undone.`}
            confirmLabel="Delete"
            onConfirm={() => onDelete(user.id)}
            variant="destructive"
          />
        </div>
      </div>
    </div>
  );
};

export default UserItem;
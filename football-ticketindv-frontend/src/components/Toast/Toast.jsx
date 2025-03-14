import React from 'react';

const Toast = ({ message, type = 'info', onClose }) => {
  return (
    <div className={`fixed-bottom mb-4 mx-4 toast show bg-${type}`} role="alert" aria-live="assertive" aria-atomic="true">
      <div className="toast-header">
        <strong className="me-auto">Seat Update</strong>
        <button type="button" className="btn-close" onClick={onClose} aria-label="Close"></button>
      </div>
      <div className="toast-body">
        {message}
      </div>
    </div>
  );
};

export default Toast;
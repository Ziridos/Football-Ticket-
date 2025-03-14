import React, { useEffect } from 'react';
import { Check } from 'lucide-react';

const SuccessNotification = ({ message, isVisible, onClose, duration = 3000 }) => {
  useEffect(() => {
    if (isVisible) {
      const timer = setTimeout(() => {
        onClose();
      }, duration);

      return () => clearTimeout(timer);
    }
  }, [isVisible, onClose, duration]);

  if (!isVisible) return null;

  return (
    <div style={{ 
      position: 'fixed',
      top: '20px',
      left: '50%',
      transform: 'translateX(-50%)',
      zIndex: 9999,
      width: '90%',
      maxWidth: '500px'
    }}>
      <div className="alert alert-success d-flex align-items-center shadow-sm" role="alert">
        <Check size={20} className="me-2" />
        <span>{message}</span>
        <button 
          type="button" 
          className="btn-close ms-3" 
          onClick={onClose}
          aria-label="Close"
        />
      </div>
    </div>
  );
};

export default SuccessNotification;
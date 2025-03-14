import React from 'react';
import PropTypes from 'prop-types';

const ClubLogo = ({ 
  club, 
  width = '100px', 
  height = '100px', 
  className = '',
  showPlaceholderText = true 
}) => {
  if (club?.logo && club?.logoContentType) {
    return (
      <img
        src={`data:${club.logoContentType};base64,${club.logo}`}
        alt={`${club.clubName} logo`}
        style={{ 
          width, 
          height, 
          objectFit: 'contain' 
        }}
        className={className}
      />
    );
  }

  return (
    <div 
      className={`bg-light d-flex align-items-center justify-content-center ${className}`}
      style={{ 
        width, 
        height, 
        borderRadius: '4px' 
      }}
    >
      {showPlaceholderText && (
        <span className="text-muted small">No Logo</span>
      )}
    </div>
  );
};

ClubLogo.propTypes = {
  club: PropTypes.shape({
    clubName: PropTypes.string,
    logo: PropTypes.string,
    logoContentType: PropTypes.string
  }),
  width: PropTypes.string,
  height: PropTypes.string,
  className: PropTypes.string,
  showPlaceholderText: PropTypes.bool
};

export default ClubLogo;
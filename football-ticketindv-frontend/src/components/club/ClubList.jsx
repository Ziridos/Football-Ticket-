import React from 'react';
import ClubItem from './ClubItem';

const ClubList = ({ clubs, onDelete }) => {
  if (!Array.isArray(clubs) || clubs.length === 0) {
    return <p>No clubs available</p>;
  }

  return (
    <div>
      <h2>Club List</h2>
      <ul>
        {clubs.map((club) => (
          <ClubItem key={club.clubId} club={club} onDelete={onDelete} />
        ))}
      </ul>
    </div>
  );
};

export default ClubList;

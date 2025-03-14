import React from 'react';
import MatchItem from './MatchItem';

const MatchList = ({ matches, onDelete }) => {
  if (!Array.isArray(matches) || matches.length === 0) {
    return <p>No matches available</p>;
  }

  return (
    <div>
      <h2>Match List</h2>
      <ul>
        {matches.map((match) => (
          <MatchItem key={match.matchId} match={match} onDelete={onDelete} />
        ))}
      </ul>
    </div>
  );
};

export default MatchList;

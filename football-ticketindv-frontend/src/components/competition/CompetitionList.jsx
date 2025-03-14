import React from 'react';
import CompetitionItem from './CompetitionItem';

const CompetitionList = ({ competitions, onDelete }) => {
  if (!Array.isArray(competitions) || competitions.length === 0) {
    return <p>No competitions available</p>; 
  }

  return (
    <div>
      <h2>Competition List</h2>
      <ul>
        {competitions.map((competition) => (
          <CompetitionItem key={competition.competitionId} competition={competition} onDelete={onDelete} />
        ))}
      </ul>
    </div>
  );
};

export default CompetitionList;

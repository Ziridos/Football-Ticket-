import React from 'react';

const MatchSelector = ({ matches, onSelectMatch }) => {
  console.log('Matches received in MatchSelector:', matches);

  if (!Array.isArray(matches) || matches.length === 0) {
    return <p>No matches available.</p>;
  }

  return (
    <div>
      <h2>Select a Match</h2>
      <select onChange={(e) => {
        const selectedMatch = matches[e.target.value];
        console.log('Selected match:', selectedMatch);
        onSelectMatch(selectedMatch);
      }}>
        <option value="">-- Select a match --</option>
        {matches.map((match, index) => {
          console.log('Processing match:', match);
          const homeClubName = match.homeClub?.clubName || 'Unknown Home Club';
          const awayClubName = match.awayClub?.clubName || 'Unknown Away Club';
          const matchDateTime = match.matchDateTime ? new Date(match.matchDateTime).toLocaleString() : 'Unknown Date';
          const competitionName = match.competition?.competitionName || 'Unknown Competition';
          
          return (
            <option key={match.matchId || index} value={index}>
              {homeClubName} vs {awayClubName} - {matchDateTime} ({competitionName})
            </option>
          );
        })}
      </select>
    </div>
  );
};

export default MatchSelector;
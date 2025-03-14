import React from 'react';
import StadiumItem from './StadiumItem';

const StadiumList = ({ stadiums, onDelete }) => {
  return (
    <div>
      {stadiums.map((stadium) => (
        <StadiumItem key={stadium.stadiumId} stadium={stadium} onDelete={onDelete} />
      ))}
    </div>
  );
};

export default StadiumList;

import React from 'react';
import { Circle } from 'react-konva';

const Seat = ({ 
    x, 
    y, 
    seatId, 
    price, 
    isAvailable, 
    isSelected, 
    onSelect, 
    isCreationMode,
    isTemporaryLocked 
}) => {
    const getFillColor = () => {
        if (isCreationMode) return "#2196F3"; // Blue
        if (isSelected) return "#FFD700"; // Yellow
        if (isTemporaryLocked) return "#FF9800"; // Bright Orange
        return isAvailable ? "#4CAF50" : "#F44336"; // Green or Red
    };

    const handleSelect = () => {
        console.log(`Seat ${seatId} clicked. Available: ${isAvailable}, Locked: ${isTemporaryLocked}`);
        if (isCreationMode || (isAvailable && !isTemporaryLocked)) {
            onSelect({ seatId, price, x, y });
        }
    };

    return (
        <Circle
            x={x}
            y={y}
            radius={6}
            fill={getFillColor()}
            stroke={isSelected ? "black" : null}
            strokeWidth={isSelected ? 2 : 0}
            onClick={handleSelect}
            listening={isCreationMode || (isAvailable && !isTemporaryLocked)}
            shadowColor="black"
            shadowBlur={isTemporaryLocked ? 4 : 0}
            shadowOpacity={0.3}
        />
    );
};

export default Seat;
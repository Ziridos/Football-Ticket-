import React from 'react';
import { Group, Rect, Text } from 'react-konva';
import Seat from '../seat/Seat';

const Block = ({ 
    blockId, 
    blockName, 
    x, 
    y, 
    width, 
    height, 
    seats, 
    onSeatSelect, 
    seatAvailability, 
    selectedSeats = [], 
    isCreationMode,
    temporaryLockedSeats = new Set() 
}) => {
    console.log('Block props:', { 
        blockId, 
        blockName, 
        x, 
        y, 
        width, 
        height, 
        temporaryLockedSeats: Array.from(temporaryLockedSeats)
        
    });

    

    if (!Array.isArray(seats)) {
        console.error('Seats is not an array:', seats);
        return null;
    }

    return (
        <Group>
            <Rect 
                x={x} 
                y={y} 
                width={width} 
                height={height} 
                fill="lightgrey" 
                stroke="black" 
                strokeWidth={4}
                dash={[10, 5]}
            />
            <Text text={blockName} x={x + 10} y={y - 20} fill="black" />

            {seats.map((seat) => (
                <Seat 
                    key={seat.seatId}
                    x={seat.xposition}
                    y={seat.yposition}
                    seatId={seat.seatId}
                    isAvailable={isCreationMode || seatAvailability?.[seat.seatId]}
                    isSelected={isCreationMode ? false : selectedSeats.some(s => s.seatId === seat.seatId)}
                    isTemporaryLocked={temporaryLockedSeats.has(seat.seatId)}
                    onSelect={() => {
                        if (isCreationMode || (seatAvailability?.[seat.seatId] && !temporaryLockedSeats.has(seat.seatId))) {
                            onSeatSelect(seat);
                        }
                    }}
                    isCreationMode={isCreationMode}
                />
            ))}
        </Group>
    );
};

export default Block;
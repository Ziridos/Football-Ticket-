import React from 'react';
import { Group, Rect, Text } from 'react-konva';

const BoxView = ({ box, availableSeats, onSelectSeat }) => {
  return (
    <Group>
      {box.blocks.map((block) => (
        <Group key={block.blockId}>
          <Rect
            x={block.xPosition}
            y={block.yPosition}
            width={block.width}
            height={block.height}
            fill="lightgrey"
            stroke="grey"
          />
          <Text
            text={block.blockName}
            x={block.xPosition + 5}
            y={block.yPosition - 20}
            fill="black"
          />
          {block.seats.map((seat) => (
            <Rect
              key={seat.seatId}
              x={seat.xPosition}
              y={seat.yPosition}
              width={20}
              height={20}
              fill={availableSeats[seat.seatId] ? "green" : "red"}
              stroke="black"
              onClick={() => availableSeats[seat.seatId] && onSelectSeat(seat)}
            />
          ))}
        </Group>
      ))}
    </Group>
  );
};

export default BoxView;
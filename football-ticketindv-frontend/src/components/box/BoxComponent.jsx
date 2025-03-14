import React from 'react';
import { Rect, Text, Group } from 'react-konva';

const BoxComponent = ({ boxId, boxName, x, y, width, height, price, onMouseDown, onMouseUp }) => {
  const displayPrice = price !== undefined ? `$${price.toFixed(2)}` : 'Price not set';

  return (
    <Group
      onMouseDown={onMouseDown}
      onMouseUp={onMouseUp}
      onTouchStart={onMouseDown}
      onTouchEnd={onMouseUp}
    >
      <Rect
        x={x}
        y={y}
        width={width}
        height={height}
        fill="lightblue"
        stroke="blue"
        strokeWidth={2}
      />
      <Text 
        text={`${boxName} (${displayPrice})`}
        x={x + 5}
        y={y + 5}
        fill="black"
      />
    </Group>
  );
};

export default BoxComponent;
import React from 'react';
import { Group, Rect, Text } from 'react-konva';

const StadiumView = ({ stadiumLayout, onSelectBox }) => {
  console.log('StadiumView rendered with stadiumLayout:', stadiumLayout);

  if (!stadiumLayout || !Array.isArray(stadiumLayout)) {
    console.warn('StadiumView: stadiumLayout is not an array or is undefined', stadiumLayout);
    return null;
  }

  return (
    <Group>
      {stadiumLayout.map((box, index) => (
        <Group key={box.boxId || index} onClick={() => onSelectBox(box)} id={`box-${box.boxName}`}>
          <Rect
            x={box.x}
            y={box.y}
            width={box.width || 50}
            height={box.height || 50}
            fill="lightgrey"
            stroke="black"
          />
          <Text
            x={box.x}
            y={box.y}
            text={box.boxName || `Box ${index + 1}`}
            fontSize={12}
            fill="black"
          />
        </Group>
      ))}
    </Group>
  );
};

export default StadiumView;
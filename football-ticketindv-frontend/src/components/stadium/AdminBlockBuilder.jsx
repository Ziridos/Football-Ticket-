import React, { useState, useEffect, useRef } from 'react';
import { Stage, Layer } from 'react-konva';
import Block from '../block/Block';
import ConfirmationDialog from '../common/ConfirmationDialog';

const AdminBlockBuilder = ({ boxId, onSaveBlock, existingBlocks, OnDeleteBlock }) => {
  const [blocks, setBlocks] = useState(existingBlocks || []);
  const [currentBlock, setCurrentBlock] = useState(null);
  const [isDrawing, setIsDrawing] = useState(false);
  const [blockName, setBlockName] = useState('');
  let seatIdCounter = 1;

  //Deleting
  const [selectedBlock, setSelectedBlock] = useState(null);
  const longPressTimer = useRef(null);
  const longPressDuration = 500;

  useEffect(() => {
    setBlocks(existingBlocks);
  }, [existingBlocks]);

  const checkBlockOverlap = (newBlock, existingBlocks) => {
    return existingBlocks.some(existingBlock => {
      return !(
        newBlock.x + newBlock.width < existingBlock.x ||
        newBlock.x > existingBlock.x + existingBlock.width ||
        newBlock.y + newBlock.height < existingBlock.y ||
        newBlock.y > existingBlock.y + existingBlock.height
      );
    });
  };

  const generateSeats = (x, y, width, height) => {
    const rows = Math.floor(height / 20);
    const cols = Math.floor(width / 20);
    const seats = [];
  
    for (let row = 0; row < rows; row++) {
      for (let col = 0; col < cols; col++) {
        seats.push({
          seatId: seatIdCounter++,
          xposition: x + 10 + col * 20,
          yposition: y + 10 + row * 20,
        });
      }
    }
    return seats;
  };

  const handleMouseDown = (e) => {
    const stage = e.target.getStage();
    const pointerPos = stage?.getPointerPosition();
    if (!pointerPos) return;
  
    const { x, y } = pointerPos;
    // Remove any unsaved block and reset to existing blocks
    setBlocks(existingBlocks || []);
    setCurrentBlock({ x, y, width: 0, height: 0, seats: [] });
    setIsDrawing(true);
  };

  const handleMouseMove = (e) => {
    if (!isDrawing || !currentBlock) return;
    
    const stage = e.target.getStage();
    const pointerPos = stage?.getPointerPosition();
    if (!pointerPos) return;
    
    const { x, y } = pointerPos;
    const newWidth = Math.max(0, x - currentBlock.x);
    const newHeight = Math.max(0, y - currentBlock.y);

    // Check for overlap with existing (saved) blocks
    const tempBlock = {
      x: currentBlock.x,
      y: currentBlock.y,
      width: newWidth,
      height: newHeight
    };

    if (checkBlockOverlap(tempBlock, existingBlocks)) {
      return; // Don't update if there's an overlap with existing blocks
    }

    const newSeats = generateSeats(currentBlock.x, currentBlock.y, newWidth, newHeight);
    setCurrentBlock({ 
      ...currentBlock, 
      width: newWidth, 
      height: newHeight, 
      seats: newSeats 
    });
  };

  const handleMouseUp = () => {
    setIsDrawing(false);
    if (currentBlock && currentBlock.width > 0 && currentBlock.height > 0) {
      const newBlock = { 
        ...currentBlock, 
        blockName: blockName || `Block ${blocks.length + 1}` 
      };

      // Check for overlap with existing (saved) blocks
      if (checkBlockOverlap(newBlock, existingBlocks)) {
        setCurrentBlock(null);
        throw new Error('Block overlap');
        return;
      }

      setBlocks([...existingBlocks, newBlock]);
      setCurrentBlock(null);
      setBlockName('');
    }
  };

  //DeletingBlock
  const deleteBlock = async () => {
    if (selectedBlock && selectedBlock.blockId) {
      try {
        await onDeleteBlock(selectedBlock.blockId);
        setBlocks(prevBlocks => prevBlocks.filter(block => block.blockId !== selectedBlock.blockId));
        setSelectedBlock(null);
      } catch (error) {
        console.error('Error deleting block:', error);
        throw error;
      }
    }
  };

  const handleBlockMouseDown = (blockId) => {
    longPressTimer.current = setTimeout(() => {
      const block = blocks.find(b => b.blockId === blockId);
      setSelectedBlock(block);
    }, longPressDuration);
  };

  const handleBlockMouseUp = () => {
    if (longPressTimer.current) {
      clearTimeout(longPressTimer.current);
    }
  };

  const saveBlock = async () => {
    if (blocks.length > existingBlocks.length) {
      const unsavedBlock = blocks[blocks.length - 1];

      // One final check for overlap before saving
      if (checkBlockOverlap(unsavedBlock, existingBlocks)) {
        setBlocks(existingBlocks);
        throw new Error('Block overlap');
        return;
      }

      const blockToSave = {
        ...unsavedBlock,
        seats: unsavedBlock.seats.map(seat => ({
          ...seat,
          seatNumber: seat.seatId.toString()
        }))
      };
      
      try {
        await onSaveBlock(blockToSave);
      } catch (error) {
        // Remove the unsaved block if save fails
        setBlocks(existingBlocks);
        throw error;
      }
    } else {
      console.error('No block to save');
    }
  };

  return (
    <div>
      <input
        type="text"
        placeholder="Block Name"
        value={blockName}
        onChange={(e) => setBlockName(e.target.value)}
      />
      <button onClick={saveBlock}>Save Block</button>

      {selectedBlock && (
        <div>
          <h3>Selected Block: {selectedBlock.blockName}</h3>
          <ConfirmationDialog
            trigger={
              <button className="btn btn-danger">
                Delete Block
              </button>
            }
            title="Delete Block"
            description={`Are you sure you want to delete ${selectedBlock.blockName}? This will also delete all seats within this block. This action cannot be undone.`}
            confirmLabel="Delete"
            onConfirm={deleteBlock}
            variant="destructive"
          />
          <button onClick={() => setSelectedBlock(null)}>Cancel</button>
        </div>
      )}

      <Stage
        width={window.innerWidth}
        height={window.innerHeight}
        onMouseDown={handleMouseDown}
        onMouseMove={handleMouseMove}
        onMouseUp={handleMouseUp}
      >
        <Layer>
          {blocks.map((block, index) => (
            <Block
              key={index}
              blockId={block.blockId || index}
              blockName={block.blockName}
              x={block.x}
              y={block.y}
              width={block.width}
              height={block.height}
              seats={block.seats || []}
              isCreationMode={true}
              onSeatSelect={() => {}}
              seatAvailability={{}}
            />
          ))}

          {isDrawing && currentBlock && (
            <Block
              key="current"
              blockId="current"
              blockName={blockName || `Block ${blocks.length + 1}`}
              x={currentBlock.x}
              y={currentBlock.y}
              width={currentBlock.width}
              height={currentBlock.height}
              seats={currentBlock.seats || []}
              isCreationMode={true}
              onSeatSelect={() => {}}
              seatAvailability={{}}
            />
          )}
        </Layer>
      </Stage>
    </div>
  );
};

export default AdminBlockBuilder;
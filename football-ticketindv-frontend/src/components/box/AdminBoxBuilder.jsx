import React, { useState, useEffect, useCallback, useRef } from 'react';
import { Stage, Layer } from 'react-konva';
import { useNavigate } from 'react-router-dom';
import BoxComponent from './BoxComponent';
import ConfirmationDialog from '../common/ConfirmationDialog';

const AdminBoxBuilder = ({ stadiumId, onSaveBox, onUpdateBoxPrice, existingBoxes, onDeleteBox }) => {
  const [boxes, setBoxes] = useState(existingBoxes || []);
  const [currentBox, setCurrentBox] = useState(null);
  const [isDrawing, setIsDrawing] = useState(false);
  const [boxName, setBoxName] = useState('');
  const [boxPrice, setBoxPrice] = useState('');
  const [selectedBox, setSelectedBox] = useState(null);
  const [updatePrice, setUpdatePrice] = useState('');
  const [stageSize, setStageSize] = useState({ width: window.innerWidth, height: window.innerHeight });
  const navigate = useNavigate();
  const longPressTimer = useRef(null);
  const longPressDuration = 500;

  useEffect(() => {
    setBoxes(existingBoxes || []);
  }, [existingBoxes]);

  useEffect(() => {
    const handleResize = () => {
      setStageSize({ width: window.innerWidth, height: window.innerHeight });
    };

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  const checkBoxOverlap = (box, existingBoxes) => {
    return existingBoxes.some(existingBox => {
      return !(
        box.x + box.width < existingBox.x ||
        box.x > existingBox.x + existingBox.width ||
        box.y + box.height < existingBox.y ||
        box.y > existingBox.y + existingBox.height
      );
    });
  };

  const handleMouseDown = useCallback((e) => {
    const stage = e.target.getStage();
    const pointerPos = stage?.getPointerPosition();
    if (!pointerPos) return;

    // Reset to only existing boxes when starting a new box
    setBoxes(existingBoxes || []);
    const { x, y } = pointerPos;
    setCurrentBox({ x, y, width: 0, height: 0, price: parseFloat(boxPrice) || 0 });
    setIsDrawing(true);
  }, [boxPrice, existingBoxes]);

  const handleMouseMove = useCallback((e) => {
    if (!isDrawing || !currentBox) return;
    
    const stage = e.target.getStage();
    const pointerPos = stage?.getPointerPosition();
    if (!pointerPos) return;
    
    const { x, y } = pointerPos;
    const newWidth = Math.max(0, x - currentBox.x);
    const newHeight = Math.max(0, y - currentBox.y);

    // Check overlap with existing boxes during drawing
    const tempBox = {
      x: currentBox.x,
      y: currentBox.y,
      width: newWidth,
      height: newHeight
    };

    if (checkBoxOverlap(tempBox, existingBoxes)) {
      return; // Don't update if there's an overlap
    }

    setCurrentBox(prev => ({
      ...prev,
      width: newWidth,
      height: newHeight
    }));
  }, [isDrawing, currentBox, existingBoxes]);

  const handleMouseUp = useCallback(() => {
    setIsDrawing(false);
    if (currentBox && currentBox.width > 0 && currentBox.height > 0) {
      const newBox = { 
        ...currentBox, 
        boxName: boxName || `Box ${existingBoxes.length + 1}`,
        price: parseFloat(boxPrice) || 0
      };
      
      // Check for overlap with existing boxes
      if (checkBoxOverlap(newBox, existingBoxes)) {
        setCurrentBox(null);
        return;
      }

      setBoxes([...existingBoxes, newBox]);
      setCurrentBox(null);
      setBoxName('');
      setBoxPrice('');
    }
  }, [currentBox, boxName, boxPrice, existingBoxes]);

  const saveBox = async () => {
    if (boxes.length > existingBoxes.length) {
      const unsavedBox = boxes[boxes.length - 1];
      
      // Final overlap check before saving
      if (checkBoxOverlap(unsavedBox, existingBoxes)) {
        setBoxes(existingBoxes); // Reset to only saved boxes
        return;
      }

      try {
        await onSaveBox(unsavedBox);
        console.log('Box saved successfully:', unsavedBox);
      } catch (error) {
        setBoxes(existingBoxes); // Reset on error
        console.error('Error saving box:', error);
      }
    } else {
      console.error('No box to save');
    }
  };

  const deleteSelectedBox = async () => {
    if (selectedBox && selectedBox.boxId) {
      try {
        await onDeleteBox(selectedBox.boxId);
        setBoxes(prevBoxes => prevBoxes.filter(box => box.boxId !== selectedBox.boxId));
        setSelectedBox(null);
      } catch (error) {
        console.error('Error deleting box:', error);
      }
    }
  };

  // Rest of your component remains the same...
  const handleBoxMouseDown = (boxId) => {
    longPressTimer.current = setTimeout(() => {
      const box = boxes.find(b => b.boxId === boxId);
      setSelectedBox(box);
      setUpdatePrice(box.price !== undefined ? box.price.toString() : '');
    }, longPressDuration);
  };

  const handleBoxMouseUp = (boxId) => {
    if (longPressTimer.current) {
      clearTimeout(longPressTimer.current);
    }
    if (!selectedBox) {
      navigate(`/admin-block-builder/${boxId}`, { state: { stadiumId, boxId } });
    }
  };

  const handleUpdatePrice = async () => {
    if (selectedBox && updatePrice) {
      try {
        await onUpdateBoxPrice(selectedBox.boxId, parseFloat(updatePrice));
        setBoxes(prevBoxes => prevBoxes.map(box => 
          box.boxId === selectedBox.boxId ? { ...box, price: parseFloat(updatePrice) } : box
        ));
        setSelectedBox(null);
        setUpdatePrice('');
      } catch (error) {
        console.error('Error updating box price:', error);
      }
    }
  };

  

  return (
    <div>
      <input
        type="text"
        placeholder="Box Name"
        value={boxName}
        onChange={(e) => setBoxName(e.target.value)}
      />

      <button onClick={saveBox}>Save Box</button>

      {selectedBox && (
        <div>
          <h3>Update Price for {selectedBox.boxName}</h3>
          <input
            type="number"
            value={updatePrice}
            onChange={(e) => setUpdatePrice(e.target.value)}
          />
          <button onClick={handleUpdatePrice}>Update Price</button>
          <ConfirmationDialog
            trigger={
              <button className="btn btn-danger">
                Delete Box
              </button>
            }
            title="Delete Box"
            description={`Are you sure you want to delete ${selectedBox.boxName}? This action cannot be undone.`}
            confirmLabel="Delete"
            onConfirm={deleteSelectedBox}
            variant="destructive"
          />
          <button onClick={() => setSelectedBox(null)}>Cancel</button>
        </div>
      )}

      <Stage
        width={stageSize.width}
        height={stageSize.height}
        onMouseDown={handleMouseDown}
        onMouseMove={handleMouseMove}
        onMouseUp={handleMouseUp}
      >
        <Layer>
          {boxes.map((box, index) => (
            <BoxComponent
              key={box.boxId || index}
              boxId={box.boxId || index}
              boxName={box.boxName}
              x={box.x}
              y={box.y}
              width={box.width}
              height={box.height}
              price={box.price}
              onMouseDown={() => handleBoxMouseDown(box.boxId)}
              onMouseUp={() => handleBoxMouseUp(box.boxId)}
            />
          ))}
          {isDrawing && currentBox && (
            <BoxComponent
              key="current"
              boxId="current"
              boxName={boxName || `Box ${boxes.length + 1}`}
              x={currentBox.x}
              y={currentBox.y}
              width={currentBox.width}
              height={currentBox.height}
              price={currentBox.price}
              onMouseDown={() => {}}
              onMouseUp={() => {}}
            />
          )}
        </Layer>
      </Stage>
    </div>
  );
};

export default AdminBoxBuilder;
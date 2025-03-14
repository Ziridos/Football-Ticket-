import React, { useState } from 'react';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

const ConfirmationDialog = ({
  trigger,
  title = "Are you sure?",
  description = "This action cannot be undone.",
  confirmLabel = "Confirm",
  cancelLabel = "Cancel",
  onConfirm,
  variant = "danger"
}) => {
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const handleConfirm = () => {
    onConfirm();
    handleClose();
  };

  return (
    <>
      {/* Clone the trigger button and add onClick handler */}
      {React.cloneElement(trigger, { onClick: handleShow })}

      <Modal 
        show={show} 
        onHide={handleClose}
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>{title}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {description}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            {cancelLabel}
          </Button>
          <Button variant={variant} onClick={handleConfirm}>
            {confirmLabel}
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ConfirmationDialog;
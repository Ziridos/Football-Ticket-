import React from 'react';
import { Check } from 'lucide-react';

const TicketPurchaseStepper = ({ currentStep, showPayment = false }) => {
  const steps = [
    { id: 1, label: "Match Selection", path: "/matches" },
    { id: 2, label: "Box Selection", path: "/ticket-purchase/boxes" },
    { id: 3, label: "Seat Selection", path: "/ticket-purchase/seats" },
    { id: 4, label: "Payment", path: "/payment", isSubStep: true }
  ];

  return (
    <div className="mb-4">
      <nav className="d-flex justify-content-between align-items-center">
        {steps.map((step, index) => {
          const isComplete = step.id < currentStep;
          const isCurrent = step.id === currentStep;
          const isSubStepHidden = step.isSubStep && !showPayment;

          if (isSubStepHidden) return null;

          return (
            <React.Fragment key={step.id}>
              <div className="d-flex flex-column align-items-center position-relative" style={{ flex: 1 }}>
                <div
                  className={`rounded-circle d-flex align-items-center justify-content-center mb-2
                    ${isComplete ? 'bg-success' : isCurrent ? 'bg-primary' : 'bg-light'}
                    ${isComplete || isCurrent ? 'text-white' : 'text-muted'}`}
                  style={{ width: '40px', height: '40px' }}
                >
                  {isComplete ? (
                    <Check size={20} />
                  ) : (
                    <span className="fw-bold">{step.id}</span>
                  )}
                </div>
                <span className={`text-center small ${isCurrent ? 'fw-bold' : 'text-muted'}`}>
                  {step.label}
                </span>
                {index < (showPayment ? steps.length - 1 : steps.length - 2) && (
                  <div
                    className={`position-absolute top-50 start-50 w-100 
                      ${isComplete ? 'bg-success' : 'bg-light'}`}
                    style={{
                      height: '2px',
                      transform: 'translateY(-20px)',
                      zIndex: -1,
                      width: '100%'
                    }}
                  />
                )}
              </div>
            </React.Fragment>
          );
        })}
      </nav>
    </div>
  );
};

export default TicketPurchaseStepper;
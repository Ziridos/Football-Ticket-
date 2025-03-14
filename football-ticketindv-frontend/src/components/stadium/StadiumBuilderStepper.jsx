import React from 'react';
import { Check, Square, LayoutGrid } from 'lucide-react';

const StadiumBuilderStepper = ({ currentStep }) => {
  const steps = [
    { id: 1, label: "Box Creation", icon: Square },
    { id: 2, label: "Block & Seat Setup", icon: LayoutGrid }
  ];

  return (
    <div className="mb-4">
      <nav className="d-flex justify-content-between align-items-center">
        {steps.map((step, index) => {
          const Icon = step.icon;
          const isComplete = step.id < currentStep;
          const isCurrent = step.id === currentStep;

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
                    <Icon size={20} />
                  )}
                </div>
                <span className={`text-center small ${isCurrent ? 'fw-bold' : 'text-muted'}`}>
                  {step.label}
                </span>
                {index < steps.length - 1 && (
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

export default StadiumBuilderStepper;
import React, { useState } from 'react';
import { PaymentElement, useStripe, useElements } from '@stripe/react-stripe-js';

const PaymentForm = () => {
  const stripe = useStripe();
  const elements = useElements();
  const [error, setError] = useState(null);
  const [processing, setProcessing] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!stripe || !elements) {
      return;
    }

    setProcessing(true);
    setError(null);

    try {
      const { error: submitError } = await elements.submit();
      if (submitError) {
        setError(submitError.message);
        setProcessing(false);
        return;
      }

      const { error: confirmError } = await stripe.confirmPayment({
        elements,
        confirmParams: {
          return_url: `${window.location.origin}/payment/success`,
        }
      });

      if (confirmError) {
        setError(confirmError.message);
      }
    } catch (e) {
      setError('An unexpected error occurred.');
    } finally {
      setProcessing(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <PaymentElement />
      {error && (
        <div className="alert alert-danger mt-3">
          {error}
        </div>
      )}
      <button 
        type="submit" 
        className="btn btn-primary mt-3 w-100" 
        disabled={!stripe || processing}
      >
        {processing ? 'Processing...' : 'Pay Now'}
      </button>
    </form>
  );
};

export default PaymentForm;
import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import Layout from '../../components/layout/Layout';

const PaymentSuccessPage = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  useEffect(() => {
    const status = searchParams.get('redirect_status');
    
    if (status === 'succeeded') {
      setTimeout(() => {
        navigate('/'); 
      }, 3000);
    }
  }, [searchParams, navigate]);

  return (
    <Layout>
      <div className="container mt-5">
        <div className="card shadow-sm">
          <div className="card-body text-center">
            <h2 className="text-success mb-4">Payment Successful!</h2>
            <p>Your tickets have been purchased successfully.</p>
            <p>You will be redirected to the home page in a few seconds...</p>
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default PaymentSuccessPage;
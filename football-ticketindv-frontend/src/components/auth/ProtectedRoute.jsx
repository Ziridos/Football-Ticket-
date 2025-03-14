import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const ProtectedRoute = ({ children, adminRequired = false }) => {
  const { user, isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  if (adminRequired && user?.role !== 'ADMIN') {
    return <Navigate to="/" />;
  }

  return children;
};

export default ProtectedRoute;
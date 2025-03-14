import React from 'react';
import AppRouter from './router';
import './App.css';

function App() {
  return (
    <div className="w-100 min-vh-100 d-flex flex-column overflow-hidden">
      <AppRouter />
    </div>
  );
}

export default App;
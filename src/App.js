import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Home from './pages/Home';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import './App.css';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userRole, setUserRole] = useState('user');

  useEffect(() => {
    // Check if user is logged in (e.g., from localStorage or session)
    const token = localStorage.getItem('token');
    if (token) {
      setIsAuthenticated(true);
      // Fetch user role if needed
    }
  }, []);

  return (
    <Router>
      <div className="app">
        <Routes>
          <Route 
            path="/" 
            element={isAuthenticated ? <Home userRole={userRole} /> : <Navigate to="/login" />} 
          />
          <Route 
            path="/login" 
            element={<LoginPage setIsAuthenticated={setIsAuthenticated} setUserRole={setUserRole} />} 
          />
          <Route 
            path="/signup" 
            element={<SignupPage />} 
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
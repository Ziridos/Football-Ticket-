import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/auth/ProtectedRoute';
import LoginPage from './pages/LoginPage';
import MainPage from './pages/MainPage';
import Users from './pages/user/users';
import CreateUser from './pages/user/createuser';
import EditUser from './pages/user/EditUser';
import Competitions from './pages/competition/competitions';
import EditCompetition from './pages/competition/EditCompetition';
import CreateCompetition from './pages/competition/createCompetition';
import Stadiums from './pages/stadium/Stadiums';
import CreateStadium from './pages/stadium/CreateStadium';
import EditStadium from './pages/stadium/EditStadium';
import Clubs from './pages/club/clubs';
import CreateClub from './pages/club/CreateClub';
import EditClub from './pages/club/EditClub';
import Matches from './pages/match/matches';
import CreateMatch from './pages/match/CreateMatch';
import EditMatch from './pages/match/EditMatch';
import AdminBoxBuilderPage from './pages/box/AdminBoxBuilderPage';
import AdminBlockBuilderPage from './pages/stadium/AdminBlockBuilderPage';
import MatchSelectionPage from './pages/match/MatchSelectionPage';
import BoxSelectionPage from './pages/box/BoxSelectionPage';
import SeatSelectionPage from './pages/seat/SeatSelectionPage';
import BoxPricingRules from './pages/boxPricingRule/BoxPricingRules';
import CreateBoxPricingRule from './pages/boxPricingRule/CreateBoxPricingRule';
import EditBoxPricingRule from './pages/boxPricingRule/EditBoxPricingRule';
import MyTickets from './pages/ticket/MyTickets';
import TicketSalesStatistics from './pages/ticket/TicketSalesStatistics';
import PaymentSuccessPage from './pages/payment/PaymentSuccess.jsx';
import Profile from './pages/user/Profile.jsx';
import Register from './pages/user/Register.jsx';

const AppRouter = () => {
  return (
    <Router>
      <AuthProvider>
        <div className="w-100 min-vh-100 d-flex flex-column overflow-hidden">
          <Routes>
            {/* Public routes */}
            <Route path="/" element={<MainPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<Register />} />

            {/* Admin-only routes */}
            <Route path="/users" element={
              <ProtectedRoute adminRequired>
                <Users />
              </ProtectedRoute>
            } />
            <Route path="/create-user" element={
              <ProtectedRoute adminRequired>
                <CreateUser />
              </ProtectedRoute>
            } />
            <Route path="/edit-user/:id" element={
              <ProtectedRoute adminRequired>
                <EditUser />
              </ProtectedRoute>
            } />

            {/* Competition management - Admin only */}
            <Route path="/competitions" element={
              <ProtectedRoute adminRequired>
                <Competitions />
              </ProtectedRoute>
            } />
            <Route path="/create-competition" element={
              <ProtectedRoute adminRequired>
                <CreateCompetition />
              </ProtectedRoute>
            } />
            <Route path="/edit-competition/:id" element={
              <ProtectedRoute adminRequired>
                <EditCompetition />
              </ProtectedRoute>
            } />

            {/* Stadium management - Admin only */}
            <Route path="/stadiums" element={
              <ProtectedRoute adminRequired>
                <Stadiums />
              </ProtectedRoute>
            } />
            <Route path="/create-stadium" element={
              <ProtectedRoute adminRequired>
                <CreateStadium />
              </ProtectedRoute>
            } />
            <Route path="/edit-stadium/:id" element={
              <ProtectedRoute adminRequired>
                <EditStadium />
              </ProtectedRoute>
            } />

            {/* Club management - Admin only */}
            <Route path="/clubs" element={
              <ProtectedRoute adminRequired>
                <Clubs />
              </ProtectedRoute>
            } />
            <Route path="/create-club" element={
              <ProtectedRoute adminRequired>
                <CreateClub />
              </ProtectedRoute>
            } />
            <Route path="/edit-club/:id" element={
              <ProtectedRoute adminRequired>
                <EditClub />
              </ProtectedRoute>
            } />

            {/* Match management - Admin only */}
            <Route path="/matches" element={
              <ProtectedRoute adminRequired>
                <Matches />
              </ProtectedRoute>
            } />
            <Route path="/create-match" element={
              <ProtectedRoute adminRequired>
                <CreateMatch />
              </ProtectedRoute>
            } />
            <Route path="/edit-match/:id" element={
              <ProtectedRoute adminRequired>
                <EditMatch />
              </ProtectedRoute>
            } />

            {/* Box and Block management - Admin only */}
            <Route path="/admin-box-builder/:id" element={
              <ProtectedRoute adminRequired>
                <AdminBoxBuilderPage />
              </ProtectedRoute>
            } />
            <Route path="/admin-block-builder/:id" element={
              <ProtectedRoute adminRequired>
                <AdminBlockBuilderPage />
              </ProtectedRoute>
            } />

            {/* Box Pricing Rules - Admin only */}
            <Route path="/box-pricing-rules/:stadiumId" element={
              <ProtectedRoute adminRequired>
                <BoxPricingRules />
              </ProtectedRoute>
            } />
            <Route path="/create-box-pricing-rule/:stadiumId" element={
              <ProtectedRoute adminRequired>
                <CreateBoxPricingRule />
              </ProtectedRoute>
            } />
            <Route path="/edit-box-pricing-rule/:stadiumId/:ruleId" element={
              <ProtectedRoute adminRequired>
                <EditBoxPricingRule />
              </ProtectedRoute>
            } />


            {/* Admin only ticket sales */}
            <Route path="/tickets/statistics" element={
              <ProtectedRoute adminRequired>
                <TicketSalesStatistics />
              </ProtectedRoute>
            } />

            {/* User accessible routes - Requires authentication but not admin */}
            <Route path="/my-tickets" element={
              <ProtectedRoute>
                <MyTickets />
              </ProtectedRoute>
            } />
            <Route path="/ticket-purchase" element={
              <ProtectedRoute>
                <MatchSelectionPage />
              </ProtectedRoute>
            } />
            <Route path="/ticket-purchase/boxes/:matchId" element={
              <ProtectedRoute>
                <BoxSelectionPage />
              </ProtectedRoute>
            } />
            <Route path="/ticket-purchase/seats/:matchId/:boxId" element={
              <ProtectedRoute>
                <SeatSelectionPage />
              </ProtectedRoute>
            } />
            <Route path="/payment/success" element={
              <ProtectedRoute>
                <PaymentSuccessPage />
              </ProtectedRoute>
            } />
            <Route path="/profile/:id" element={
              <ProtectedRoute>
                <Profile/>
              </ProtectedRoute>
            } />
          </Routes>
        </div>
      </AuthProvider>
    </Router>
  );
};

export default AppRouter;
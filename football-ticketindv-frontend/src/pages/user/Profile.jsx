import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import UserForm from '../../components/user/UserForm';
import userApi from '../../services/userApi';
import { useAuth } from '../../context/AuthContext';
import Layout from '../../components/layout/Layout';

const Profile = () => {
  const [userData, setUserData] = useState(null);
  const [error, setError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const navigate = useNavigate();
  const { isAuthenticated, user } = useAuth();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    fetchUserProfile();
  }, [isAuthenticated, navigate]);

  const fetchUserProfile = async () => {
    try {
      setError(null);
      const profile = await userApi.getUserById(user.userId);
      setUserData(profile);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      setError(errorMessage || error.message);
      if (error.message.includes('Please login')) {
        navigate('/login');
      }
    }
  };

  const handleUpdate = async (updatedData) => {
    try {
      setError(null);
      await userApi.updateUser(user.userId, updatedData);
      setUserData(updatedData);
      setIsEditing(false);
    } catch (error) {

      const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

      setError(errorMessage || error.message);
      if (error.message.includes('Please login')) {
        navigate('/login');
      }
    }
  };

  const renderProfile = () => {
    if (!userData) return null;

    const fields = [
      { label: 'Name', value: userData.name },
      { label: 'Email', value: userData.email },
      { label: 'Phone', value: userData.phone },
      { label: 'Address', value: userData.address },
      { label: 'City', value: userData.city },
      { label: 'Country', value: userData.country },
      { label: 'Postal Code', value: userData.postalCode },
      { label: 'Role', value: userData.role }
    ];

    return (
      <div className="card shadow-sm">
        <div className="card-body">
          <div className="row">
            {fields.map(({ label, value }) => (
              <div className="col-md-6 mb-3" key={label}>
                <div className="d-flex flex-column">
                  <span className="text-muted small">{label}</span>
                  <span className="fw-semibold">{value}</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    );
  };

  return (
    <Layout>
      <div className="container-fluid px-4 py-4" style={{ maxHeight: 'calc(100vh - 56px)', overflowY: 'auto' }}>
        {error ? (
          <div className="alert alert-danger" role="alert">
            {error}
          </div>
        ) : (
          <>
            <div className="d-flex justify-content-between align-items-center mb-4">
              <h2 className="fs-2 fw-bold mb-0">My Profile</h2>
              <button
                className="btn btn-danger"
                onClick={() => setIsEditing(!isEditing)}
              >
                {isEditing ? 'Cancel Edit' : 'Edit Profile'}
              </button>
            </div>

            {isEditing ? (
              <div className="card shadow-sm">
                <div className="card-body">
                  <UserForm
                    onSubmit={handleUpdate}
                    initialData={userData}
                  />
                </div>
              </div>
            ) : (
              renderProfile()
            )}
          </>
        )}
      </div>
    </Layout>
  );
};

export default Profile;
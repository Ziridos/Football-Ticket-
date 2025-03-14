import React, { useState } from 'react';

const UserForm = ({ onSubmit, initialData = {}, showRoleSelect = true }) => {
  const [userData, setUserData] = useState({
    name: '',
    email: '',
    address: '',
    phone: '',
    country: '',
    city: '',
    postalCode: '',
    password: '',
    role: 'USER',
    ...initialData
  });

  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserData({
      ...userData,
      [name]: value,
    });
    setErrors({
      ...errors,
      [name]: '',
    });
  };

  const validateForm = () => {
    let newErrors = {};

    Object.entries(userData).forEach(([key, value]) => {
      if (key !== 'role') {
        if (typeof value === 'string' && !value.trim()) {
          newErrors[key] = `${key.charAt(0).toUpperCase() + key.slice(1)} is required`;
        }
      }
    });

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (userData.email && !emailRegex.test(userData.email)) {
      newErrors.email = 'Please enter a valid email address';
    }

    if (userData.phone && !/^\d{10}$/.test(userData.phone)) {
      newErrors.phone = 'Phone should be 10 digits';
    }

    if (userData.password.length < 8) {
      newErrors.password = 'Password should be at least 8 characters long';
    }

    if (!/^[A-Za-z\s]+$/.test(userData.name)) {
      newErrors.name = 'Name can only contain letters and spaces';
    }

    if (!/^[A-Za-z0-9\s]+$/.test(userData.address)) {
      newErrors.address = 'Address can only contain letters, numbers, and spaces';
    }

    if (!/^[A-Za-z\s-]+$/.test(userData.country)) {
      newErrors.country = 'Country can only contain letters, spaces, and dashes';
    }

    if (!/^[A-Za-z0-9\s]+$/.test(userData.postalCode)) {
      newErrors.postalCode = 'Postal code can only contain letters, numbers, and spaces';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateForm()) {
      onSubmit(userData);
    }
  };

  const requiredFields = ['name', 'email', 'address', 'phone', 'country', 'city', 'postalCode', 'password'];

  return (
    <form onSubmit={handleSubmit}>
      <div className="row g-4">
        {requiredFields.map((field) => (
          <div className="col-md-6" key={field}>
            <div className="form-group">
              <label htmlFor={field} className="form-label fw-semibold mb-2">
                {field.charAt(0).toUpperCase() + field.slice(1)}
              </label>
              <input
                type={field === 'password' ? 'password' : 'text'}
                id={field}
                name={field}
                className={`form-control form-control-lg ${errors[field] ? 'is-invalid' : ''}`}
                value={userData[field]}
                onChange={handleChange}
                placeholder={`Enter ${field.toLowerCase()}`}
                required
              />
              {errors[field] && (
                <div className="invalid-feedback">{errors[field]}</div>
              )}
            </div>
          </div>
        ))}

        {showRoleSelect && (
          <div className="col-12">
            <div className="form-group">
              <label htmlFor="role" className="form-label fw-semibold mb-2">
                Role
              </label>
              <select
                id="role"
                name="role"
                className="form-select form-select-lg"
                value={userData.role}
                onChange={handleChange}
              >
                <option value="USER">User</option>
                <option value="ADMIN">Admin</option>
              </select>
            </div>
          </div>
        )}

        <div className="col-12">
          <button type="submit" className="btn btn-danger btn-lg px-5">
            {initialData.id ? 'Update User' : 'Create User'}
          </button>
        </div>
      </div>
    </form>
  );
};

export default UserForm;
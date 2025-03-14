import React, { useState } from 'react';

const StadiumForm = ({ onSubmit, initialData = {} }) => {
  const [formData, setFormData] = useState({
    stadiumName: initialData.stadiumName || '',
    stadiumAddress: initialData.stadiumAddress || '',
    stadiumPostalCode: initialData.stadiumPostalCode || '',
    stadiumCity: initialData.stadiumCity || '',
    stadiumCountry: initialData.stadiumCountry || '',
  });

  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({
      ...prevData,
      [name]: value
    }));
    setErrors(prevErrors => ({
      ...prevErrors,
      [name]: ''
    }));
  };

  const validateForm = () => {
    const newErrors = {};
    
    if (!/^[A-Za-z\s]+$/.test(formData.stadiumName.trim())) {
      newErrors.stadiumName = 'Name can only contain letters and spaces';
    }
    
    if (!/^[A-Za-z0-9\s]+$/.test(formData.stadiumAddress.trim())) {
      newErrors.stadiumAddress = 'Address can only contain letters, numbers, and spaces';
    }
    
    if (!/^[A-Za-z\s-]+$/.test(formData.stadiumCity.trim())) {
      newErrors.stadiumCity = 'City can only contain letters, spaces, and dashes';
    }
    
    if (!/^[A-Za-z0-9\s-]+$/.test(formData.stadiumCountry.trim())) {
      newErrors.stadiumCountry = 'Country can only contain letters, spaces, numbers, and dashes';
    }

    Object.keys(formData).forEach(key => {
      if (!formData[key].trim()) {
        newErrors[key] = `${key.replace('stadium', '').replace(/([A-Z])/g, ' $1').trim()} is required`;
      }
    });

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateForm()) {
      onSubmit(formData).catch((err) => {
        setErrors(prevErrors => ({
          ...prevErrors,
          submit: err.message
        }));
      });
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      {Object.keys(formData).map((field) => (
        <div className="form-group" key={field}>
          <label>{field.replace('stadium', '').replace(/([A-Z])/g, ' $1').trim()}</label>
          <input
            type="text"
            name={field}
            className={`form-control ${errors[field] ? 'is-invalid' : ''}`}
            value={formData[field]}
            onChange={handleChange}
            required
          />
          {errors[field] && <div className="invalid-feedback">{errors[field]}</div>}
        </div>
      ))}
      {errors.submit && <div className="alert alert-danger">{errors.submit}</div>}
      <button type="submit" className="btn btn-primary">Save</button>
    </form>
  );
};

export default StadiumForm;
import React, { useState } from 'react';

const BoxPricingRuleForm = ({ onSubmit, initialData = {} }) => {
  const [formData, setFormData] = useState({
    occupancyThreshold: initialData.occupancyThreshold || '',
    priceIncreasePercentage: initialData.priceIncreasePercentage || '',
  });

  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({
      ...prevData,
      [name]: value,
    }));
    setErrors(prevErrors => ({
      ...prevErrors,
      [name]: ''
    }));
  };

  const validateForm = () => {
    const newErrors = {};
    if (formData.occupancyThreshold === '') {
      newErrors.occupancyThreshold = 'Occupancy Threshold is required';
    } else if (parseFloat(formData.occupancyThreshold) < 0 || parseFloat(formData.occupancyThreshold) > 100) {
      newErrors.occupancyThreshold = 'Occupancy Threshold must be between 0 and 100';
    }

    if (formData.priceIncreasePercentage === '') {
      newErrors.priceIncreasePercentage = 'Price Increase Percentage is required';
    } else if (parseFloat(formData.priceIncreasePercentage) < 0) {
      newErrors.priceIncreasePercentage = 'Price Increase Percentage must be non-negative';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateForm()) {
      const ruleData = {
        occupancyThreshold: parseFloat(formData.occupancyThreshold),
        priceIncreasePercentage: parseFloat(formData.priceIncreasePercentage),
      };
      onSubmit(ruleData).catch((err) => {
        setErrors(prevErrors => ({
          ...prevErrors,
          submit: err.message
        }));
      });
    }
  };

  return (
    <form onSubmit={handleSubmit} className="container mt-4">
      {errors.submit && <div className="alert alert-danger">{errors.submit}</div>}
      
      <div className="mb-3">
        <label htmlFor="occupancyThreshold" className="form-label">Occupancy Threshold</label>
        <input
          type="number"
          step="0.01"
          id="occupancyThreshold"
          name="occupancyThreshold"
          className={`form-control ${errors.occupancyThreshold ? 'is-invalid' : ''}`}
          value={formData.occupancyThreshold}
          onChange={handleChange}
          required
        />
        {errors.occupancyThreshold && <div className="invalid-feedback">{errors.occupancyThreshold}</div>}
      </div>

      <div className="mb-3">
        <label htmlFor="priceIncreasePercentage" className="form-label">Price Increase Percentage</label>
        <input
          type="number"
          step="0.01"
          id="priceIncreasePercentage"
          name="priceIncreasePercentage"
          className={`form-control ${errors.priceIncreasePercentage ? 'is-invalid' : ''}`}
          value={formData.priceIncreasePercentage}
          onChange={handleChange}
          required
        />
        {errors.priceIncreasePercentage && <div className="invalid-feedback">{errors.priceIncreasePercentage}</div>}
      </div>

      <button type="submit" className="btn btn-primary">Save</button>
    </form>
  );
};

export default BoxPricingRuleForm;
import React, { useState } from 'react';

const CompetitionForm = ({ onSubmit, initialData = {} }) => {
  const [formData, setFormData] = useState({
    competitionName: initialData.competitionName || '',
    competitionId: initialData.competitionId || ''
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
    if (!formData.competitionName.trim()) {
      newErrors.competitionName = 'Competition Name is required';
    }

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
    <form onSubmit={handleSubmit} className="container mt-4">
      {errors.submit && <div className="alert alert-danger">{errors.submit}</div>}
      
      <div className="mb-3">
        <label htmlFor="competitionName" className="form-label">Competition Name</label>
        <input
          type="text"
          id="competitionName"
          name="competitionName"
          className={`form-control ${errors.competitionName ? 'is-invalid' : ''}`}
          value={formData.competitionName}
          onChange={handleChange}
          placeholder="Competition Name"
          required
        />
        {errors.competitionName && <div className="invalid-feedback">{errors.competitionName}</div>}
      </div>

      <button type="submit" className="btn btn-primary">Submit</button>
    </form>
  );
};

export default CompetitionForm;
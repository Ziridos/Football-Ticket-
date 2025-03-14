import React, { useState } from 'react';

const MatchForm = ({ 
  onSubmit, 
  initialData = {}, 
  clubs = [], 
  competitions = [], 
  isLoadingClubs = false,
  isLoadingCompetitions = false,
  clubsError = null,
  competitionsError = null 
}) => {
  const [formData, setFormData] = useState({
    homeClubName: initialData.homeClubName || '',
    awayClubName: initialData.awayClubName || '',
    competitionName: initialData.competitionName || '',
    matchDateTime: initialData.matchDateTime || '',
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
    Object.keys(formData).forEach(key => {
      if (!formData[key].trim()) {
        newErrors[key] = `${key.replace(/([A-Z])/g, ' $1').trim()} is required`;
      }
    });

    if (formData.homeClubName === formData.awayClubName) {
      newErrors.awayClubName = 'Home club and away club cannot be the same';
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
      {clubsError && <div className="alert alert-danger">{clubsError}</div>}
      {competitionsError && <div className="alert alert-danger">{competitionsError}</div>}
      {errors.submit && <div className="alert alert-danger">{errors.submit}</div>}
      
      {['homeClubName', 'awayClubName'].map((field) => (
        <div className="mb-3" key={field}>
          <label htmlFor={field} className="form-label">
            {field.replace(/([A-Z])/g, ' $1').trim()}
          </label>
          <select
            id={field}
            name={field}
            className={`form-select ${errors[field] ? 'is-invalid' : ''}`}
            value={formData[field]}
            onChange={handleChange}
            required
            disabled={isLoadingClubs}
          >
            <option value="">Select {field.replace(/([A-Z])/g, ' $1').trim()}</option>
            {Array.isArray(clubs) && clubs.map(club => (
              <option key={club.clubId} value={club.clubName}>
                {club.clubName}
              </option>
            ))}
          </select>
          {errors[field] && <div className="invalid-feedback">{errors[field]}</div>}
        </div>
      ))}

      <div className="mb-3">
        <label htmlFor="competitionName" className="form-label">Competition</label>
        <select
          id="competitionName"
          name="competitionName"
          className={`form-select ${errors.competitionName ? 'is-invalid' : ''}`}
          value={formData.competitionName}
          onChange={handleChange}
          required
          disabled={isLoadingCompetitions}
        >
          <option value="">Select Competition</option>
          {Array.isArray(competitions) && competitions.map(competition => (
            <option key={competition.competitionId} value={competition.competitionName}>
              {competition.competitionName}
            </option>
          ))}
        </select>
        {errors.competitionName && <div className="invalid-feedback">{errors.competitionName}</div>}
      </div>

      <div className="mb-3">
        <label htmlFor="matchDateTime" className="form-label">Match Date and Time</label>
        <input
          type="datetime-local"
          id="matchDateTime"
          name="matchDateTime"
          className={`form-control ${errors.matchDateTime ? 'is-invalid' : ''}`}
          value={formData.matchDateTime}
          onChange={handleChange}
          required
        />
        {errors.matchDateTime && <div className="invalid-feedback">{errors.matchDateTime}</div>}
      </div>

      <button type="submit" className="btn btn-primary">Submit</button>
    </form>
  );
};

export default MatchForm;
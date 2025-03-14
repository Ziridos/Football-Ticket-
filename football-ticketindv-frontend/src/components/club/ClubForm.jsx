import React, { useState } from 'react';

const ClubForm = ({ onSubmit, initialData = {}, stadiums = [], isLoadingStadiums = false, stadiumError = null }) => {
  const [formData, setFormData] = useState({
    clubName: initialData.clubName || '',
    clubId: initialData.clubId || '',
    stadiumId: initialData.stadiumId || '',
  });
  const [logoFile, setLogoFile] = useState(null);
  const [logoPreview, setLogoPreview] = useState(
    initialData.logo ? `data:${initialData.logoContentType};base64,${initialData.logo}` : null
  );
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

  const handleLogoChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      if (file.size > 10 * 1024 * 1024) { // 10MB limit
        setErrors(prev => ({
          ...prev,
          logo: 'File size must be less than 10MB'
        }));
        return;
      }
      setLogoFile(file);
      const reader = new FileReader();
      reader.onloadend = () => {
        setLogoPreview(reader.result);
      };
      reader.readAsDataURL(file);
      setErrors(prev => ({ ...prev, logo: '' }));
    }
  };

  const validateForm = () => {
    const newErrors = {};
    if (!formData.clubName.trim()) {
      newErrors.clubName = 'Club Name is required';
    }
    if (!formData.stadiumId) {
      newErrors.stadiumId = 'Stadium selection is required';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateForm()) {
      const data = { ...formData };
      if (logoFile) {
        data.logoFile = logoFile;
      }
      onSubmit(data).catch((err) => {
        setErrors(prevErrors => ({
          ...prevErrors,
          submit: err.message
        }));
      });
    }
  };

  return (
    <form onSubmit={handleSubmit} className="container mt-4">
      {stadiumError && <div className="alert alert-danger">{stadiumError}</div>}
      {errors.submit && <div className="alert alert-danger">{errors.submit}</div>}
      
      <div className="mb-3">
        <label htmlFor="clubName" className="form-label">Club Name</label>
        <input
          type="text"
          id="clubName"
          name="clubName"
          className={`form-control ${errors.clubName ? 'is-invalid' : ''}`}
          value={formData.clubName}
          onChange={handleChange}
          placeholder="Club Name"
          required
        />
        {errors.clubName && <div className="invalid-feedback">{errors.clubName}</div>}
      </div>

      <div className="mb-3">
        <label htmlFor="stadiumId" className="form-label">Select Stadium</label>
        <select
          id="stadiumId"
          name="stadiumId"
          className={`form-select ${errors.stadiumId ? 'is-invalid' : ''}`}
          value={formData.stadiumId}
          onChange={handleChange}
          required
          disabled={isLoadingStadiums}
        >
          <option value="">Select a stadium</option>
          {stadiums.map((stadium) => (
            <option key={stadium.stadiumId} value={stadium.stadiumId}>
              {stadium.stadiumName} ({stadium.stadiumCity}, {stadium.stadiumCountry})
            </option>
          ))}
        </select>
        {errors.stadiumId && <div className="invalid-feedback">{errors.stadiumId}</div>}
      </div>

      <div className="mb-3">
        <label htmlFor="logo" className="form-label">Club Logo</label>
        <input
          type="file"
          id="logo"
          className={`form-control ${errors.logo ? 'is-invalid' : ''}`}
          onChange={handleLogoChange}
          accept="image/*"
        />
        {errors.logo && <div className="invalid-feedback">{errors.logo}</div>}
        
        {logoPreview && (
          <div className="mt-2">
            <img 
              src={logoPreview} 
              alt="Logo preview" 
              style={{ maxWidth: '200px', maxHeight: '200px' }} 
              className="img-thumbnail"
            />
          </div>
        )}
      </div>

      <button type="submit" className="btn btn-primary">Submit</button>
    </form>
  );
};

export default ClubForm;
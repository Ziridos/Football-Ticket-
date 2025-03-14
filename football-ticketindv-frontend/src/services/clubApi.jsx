import axios from 'axios';
import { createApiInstance } from './apiConfig';

const BASE_URL = 'http://localhost:8080';

const api = createApiInstance();

const clubApi = {
  getAllClubs: async (filters = {}) => {
    try {
      const queryParams = new URLSearchParams();
      
      // Handle string filters
      if (filters.name) queryParams.append('name', filters.name.trim());
      if (filters.stadiumName) queryParams.append('stadiumName', filters.stadiumName.trim());
      if (filters.stadiumCity) queryParams.append('stadiumCity', filters.stadiumCity.trim());
      if (filters.stadiumCountry) queryParams.append('stadiumCountry', filters.stadiumCountry.trim());

      // Handle pagination parameters
      queryParams.append('page', filters.page || 0);
      queryParams.append('size', filters.size || 10);
      queryParams.append('sortBy', filters.sortBy || 'id');  // Changed from 'clubId' to 'id'
      queryParams.append('sortDirection', filters.sortDirection || 'ASC');

      const url = `/clubs?${queryParams.toString()}`;
      
      console.log('Making request to:', url);
      console.log('With filters:', filters);

      const response = await api.get(url);
      console.log('Response received:', response.data);
      
      return {
        clubs: response.data.clubs || [],
        totalElements: response.data.totalElements,
        totalPages: response.data.totalPages,
        currentPage: response.data.currentPage
      };
    } catch (error) {
      console.error('Error fetching clubs:', error);
      throw error;
    }
},

getAllClubsList: async () => {
  try {
    const response = await api.get('/clubs?page=0&size=1000&sortBy=id&sortDirection=ASC');
    return response.data.clubs || [];
  } catch (error) {
    console.error('Error fetching all clubs:', error);
    throw error;
  }
},

  getClubById: async (clubId) => {
    try {
      const response = await api.get(`/clubs/${clubId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching club:', error);
      throw error;
    }
  },

  createClub: async (clubData) => {
    try {
      const response = await api.post('/clubs', clubData);
      return response.data;
    } catch (error) {
      console.error('Error creating club:', error);
      throw error;
    }
  },

  updateClub: async (clubId, clubData) => {
    try {
      const response = await api.put(`/clubs/${clubId}`, clubData);
      return response.data;
    } catch (error) {
      console.error('Error updating club:', error);
      throw error;
    }
  },

  deleteClub: async (clubId) => {
    try {
      await api.delete(`/clubs/${clubId}`);
    } catch (error) {
      console.error('Error deleting club:', error);
      throw error;
    }
  },

  uploadLogo: async (clubId, file) => {
    try {
      const formData = new FormData();
      formData.append('file', file);
      
      const response = await api.post(`/clubs/${clubId}/logo`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      return response.data;
    } catch (error) {
      console.error('Error uploading logo:', error);
      throw error;
    }
  },

  deleteLogo: async (clubId) => {
    try {
      await api.delete(`/clubs/${clubId}/logo`);
    } catch (error) {
      console.error('Error deleting logo:', error);
      throw error;
    }
  },
};

export default clubApi;
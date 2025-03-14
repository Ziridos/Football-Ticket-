import axios from 'axios';
import { createApiInstance } from './apiConfig';

const BASE_URL = 'http://localhost:8080';

const api = createApiInstance();

const competitionApi = {
  getAllCompetitions: async (filters = {}) => {
    try {
      const queryParams = new URLSearchParams();
      if (filters.name) queryParams.append('name', filters.name);
      queryParams.append('page', filters.page || 0);
      queryParams.append('size', filters.size || 10);
      queryParams.append('sortBy', filters.sortBy || 'id');
      queryParams.append('sortDirection', filters.sortDirection || 'ASC');

      const url = `/competitions?${queryParams.toString()}`;
      
      const response = await api.get(url);
      return {
        competitions: response.data.competitions || [],
        totalElements: response.data.totalElements,
        totalPages: response.data.totalPages,
        currentPage: response.data.currentPage
      };
    } catch (error) {
      console.error('Error fetching competitions:', error);
      throw error;
    }
  },

  getAllCompetitionsList: async () => {
    try {
      const response = await api.get('/competitions?page=0&size=1000&sortBy=id&sortDirection=ASC');
      return response.data.competitions || [];
    } catch (error) {
      console.error('Error fetching all competitions:', error);
      throw error;
    }
  },


  getCompetitionById: async (competitionId) => {
    try {
      const response = await api.get(`/competitions/${competitionId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching competition:', error);
      throw error;
    }
  },

  createCompetition: async (competitionData) => {
    try {
      const response = await api.post('/competitions', competitionData);
      return response.data;
    } catch (error) {
      console.error('Error creating competition:', error);
      throw error;
    }
  },

  updateCompetition: async (competitionId, competitionData) => {
    try {
      const response = await api.put(`/competitions/${competitionId}`, competitionData);
      return response.data;
    } catch (error) {
      console.error('Error updating competition:', error);
      throw error;
    }
  },

  deleteCompetition: async (competitionId) => {
    try {
      await api.delete(`/competitions/${competitionId}`);
    } catch (error) {
      console.error('Error deleting competition:', error);
      throw error;
    }
  },
};

export default competitionApi;
import axios from 'axios';
import { createApiInstance } from './apiConfig';

const BASE_URL = 'http://localhost:8080';

const api = createApiInstance();

const matchApi = {
  getAllMatches: async (filters = {}) => {
    try {
      const queryParams = new URLSearchParams();
      if (filters.homeClubName) queryParams.append('homeClubName', filters.homeClubName);
      if (filters.awayClubName) queryParams.append('awayClubName', filters.awayClubName);
      if (filters.competitionName) queryParams.append('competitionName', filters.competitionName);
      if (filters.date) queryParams.append('date', filters.date);
      
      // Add pagination parameters
      queryParams.append('page', filters.page || 0);
      queryParams.append('size', filters.size || 10);
      queryParams.append('sortBy', 'matchDateTime');
      queryParams.append('sortDirection', 'DESC');

      const url = `/matches?${queryParams.toString()}`;
      
      const response = await api.get(url);
      return {
        matches: response.data.matches || [],
        totalElements: response.data.totalElements,
        totalPages: response.data.totalPages,
        currentPage: response.data.currentPage
      };
    } catch (error) {
      console.error('Error fetching matches:', error);
      throw error;
    }
},

  getMatchById: async (matchId) => {
    try {
      const response = await api.get(`/matches/${matchId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching match:', error);
      throw error;
    }
  },

  createMatch: async (matchData) => {
    try {
      const response = await api.post('/matches', matchData);
      return response.data;
    } catch (error) {
      console.error('Error creating match:', error);
      throw error;
    }
  },

  updateMatch: async (matchId, matchData) => {
    try {
      const response = await api.put(`/matches/${matchId}`, matchData);
      return response.data;
    } catch (error) {
      console.error('Error updating match:', error);
      throw error;
    }
  },

  deleteMatch: async (matchId) => {
    try {
      await api.delete(`/matches/${matchId}`);
    } catch (error) {
      console.error('Error deleting match:', error);
      throw error;
    }
  },
};

export default matchApi;
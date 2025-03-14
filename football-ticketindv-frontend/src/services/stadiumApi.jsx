import axios from 'axios';
import { createApiInstance } from './apiConfig';

const BASE_URL = 'http://localhost:8080';

const api = createApiInstance();

const stadiumApi = {
  getAllStadiums: async (filters = {}) => {
    try {
      const queryParams = new URLSearchParams();
      if (filters.name) queryParams.append('name', filters.name);
      if (filters.city) queryParams.append('city', filters.city);
      if (filters.country) queryParams.append('country', filters.country);
      queryParams.append('page', filters.page || 0);
      queryParams.append('size', filters.size || 10);
      queryParams.append('sortBy', filters.sortBy || 'id');
      queryParams.append('sortDirection', filters.sortDirection || 'ASC');

      const url = `/stadiums?${queryParams.toString()}`;
      
      const response = await api.get(url);
      return {
        stadiums: response.data.stadiums || [],
        totalElements: response.data.totalElements,
        totalPages: response.data.totalPages,
        currentPage: response.data.currentPage
      };
    } catch (error) {
      console.error('Error fetching stadiums:', error);
      throw error;
    }
},

  getStadiumById: async (stadiumId) => {
    try {
      const response = await api.get(`/stadiums/${stadiumId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching stadium:', error);
      throw error;
    }
  },

  createStadium: async (stadiumData) => {
    try {
      const response = await api.post('/stadiums', stadiumData);
      return response.data;
    } catch (error) {
      console.error('Error creating stadium:', error);
      throw error;
    }
  },

  updateStadium: async (stadiumId, stadiumData) => {
    try {
      const response = await api.put(`/stadiums/${stadiumId}`, stadiumData);
      return response.data;
    } catch (error) {
      console.error('Error updating stadium:', error);
      throw error;
    }
  },

  deleteStadium: async (stadiumId) => {
    try {
      await api.delete(`/stadiums/${stadiumId}`);
    } catch (error) {
      console.error('Error deleting stadium:', error);
      throw error;
    }
  },

  getAllStadiumsList: async () => {
    try {
      
      const response = await api.get('/stadiums?page=0&size=1000&sortBy=id&sortDirection=ASC');
      return response.data.stadiums || [];
    } catch (error) {
      console.error('Error fetching all stadiums:', error);
      throw error;
    }
  },

  getBoxBlocks: async (stadiumId, boxId) => {
    try {
      const response = await api.get(`/stadiums/${stadiumId}/boxes/${boxId}/blocks`);
      return response.data.map(block => ({
        ...block,
        x: block.xposition || 0,
        y: block.yposition || 0,
        xPosition: block.xposition || 0,
        yPosition: block.yposition || 0,
        seats: (block.seats || []).map(seat => ({
          ...seat,
          x: seat.xposition || 0,
          y: seat.yposition || 0
        }))
      }));
    } catch (error) {
      console.error('Error fetching box blocks:', error);
      throw error;
    }
  },

  saveBlock: async (stadiumId, boxId, blockData) => {
    try {
      const response = await api.post(`/stadiums/${stadiumId}/boxes/${boxId}/blocks`, blockData);
      return response.data;
    } catch (error) {
      console.error('Error saving block:', error);
      throw error;
    }
  },

  deleteBlock: async (stadiumId, boxId, blockId) => {
    try {
      await api.delete(`/stadiums/${stadiumId}/boxes/${boxId}/blocks/${blockId}`);
      return true; 
    } catch (error) {
      console.error('Error deleting block:', error);
      throw error;
    }
  },

};

export default stadiumApi;
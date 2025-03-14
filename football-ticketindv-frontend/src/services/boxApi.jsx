import axios from 'axios';
import { createApiInstance } from './apiConfig';

const BASE_URL = 'http://localhost:8080';

const api = createApiInstance();

const boxApi = {
  getBoxesByStadium: async (stadiumId) => {
    try {
      const response = await api.get(`/stadiums/${stadiumId}/boxes`);
      return response.data.map(box => ({
        ...box,
        x: box.xposition || 0,
        y: box.yposition || 0,
        width: box.width || 100,
        height: box.height || 100,
        price: box.price || 0,
      }));
    } catch (error) {
      console.error('Error fetching boxes:', error);
      throw error;
    }
  },

  createBox: async (stadiumId, boxData) => {
    try {
      const response = await api.post(`/stadiums/${stadiumId}/boxes`, boxData);
      return response.data;
    } catch (error) {
      console.error('Error creating box:', error);
      throw error;
    }
  },

  updateBoxPrice: async (stadiumId, boxId, newPrice) => {
    try {
      const response = await api.patch(`/stadiums/${stadiumId}/boxes/${boxId}/price`, { boxId, newPrice });
      return response.data;
    } catch (error) {
      console.error('Error updating box price:', error);
      throw error;
    }
  },

  deleteBox: async (stadiumId, boxId) => {
    try {
      await api.delete(`/stadiums/${stadiumId}/boxes/${boxId}`);
      return true; 
    } catch (error) {
      console.error('Error deleting box:', error);
      throw error;
    }
  },



};

export default boxApi;

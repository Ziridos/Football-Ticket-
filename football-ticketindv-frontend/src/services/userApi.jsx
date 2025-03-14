import axios from 'axios';
import { createApiInstance } from './apiConfig';

const BASE_URL = 'http://localhost:8080';

const api = createApiInstance();

const userApi = {
  getAllUsers: async (filters = {}) => {
    try {
      const queryParams = new URLSearchParams();
      if (filters.name) queryParams.append('name', filters.name);
      if (filters.email) queryParams.append('email', filters.email);
      if (filters.role) queryParams.append('role', filters.role);
      queryParams.append('page', filters.page || 0);
      queryParams.append('size', filters.size || 10);
      queryParams.append('sortBy', filters.sortBy || 'id');
      queryParams.append('sortDirection', filters.sortDirection || 'ASC');

      const queryString = queryParams.toString();
      const url = `/users?${queryString}`;
      
      const response = await api.get(url);
      return response.data;
    } catch (error) {
      console.error('Error fetching users:', error);
      throw error;
    }
  },

  getUserById: async (userId) => {
    try {
      const response = await api.get(`/users/${userId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching user:', error);
      throw error;
    }
  },

  createUser: async (userData) => {
    try {
      const response = await api.post('/users', userData);
      return response.data;
    } catch (error) {
      console.error('Error creating user:', error);
      throw error;
    }
  },

  register: async (userData) => {
    try {
      const response = await axios.post(`${BASE_URL}/users/register`, userData, {
        headers: {
          'Content-Type': 'application/json',
        }
      });
      return response.data;
    } catch (error) {
      console.error('Error during registration:', error);
      throw error;
    }
  },

  updateUser: async (userId, userData) => {
    try {
      const response = await api.put(`/users/${userId}`, userData);
      return response.data;
    } catch (error) {
      console.error('Error updating user:', error);
      throw error;
    }
  },

  deleteUser: async (userId) => {
    try {
      await api.delete(`/users/${userId}`);
    } catch (error) {
      console.error('Error deleting user:', error);
      throw error;
    }
  },
};

export default userApi;
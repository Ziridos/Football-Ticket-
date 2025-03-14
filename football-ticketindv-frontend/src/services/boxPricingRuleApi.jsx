import axios from 'axios';
import { createApiInstance } from './apiConfig';

const BASE_URL = 'http://localhost:8080';

const api = createApiInstance();

const boxPricingRuleApi = {
  getRulesByStadium: async (stadiumId) => {
    try {
      const response = await api.get(`/box-pricing-rules/stadium/${stadiumId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching rules:', error);
      throw error;
    }
  },

  getRuleById: async (ruleId) => {
    try {
      const response = await api.get(`/box-pricing-rules/${ruleId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching rule:', error);
      throw error;
    }
  },

  createRule: async (ruleData) => {
    try {
      const response = await api.post('/box-pricing-rules', ruleData);
      return response.data;
    } catch (error) {
      console.error('Error creating rule:', error);
      throw error;
    }
  },

  updateRule: async (ruleId, ruleData) => {
    try {
      const response = await api.put(`/box-pricing-rules/${ruleId}`, ruleData);
      return response.data;
    } catch (error) {
      console.error('Error updating rule:', error);
      throw error;
    }
  },

  deleteRule: async (ruleId) => {
    try {
      await api.delete(`/box-pricing-rules/${ruleId}`);
    } catch (error) {
      console.error('Error deleting rule:', error);
      throw error;
    }
  },
};

export default boxPricingRuleApi;
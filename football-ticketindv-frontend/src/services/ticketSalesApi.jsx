import { createApiInstance } from './apiConfig';

const api = createApiInstance();

const ticketSalesApi = {
  getTicketSalesStatistics: async (startDate, endDate) => {
    try {
      const response = await api.get(`/tickets/statistics?startDate=${startDate}&endDate=${endDate}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching ticket sales statistics:', error);
      throw error;
    }
  },

  getQuarterlyStatistics: async (year, quarter) => {
    try {
      const response = await api.get(`/tickets/statistics/quarterly?year=${year}&quarter=${quarter}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching quarterly statistics:', error);
      throw error;
    }
  },

  getMonthlyStatistics: async (year, month) => {
    try {
      const response = await api.get(`/tickets/statistics/monthly?year=${year}&month=${month}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching monthly statistics:', error);
      throw error;
    }
  }
};

export default ticketSalesApi;
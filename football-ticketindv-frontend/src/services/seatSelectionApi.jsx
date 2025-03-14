import axios from 'axios';
import { createApiInstance } from './apiConfig';

const api = createApiInstance();

const seatSelectionApi = {
  getBoxesByMatch: async (stadiumId, matchId) => {
    try {
      const response = await api.get(`/stadiums/${stadiumId}/boxes/match/${matchId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching boxes:', error);
      throw error;
    }
  },

  getBlocksByBoxAndMatch: async (stadiumId, boxId, matchId) => {
    try {
      const response = await api.get(`/stadiums/${stadiumId}/boxes/${boxId}/blocks/match/${matchId}`);
      return response.data.map(block => ({
        ...block,
        x: block.xposition || 0,
        y: block.yposition || 0,
        xPosition: block.xposition || 0,
        yPosition: block.yposition || 0,
        seats: (block.seats || []).map(seat => ({
          ...seat,
          x: seat.xposition || 0,
          y: seat.yposition || 0,
          xPosition: seat.xposition || 0,
          yPosition: seat.yposition || 0
        }))
      }));
    } catch (error) {
      console.error('Error fetching blocks:', error);
      throw error;
    }
  },

  getSeatAvailability: async (matchId) => {
    try {
      const response = await api.get(`/matches/${matchId}/seat-availability`);
      const availabilityMap = {};
      response.data.forEach(seat => {
        availabilityMap[seat.seatId] = seat.isAvailable;
      });
      return availabilityMap;
    } catch (error) {
      console.error('Error fetching seat availability:', error);
      throw error;
    }
  },


  //newest
  getSelectedSeats: async (matchId) => {
    try {
        const response = await api.get(`/matches/${matchId}/selected-seats`);
        return response.data;
    } catch (error) {
        console.error('Error fetching selected seats:', error);
        throw error;
    }
  },

  purchaseTickets: async (ticketRequest) => {
    try {
      const response = await api.post(`/tickets`, ticketRequest);
      return response.data;
    } catch (error) {
      console.error('Error purchasing tickets:', error);
      throw error;
    }
  }
};

export default seatSelectionApi;
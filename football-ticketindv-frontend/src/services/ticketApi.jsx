import { createApiInstance } from './apiConfig';

const api = createApiInstance();



const ticketApi = {
  getMatches: async () => {
    try {
      const response = await api.get('/matches');
      return response.data.matches || [];
    } catch (error) {
      console.error('Error fetching matches:', error);
      throw error;
    }
  },

  getStadiumLayout: async (stadiumId) => {
    try {
      const response = await api.get(`/stadiums/${stadiumId}/boxes`);
      return response.data.map(box => ({
        ...box,
        x: box.xposition || 0,
        y: box.yposition || 0,
        width: box.width || 100,
        height: box.height || 100,
      }));
    } catch (error) {
      console.error('Error fetching stadium layout:', error);
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
        seats: (block.seats || []).map(seat => ({
          ...seat,
          xposition: seat.xposition || 0,
          yposition: seat.yposition || 0
        }))
      }));
    } catch (error) {
      console.error('Error fetching box blocks:', error);
      throw error;
    }
  },

  purchaseTicket: async (ticketRequest) => {
    try {
      const response = await api.post('/tickets', ticketRequest);
      return response.data;
    } catch (error) {
      console.error('Error purchasing ticket:', error);
      throw error;
    }
  },

  createPaymentIntent: async (data) => {
    try {
      console.log('Sending payment intent request:', data);
    
      const response = await api.post('/api/payments/create-payment-intent', data);
      console.log('Payment intent response:', response);
      return response.data;
    } catch (error) {
      console.error('Error creating payment intent:', error);
      throw error;
    }
},

getUserTickets: async (userId, filters = {}) => {
  try {
    const queryParams = new URLSearchParams();
    if (filters.year) queryParams.append('year', filters.year);
    if (filters.quarter) queryParams.append('quarter', filters.quarter);
    queryParams.append('page', filters.page || 0);
    queryParams.append('size', filters.size || 10);
    queryParams.append('sortBy', filters.sortBy || 'purchaseDateTime');
    queryParams.append('sortDirection', filters.sortDirection || 'DESC');

    const response = await api.get(`/tickets/user/${userId}?${queryParams.toString()}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching user tickets:', error);
    if (error.response?.status === 401) {
      throw new Error('Please login to continue');
    }
    throw error;
  }
},
};

export default ticketApi;
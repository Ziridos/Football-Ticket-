import axios from 'axios';

const setupInterceptors = (navigate) => {
  axios.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response?.status === 401) {
        navigate('/login');
      }
      return Promise.reject(error);
    }
  );
};

export default setupInterceptors;
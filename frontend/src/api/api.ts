import axios from 'axios';

const baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080';
console.log('API Base URL:', baseURL);

const api = axios.create({
  baseURL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json'
  }
});

api.interceptors.request.use(request => {
  const token = sessionStorage.getItem("access_token");

  if(token) request.headers.Authorization = `Bearer ${token}`;

  return request;
});

api.interceptors.response.use(
  response => {
    //console.log('Response:', response);
    return response;
  },
  error => {
    console.error('Response Error:', {
      status: error.response?.status,
      data: error.response?.data,
      config: error.config
    });
    if ([401, 403].includes(error.response?.status)) {
      sessionStorage.removeItem('access_token');
      window.location.href = '/signin';
    }
    return Promise.reject(error);
  }
);

export default api;
import api from "./api";

interface ServiceType {
  id: string;
  userId: string;
  name: string;
  description: string;
  iconUrl: string;
}

const serviceApi = {
  async findServices() {
    try {
      //const token = sessionStorage.getItem("access_token");
      const token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0ZUBleGFtcGxlLmNvbSIsInVzZXJJZCI6IjY3ODkxY2ViMDMwNDkwMmZmZGE1NGU3MCIsImlhdCI6MTczNzAzOTk0OSwiZXhwIjoxNzM3MTI2MzQ5fQ.6eAct0F0A4P_rivHTD-TVQUzyso--JiShM31P05Ro1IOUtXvJ3GGxvsCXtSpSKEMDUHfizCtvtLA3zSPv7FQ1A";

      const response = await api.get("/services", { headers: { 'Authorization': `Bearer ${token}` }});
      return response.data;
    } catch (error: any) {
      return error.message;
    }
  },
  
  getFullIconUrl(iconUrl: string) {
    try{
      //const token = sessionStorage.getItem("access_token");
      const baseURL = import.meta.env.VITE_API_URL;

      return baseURL+`/services/download-icon/${iconUrl}`;
    }catch(error: any) {
      return error.message;
    }
  }
};

export default serviceApi;
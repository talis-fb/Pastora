import api from "./api";

interface ServiceType {
  id: string;
  userId: string;
  name: string;
  description: string;
  iconUrl: string;
}

interface CreateServiceDto {
  name: string;
  icon: File;
}

const serviceApi = {
  async findServices() {
    try {
      const response = await api.get("/services");
      return response.data;
    } catch (error: any) {
      return error.message;
    }
  },

  async findServiceById(serviceId:string) {
    try {
      const response = await api.get("/services/"+serviceId);
      return response.data;
    } catch (error: any) {
      return error.message;
    }    
  },
  async createService(data: CreateServiceDto) {
    const formData = new FormData();
    formData.append('name', data.name);
    formData.append('icon', data.icon);
    
    try {
      const response = await api.post("/services", formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      return response.data;
    } catch (error: any) {
      throw error;
    }
  },
  
  getFullIconUrl(iconUrl: string) {
    try{
      const baseURL = import.meta.env.VITE_API_URL;
      return baseURL+`/services/download-icon/${iconUrl}`;
    }catch(error: any) {
      return error.message;
    }
  }
};

export default serviceApi;
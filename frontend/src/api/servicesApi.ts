import api from "./api";

interface ServiceType {
  id: string;
  userId: string;
  name: string;
  description: string;
  iconUrl: string;
}
// TODO: REMOVER DEPOIS
const findServicesMock: Array<ServiceType> = [
  {
    id: "1231231231",
    userId: "1",
    name: "Rails API",
    description: "Description Rails API",
    iconUrl: ""
  },
  {
    id: "323231231231",
    userId: "1",
    name: "Apache Server",
    description: "Description Apache Service",
    iconUrl: ""    
  },
  {
    id: "323231231231",
    userId: "1",
    name: "Apache Server",
    description: "Description Apache Service",
    iconUrl: ""
  }  
]

const serviceApi = {
  async findServices() {
    try {
      //const token = sessionStorage.getItem("access_token");
      const token = "TOKEN";

      //const response = await api.get("/services", { headers: { 'Authorization': `Bearer ${token}` } });
      //return response.data;
      return findServicesMock;
    } catch (error: any) {
      return error.message;
    }
  } 
};

export default serviceApi;
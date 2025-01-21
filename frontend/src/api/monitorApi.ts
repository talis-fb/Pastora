import api from './api';

interface MonitorValidation {
  field: 'status' | 'body';
  operation: 'equals' | 'not_equals' | 'contains';
  value: string;
}

interface MonitorHttpDefinition {
  url: string;
  method: string;
  headers: Record<string, string>;
  body?: string;
}

interface CreateMonitorDto {
  enabled: boolean;
  name: string;
  description: string;
  userId?: string;
  serviceId: string;
  intervalRate: number;
  onSuccess: string[];
  onFail: string[];
  saveSuccessWhen: string;
  http: MonitorHttpDefinition;
  validations: MonitorValidation[];
}

const monitorApi = {
  async createMonitor(data: CreateMonitorDto) {
    try {
      const response = await api.post('/monitors/http', data);
      return response.data;
    } catch (error: any) {
      throw error;
    }
  },

  async getMonitors() {
    try {
      const response = await api.get('/monitors/http');
      return response.data;
    } catch (error: any) {
      throw error;
    }
  },

  async getMonitorsByService(serviceId: string) {
    try {
      const response = await api.get(`/monitors/service/${serviceId}`);
      return response.data;
    } catch (error: any) {
      throw error;
    }
  }
};

export default monitorApi;
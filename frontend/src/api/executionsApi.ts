import api from "./api";

const executionsApi = {
  async findExecutionsByMonitorId(monitorId: string) {
    try {
      const response = await api.get("/executions/monitor/"+monitorId);
      return response.data;
    } catch (error: any) {
      return error.message;
    }
  }
};

export default executionsApi;
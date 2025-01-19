import api from "./api";

interface SignInDto {
  email: string;
  password: string;
}

interface SignUpDto {
  name: string;
  email: string;
  password: string;
}

export const authApi = {
  async signin(credentials: SignInDto): Promise<string> {
    console.log('Sending signin request to:', `${api.defaults.baseURL}/auth/signin`);
    const response = await api.post("/auth/signin", credentials);
    return response.data;
  },

  async signup(credentials: SignUpDto): Promise<string> {
    console.log('Sending signup request to:', `${api.defaults.baseURL}/auth/signup`);
    const response = await api.post("/auth/signup", credentials);
    return response.data;
  },
};
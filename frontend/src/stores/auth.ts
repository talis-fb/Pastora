import { defineStore } from 'pinia';
import { ref } from 'vue';
import { authApi } from '@/api/authApi';
import router from '@/router';

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(sessionStorage.getItem('access_token'));
  const loading = ref(false);
  const error = ref<string | null>(null);

  async function signin(email: string, password: string) {
    try {
      loading.value = true;
      error.value = null;
      const response = await authApi.signin({ email, password });
      
      if (response && typeof response === 'string') {
        token.value = response;
        sessionStorage.setItem('access_token', response);
        router.push('/services');
      } else {
        throw new Error('Invalid token received');
      }
    } catch (e: any) {
      error.value = e.response?.data?.message || e.message || 'Failed to sign in';
      throw error.value;
    } finally {
      loading.value = false;
    }
  }

  async function signup(name: string, email: string, password: string) {
    try {
      loading.value = true;
      error.value = null;
      const response = await authApi.signup({ name, email, password });
      
      if (response && typeof response === 'string') {
        token.value = response;
        sessionStorage.setItem('access_token', response);
        router.push('/services');
      } else {
        throw new Error('Invalid token received');
      }
    } catch (e: any) {
      error.value = e.response?.data?.message || e.message || 'Failed to sign up';
      throw error.value;
    } finally {
      loading.value = false;
    }
  }

  function logout() {
    token.value = null;
    sessionStorage.removeItem('access_token');
    router.push('/signin');
  }

  return { token, loading, error, signin, signup, logout };
});
<template>
    <div class="min-h-screen bg-[#020817] flex items-center justify-center px-4">
      <div class="max-w-md w-full space-y-8 bg-[#1E293B] p-8 rounded-lg">
        <div class="text-center">
          <img src="@/assets/logo.png" alt="Logo" class="h-14 w-auto mx-auto">
          <h2 class="mt-6 text-3xl font-bold text-white">Acesse sua conta</h2>
        </div>
        <form class="mt-8 space-y-6" @submit.prevent="handleSubmit">
          <div class="space-y-4">
            <div>
              <label for="email" class="text-sm font-medium text-gray-300">Email</label>
              <input
                id="email"
                v-model="email"
                type="email"
                required
                class="w-full px-4 py-2 mt-1 text-white bg-[#0F172A] rounded-lg focus:ring-2 focus:ring-[#3576DF]"
              />
            </div>
            <div>
              <label for="password" class="text-sm font-medium text-gray-300">Senha</label>
              <input
                id="password"
                v-model="password"
                type="password"
                required
                minlength="6"
                class="w-full px-4 py-2 mt-1 text-white bg-[#0F172A] rounded-lg focus:ring-2 focus:ring-[#3576DF]"
              />
            </div>
          </div>
  
          <div v-if="authStore.error" class="text-red-500 text-sm text-center">
            {{ authStore.error }}
          </div>
  
          <button
            type="submit"
            :disabled="authStore.loading"
            class="w-full py-2 px-4 bg-[#3576DF] text-white rounded-lg hover:bg-[#285bb8] focus:ring-2 focus:ring-[#3576DF] disabled:opacity-50"
          >
            {{ authStore.loading ? 'Entrando...' : 'Entrar' }}
          </button>
  
          <div class="text-center text-sm">
            <router-link to="/signup" class="text-[#3576DF] hover:text-[#285bb8]">
              Não tem uma conta? Registre-se
            </router-link>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref } from 'vue';
  import { useAuthStore } from '@/stores/auth';
  
  const authStore = useAuthStore();
  const email = ref('');
  const password = ref('');
  
  async function handleSubmit() {
    try {
      await authStore.signin(email.value, password.value);
    } catch (error) {
      // Error already handled in store
    }
  }
  </script>
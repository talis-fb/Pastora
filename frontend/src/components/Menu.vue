<template>
  <header class="flex items-center justify-between px-8 py-6 bg-[#09090B] border-b border-[#1E293B]">
    <!-- Logo e Título -->
    <div class="flex items-center space-x-4">
      <img src="@/assets/logo.png" alt="Logo" class="h-14 w-auto">
      <span class="text-3xl font-bold">Pastora</span>
    </div>

    <!-- Navegação -->
    <nav class="flex space-x-8 ml-16 text-[#94A3B8] text-lg">
      <router-link
        to="/services"
        class="hover:text-white flex items-center"
        active-class="text-white"
      >
        <span class="material-icons-outlined mr-2">cloud</span> Services
      </router-link>
      <router-link
        to="/monitors"
        class="hover:text-white flex items-center"
        active-class="text-white"
      >
        <span class="material-icons-outlined mr-2">monitor</span> Monitors
      </router-link>

    <!-- Logout -->
    <div class="flex items-center space-x-4">
      <button
        @click="logout"
        class="hover:text-white flex items-center text-[#94A3B8] text-lg"
      >
        <span class="material-icons-outlined mr-2">logout</span> Logout
      </button>
    </div>      
    </nav>
  </header>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'; // Substitua pelo caminho correto do store

// Importa o store e obtém a função logout
const authStore = useAuthStore();
const logout = () => {
  authStore.logout(); // Executa o logout do usuário
};
</script>

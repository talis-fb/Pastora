<template>
  <router-view v-if="!authStore.token" />
  <Dashboard v-else />
</template>

<script setup>
import { useAuthStore } from '@/stores/auth';
import Dashboard from './views/Dashboard.vue';

const authStore = useAuthStore();
</script>
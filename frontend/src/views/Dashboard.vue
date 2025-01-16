<template>
  <div class="min-h-screen bg-[#020817] text-white">
    <!-- Menu -->
    <Menu />

    <!-- Rotas -->
    <router-view />
  </div>
</template>

<script setup>
import Menu from "@/components/Menu.vue";
</script>

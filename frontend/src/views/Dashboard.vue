<template>
  <div class="min-h-screen bg-[#020817] text-white">
    <!-- Menu -->
    <Menu />

    <!-- Rotas -->
    <router-view />
  </div>
</template>

<script>
import Menu from "@/components/Menu.vue";

export default {
  name: "Dashboard",
  components: {
    Menu,
  },
};
</script>

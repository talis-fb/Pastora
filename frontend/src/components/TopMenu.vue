<template>
  <nav class="top-menu">
    <ul>
      <li>
        <router-link to="/">
          <img src="/public/favicon.ico" alt="Logo">
        </router-link>
      </li>
      <li><router-link to="/">Home</router-link></li>
      <li><router-link to="/services">Services</router-link></li>
      <li><router-link to="/monitors">Monitors</router-link></li>
      <li><router-link to="/settings">Settings</router-link></li>
    </ul>
  </nav>
</template>

<script setup lang="ts">
</script>

<style scoped>
  .top-menu {
    background-color: #333;
    color: white;
    padding: 10px;
  }
  .top-menu ul {
    display: flex;
    gap: 10px;
    list-style: none;
  }
  .top-menu ul li a {
    color: white;
    text-decoration: none;
  }
</style>
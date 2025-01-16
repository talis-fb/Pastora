import { createRouter, createWebHistory } from "vue-router";

// Importar as páginas ou componentes para cada rota
import ServicesPage from "@/views/ServicesPage.vue";
import MonitorsPage from "@/views/MonitorsPage.vue";

const routes = [
  { path: "/", redirect: "/services" }, // Redirecionar para Services como padrão
  { path: "/services", component: ServicesPage },
  { path: "/monitors", component: MonitorsPage },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;

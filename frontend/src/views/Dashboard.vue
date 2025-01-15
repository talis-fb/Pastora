<template>
  <div class="min-h-screen bg-[#020817] text-white">
    <!-- Navbar -->
    <header class="flex items-center justify-between px-8 py-6 bg-[#09090B] border-b border-[#1E293B]">
      <!-- Logo e Título -->
      <div class="flex items-center space-x-4">
        <img src="@/assets/logo.png" alt="Logo" class="h-14 w-auto">
        <span class="text-3xl font-bold">Pastora</span>
      </div>

      <!-- Navegação -->
      <nav class="flex space-x-8 ml-16 text-[#94A3B8] text-lg">
        <a href="#" class="hover:text-white flex items-center">
          <span class="material-icons-outlined mr-2">cloud</span> Services
        </a>
        <a href="#" class="hover:text-white flex items-center">
          <span class="material-icons-outlined mr-2">monitor</span> Monitors
        </a>
      </nav>
    </header>

    <!-- Search Bar e Botão Add Service -->
    <div class="flex items-center px-8 py-6 bg-[#09090B]">
      <!-- Barra de Busca (Centralizada) -->
      <div class="flex-1 flex justify-center">
        <input type="text" placeholder="Buscar"
          class="w-full max-w-md px-4 py-2 text-sm rounded-lg bg-[#1E293B] text-[#94A3B8] placeholder-[#94A3B8] focus:outline-none focus:ring-2 focus:ring-[#3576DF]" />
      </div>

      <!-- Botão -->
      <button
        class="ml-4 px-6 py-2 bg-[#3576DF] text-sm font-semibold rounded-lg hover:bg-[#285bb8] focus:ring-2 focus:ring-[#3576DF]">
        Add Service
      </button>
    </div>

    <!-- Services List -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 px-8">
      <!-- Iteração sobre os serviços para popular os cards -->
      <ServiceCard v-for="service in services" :key="service.id" :name="service.name" :image="service.iconUrl" />
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from "vue"; // Importa os hooks da Composition API
import ServiceCard from "@/components/ServiceCard.vue";
import serviceApi from "@/api/servicesApi"; // Importe a API

export default {
  components: {
    ServiceCard,
  },
  setup() {
    // Cria a variável de serviços como uma referência reativa
    const services = ref([]);
    console.log(services);
    // Função para carregar os serviços
    const loadServices = async () => {
      try {
        services.value = await serviceApi.findServices();
      } catch (error) {
        console.error("Erro ao carregar serviços:", error);
      }
    };

    // Chama a função para carregar os serviços quando o componente for montado
    onMounted(loadServices);

    return {
      services,
    };
  },
};
</script>

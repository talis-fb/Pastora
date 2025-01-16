<template>
  <div class="min-h-screen bg-[#020817] text-white">
    <!-- Navbar -->
    <header class="flex items-center justify-between px-4 py-4 sm:px-8 sm:py-6 bg-[#09090B] border-b border-[#1E293B]">
      <!-- Logo e Título -->
      <div class="flex items-center space-x-4">
        <img src="@/assets/logo.png" alt="Logo" class="h-12 w-auto sm:h-14">
        <span class="text-xl sm:text-3xl font-bold">Pastora</span>
      </div>

      <!-- Navegação -->
      <nav class="hidden sm:flex space-x-6 sm:space-x-8 ml-4 text-[#94A3B8] text-sm sm:text-lg">
        <a href="#" class="hover:text-white flex items-center">
          <span class="material-icons-outlined mr-1 sm:mr-2">cloud</span> Services
        </a>
        <a href="#" class="hover:text-white flex items-center">
          <span class="material-icons-outlined mr-1 sm:mr-2">monitor</span> Monitors
        </a>
      </nav>
    </header>

    <!-- Search Bar e Botão -->
    <div class="flex flex-col items-center mt-4 sm:mt-6 px-4 sm:px-8 space-y-4 sm:space-y-0">
      <div class="flex items-center justify-between w-full sm:max-w-screen-lg">
        <!-- Barra de Busca -->
        <input type="text" placeholder="Buscar"
          class="flex-1 px-4 py-2 text-sm rounded-lg bg-[#1E293B] text-[#94A3B8] placeholder-[#94A3B8] focus:outline-none focus:ring-2 focus:ring-[#3576DF]" />
        <!-- Botão -->
        <button
          class="ml-4 px-6 py-2 bg-[#3576DF] text-sm font-semibold rounded-lg hover:bg-[#285bb8] focus:ring-2 focus:ring-[#3576DF] ml-15">
          Add Service
        </button>
      </div>
    </div>

    <!-- Services List -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4 sm:gap-6 px-4 sm:px-8 mt-6 sm:mt-8">
  <!-- Renderiza os cards diretamente -->
  <ServiceCard v-for="service in services" 
    :key="service.id" 
    :name="service.name" 
    :image="service.icon" 
  />
  
  <!-- Mensagem caso não haja serviços -->
  <div v-if="services.length === 0" class="flex justify-center text-lg text-[#94A3B8]">
    Nenhum serviço encontrado.
  </div>
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
        const defaultIcon = new URL('@/assets/logo.png', import.meta.url).href;
        const fetchedServices = await serviceApi.findServices();
        // Para cada serviço, busca o ícone e adiciona ao serviço
        for (let service of fetchedServices) {
            service.icon = !!service?.iconUrl 
              ? serviceApi.getFullIconUrl(service.iconUrl)
              : defaultIcon;
        }
        services.value = fetchedServices;
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

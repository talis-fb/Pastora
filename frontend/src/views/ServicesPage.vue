<template>
  <div class="px-8 py-6">
    <!-- Barra de Busca e Botão Add Service -->
    <div class="flex items-center mb-6">
      <!-- Barra de Busca -->
      <div class="flex-1 flex justify-center">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Buscar"
          class="w-full max-w-md px-4 py-2 text-sm rounded-lg bg-[#1E293B] text-[#94A3B8] placeholder-[#94A3B8] focus:outline-none focus:ring-2 focus:ring-[#3576DF]"
        />
      </div>

      <!-- Botão -->
      <button
        class="ml-4 px-6 py-2 bg-[#3576DF] text-sm font-semibold rounded-lg hover:bg-[#285bb8] focus:ring-2 focus:ring-[#3576DF]"
        @click="addService"
      >
        Add Service
      </button>
    </div>

    <!-- Lista de Serviços -->
    <div v-if="filteredServices.length > 0" class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <ServiceCard
        v-for="service in filteredServices"
        :key="service.id"
        :name="service.name"
        :image="service.icon"
      />
    </div>

    <!-- Mensagem caso não haja serviços -->
    <div v-else class="text-center text-lg text-[#94A3B8]">
      Nenhum serviço encontrado.
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from "vue";
import ServiceCard from "@/components/ServiceCard.vue";
import serviceApi from "@/api/servicesApi";

export default {
  name: "ServicesPage",
  components: { ServiceCard },
  setup() {
    const services = ref([]);
    const searchQuery = ref("");

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

    // Serviços filtrados com base no texto de busca
    const filteredServices = computed(() => {
      return services.value.filter((service) =>
        service.name.toLowerCase().includes(searchQuery.value.toLowerCase())
      );
    });

    // Função para adicionar um serviço
    const addService = () => {
      alert("A funcionalidade de adicionar serviço será implementada futuramente.");
    };

    onMounted(loadServices);

    return {
      services,
      searchQuery,
      filteredServices,
      addService,
    };
  },
};
</script>

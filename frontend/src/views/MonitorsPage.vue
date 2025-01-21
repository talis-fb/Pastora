<template>
  <div class="px-8 py-6">
    <!-- Search Bar and Add Monitor Button -->
    <div class="flex items-center mb-6">
      <div class="flex-1 flex justify-center">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Search"
          class="w-full max-w-md px-4 py-2 text-sm rounded-lg bg-[#1E293B] text-[#94A3B8] placeholder-[#94A3B8] focus:outline-none focus:ring-2 focus:ring-[#3576DF]"
        />
      </div>

      <button
        class="ml-4 px-6 py-2 bg-[#3576DF] text-sm font-semibold rounded-lg hover:bg-[#285bb8] focus:ring-2 focus:ring-[#3576DF]"
        @click="addMonitor"
      >
        Add Monitor
      </button>
    </div>

    <!-- Monitors List -->
    <div v-if="filteredMonitors.length > 0" class="space-y-4">
      <div
        v-for="monitor in filteredMonitors"
        :key="monitor.id"
        class="flex items-center justify-between p-4 bg-[#1E293B] rounded-lg"
      >
        <!-- Monitor Info -->
        <div class="flex-[2]">
          <h3 class="text-xl font-semibold text-white">{{ monitor.name }}</h3>
        </div>

        <div class="flex-[1] text-center">
          <span class="text-lg text-[#94A3B8]">
            {{ monitor.serviceName ?? "Sem Serviço"}}
          </span>
        </div>

        <!-- Status Indicators -->
        <div class="flex-[4] flex justify-end flex-wrap gap-2">
          <div
            v-for="execution in monitor.executions"
            :key="execution.id"
            :title="`${new Date(execution.startedTime).toLocaleString('pt-BR')}\n\n${execution.errors.length ? execution.errors.join('\n') : 'Sucess'}`
            "
            :class="[
              'h-3 w-3 rounded-full inline',
              execution.errors.length === 0 ? 'bg-blue-500' : 'bg-red-500'
            ]"
          ></div>
        </div>
      </div>
    </div>

    <!-- Empty State Message -->
    <div v-else class="text-center text-lg text-[#94A3B8]">
      No monitors found.
    </div>

    <!-- Add Monitor Modal -->
    <AddMonitorModal 
      ref="addMonitorModalRef"
      @monitor-added="handleMonitorAdded"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import AddMonitorModal from '@/components/AddMonitorModal.vue';
import monitorApi from '@/api/monitorApi';
import executionsApi from '@/api/executionsApi';
import serviceApi from '@/api/servicesApi';

// Data and State
const monitors = ref([]);
const searchQuery = ref('');
const addMonitorModalRef = ref();
let intervalId: number | undefined;

// Load Monitors
const loadMonitors = async () => {
  try {
    const findedMonitors = await monitorApi.getMonitors();
    const monitorsWithDetails = await Promise.all(
      findedMonitors.map(async (monitor) => {
        const executions = await executionsApi.findExecutionsByMonitorId(monitor.id);
        const service = monitor.serviceId? await serviceApi.findServiceById(monitor.serviceId) : null;

        return {
          ...monitor,
          executions: executions.slice(-30), // Últimos 30 executions
          serviceName: service?.name,
        };
      })
    );

    monitors.value = monitorsWithDetails;
  } catch (error) {
    console.error('Error loading monitors:', error);
  }
};

// Filter Monitors
const filteredMonitors = computed(() => {
  return monitors.value.filter((monitor) =>
    monitor.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

// Add Monitor
const addMonitor = () => {
  addMonitorModalRef.value.dialog = true;
};

// Handle Monitor Added
const handleMonitorAdded = async () => {
  await loadMonitors();
};

// Lifecycle Hooks
onMounted(() => {
  // Load monitors initially
  loadMonitors();

  // Set interval to refresh monitors every minute
  intervalId = setInterval(() => {
    loadMonitors();
  }, 60 * 1000);
});

onBeforeUnmount(() => {
  // Clear the interval when the component is destroyed
  if (intervalId) clearInterval(intervalId);
});
</script>

<style scoped>
</style>
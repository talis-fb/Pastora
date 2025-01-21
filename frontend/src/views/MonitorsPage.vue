<template>
  <div class="px-8 py-6">
    <!-- Search Bar and Add Monitor Button -->
    <div class="flex items-center mb-6">
      <div class="flex-1 flex justify-center">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Buscar"
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
    <div v-if="filteredMonitors.length > 0" class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div
        v-for="monitor in filteredMonitors"
        :key="monitor.id"
        class="p-4 bg-[#1E293B] rounded-lg"
      >
        <h3 class="text-lg font-semibold mb-2">{{ monitor.name }}</h3>
        <p class="text-[#94A3B8]">{{ monitor.trigger }}</p>
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
import { ref, computed, onMounted } from 'vue';
import AddMonitorModal from '@/components/AddMonitorModal.vue';
import monitorApi from '@/api/monitorApi';

const monitors = ref([]);
const searchQuery = ref('');
const addMonitorModalRef = ref();

const loadMonitors = async () => {
  try {
    monitors.value = await monitorApi.getMonitors();
  } catch (error) {
    console.error('Error loading monitors:', error);
  }
};

const filteredMonitors = computed(() => {
  return monitors.value.filter((monitor) =>
    monitor.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const addMonitor = () => {
  addMonitorModalRef.value.dialog = true;
};

const handleMonitorAdded = async () => {
  await loadMonitors();
};

onMounted(loadMonitors);
</script>
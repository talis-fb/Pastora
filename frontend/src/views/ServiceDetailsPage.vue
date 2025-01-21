<template>
  <div class="px-8 py-6 bg-[#0F172A] min-h-screen">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-8">
      <div class="flex items-center gap-4">
        <!-- Imagem do serviço -->
        <img :src="imageUrl" v-if="service?.iconUrl" :alt="service.name" class="h-12" />
        <img src="@/assets/logo.png" v-else alt="Node Service" class="h-12" />
        <h1 class="text-2xl font-bold text-white">{{ service?.name }}</h1>
      </div>
      <button
        class="px-4 py-2 bg-[#3576DF] text-sm font-semibold rounded-lg hover:bg-[#285bb8] focus:ring-2 focus:ring-[#3576DF]"
        @click="addMonitor"
      >
        Add Monitor
      </button>
    </div>
    ​
    <!-- Monitors List -->
    <div v-if="monitors.length > 0" class="space-y-6">
      <div
        v-for="monitor in monitors"
        :key="monitor.id"
        class="p-6 bg-[#1E293B] rounded-lg flex items-center justify-between"
      >
        <!-- Monitor Details -->
        <div class="flex-[3]">
          <h2 class="text-lg font-semibold text-white">
            {{ monitor.name }}
            <span class="px-2 py-1 bg-gray-700 rounded-full">HTTP</span>
          </h2>
          <p class="text-sm text-[#94A3B8]">{{ monitor.http.url }}</p>
        </div>

        <!-- Monitor Status -->
        <!-- Response Times -->
        <div class="flex-[2] text-center">
          <p class="text-white text-lg">{{ monitor.stats.lastResponseTime }}ms</p>
          <p class="text-sm text-[#94A3B8]">Last Response</p>
        </div>
        <div class="flex-[2] text-center">
          <p class="text-white text-lg">{{ monitor.stats.averageResponseTime }}ms</p>
          <p class="text-sm text-[#94A3B8]">Avg Response</p>
        </div>

        <!-- Total Uptime -->
        <div class="flex-[1] text-center">
          <p class="text-white text-lg">{{ monitor.stats.uptimeHours + "h" }}</p>
          <p class="text-sm text-[#94A3B8]">Uptime</p>
        </div>

        <div class="flex-[1] text-center">
          <p class="text-white text-lg font-semibold">{{ monitor.stats.uptimePercentage }}%</p>
          <p class="text-sm text-[#94A3B8]">Uptime 24h</p>
        </div>        

        <!-- Success Percentage -->
        <div class="flex-[1] text-center">
          <p class="text-white text-lg font-semibold">{{ monitor.stats.uptimePercentage }}%</p>
          <!-- <p class="text-sm text-[#94A3B8]">Success</p> -->
        </div>        

        <!-- Execution Indicators -->
        <div class="flex-[4] flex justify-end gap-1">
          <span
            v-for="execution in monitor.executions"
            :key="execution.id"
            :title="
              `${new Date(execution.startedTime).toLocaleString('pt-BR')}\n\n${
                execution.errors.length ? execution.errors.join('\n') : 'Success'
              }`
            "
            :class="[
              'h-3 w-3 rounded-full',
              execution.errors.length === 0 ? 'bg-blue-500' : 'bg-red-500'
            ]"
          ></span>
        </div>
      </div>
    </div>
    <!-- Empty State -->
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
import MonitorCard from '@/components/MonitorCard.vue';
import monitorApi from '@/api/monitorApi';
import executionsApi from '@/api/executionsApi';
import serviceApi from '@/api/servicesApi';

const service = ref(null);
const monitors = ref([]);
const addMonitorModalRef = ref();
let intervalId: number | undefined;

const props = defineProps({
  id: {
    type: String,
    required: true,
  },
});

const loadService = async () => {
  try {
    const findedService = await serviceApi.findServiceById(props.id);
    service.value = findedService;
  } catch (error) {
    console.error('Error loading service:', error);
  }
};

const loadMonitors = async () => {
  try {
    const findedMonitors = await monitorApi.getMonitorsByService(props.id);
    const monitorsWithDetails = await Promise.all(
      findedMonitors.map(async (monitor) => {
        const executions = await executionsApi.findExecutionsByMonitorId(monitor.id);

        // Calcular estatísticas
        const stats = calculateMonitorStats(executions);

        return {
          ...monitor,
          executions: executions.slice(-30), // Últimos 30 executions
          stats, // Adiciona as estatísticas calculadas
        };
      })
    );

    monitors.value = monitorsWithDetails;
  } catch (error) {
    console.error('Error loading monitors:', error);
  }
};

const imageUrl = computed(() => {
  if (!service.value?.iconUrl) return '';
  const baseUrl = import.meta.env.VITE_API_URL || '';
  return `${baseUrl}/services/download-icon/${service.value.iconUrl}`;
});

// Funções para calcular estatísticas
function calculateUptimePercentage(executions: Array<any>): number {
  const totalExecutions = executions.length;
  if (totalExecutions === 0) return 0;

  const successfulExecutions = executions.filter(
    (execution) => execution.errors.length === 0
  ).length;

  return (successfulExecutions / totalExecutions) * 100;
}

function calculateAverageResponseTime(executions: Array<any>): number {
  const totalExecutions = executions.length;
  if (totalExecutions === 0) return 0;

  const responseTimes = executions.map((execution) => {
    const startTime = new Date(execution.startedTime).getTime();
    const finishTime = new Date(execution.finishedTime).getTime();
    return Math.abs(finishTime - startTime);
  });

  const totalResponseTime = responseTimes.reduce((acc, time) => acc + time, 0);
  return totalResponseTime / totalExecutions;
}

function calculateLastResponseTime(executions: Array<any>): number {
  if (executions.length === 0) return 0;

  const lastExecution = executions[executions.length - 1];
  const startTime = new Date(lastExecution.startedTime).getTime();
  const finishTime = new Date(lastExecution.finishedTime).getTime();

  return Math.abs(finishTime - startTime);
}

function calculateUptime(executions: Array<any>) {
  const totalUptimeMs = executions.reduce((total, exec) => {
    return total + (new Date(exec.finishedTime).getTime() - new Date(exec.startedTime).getTime());
  }, 0);
  const uptimeHours = Math.floor(totalUptimeMs / (1000 * 60 * 60)); // converter de ms para horas
  return uptimeHours;
}

function calculateUptime24h(executions: Array<any>) {
  const now = new Date();
  const last24hExecutions = executions.filter(exec => {
    const finishedTime = new Date(exec.finishedTime);
    return finishedTime >= new Date(now.getTime() - 24 * 60 * 60 * 1000);
  });

  const successfulLast24h = last24hExecutions.filter(exec => exec.errors.length === 0).length;
  const uptime24hPercentage = (successfulLast24h / last24hExecutions.length) * 100 || 0; // Evitar divisão por 0
  return uptime24hPercentage;
}

function calculateMonitorStats(executions: Array<any>) {
  return {
    uptimePercentage: calculateUptimePercentage(executions).toFixed(1),
    uptimeHours: calculateUptime(executions),
    uptime24h: calculateUptime24h(executions),
    averageResponseTime: Math.round(calculateAverageResponseTime(executions)),
    lastResponseTime: calculateLastResponseTime(executions),
  };
}

const addMonitor = () => {
  addMonitorModalRef.value.dialog = true;
};

const handleMonitorAdded = async () => {
  await loadMonitors();
};

onMounted(() => {
  loadService();
  loadMonitors();
  intervalId = setInterval(loadMonitors, 60 * 1000);
});

onBeforeUnmount(() => {
  if (intervalId) clearInterval(intervalId);
});
</script>

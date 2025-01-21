<template>
  <div class="bg-gray-800 p-6 rounded-lg text-white font-sans">
    <div class="flex justify-between items-center mb-4">
      <span class="text-lg font-bold">{{ monitorName }}</span>
      <span class="text-sm bg-blue-600 px-2 py-1 rounded">{{ monitorType }}</span>
    </div>
    <div class="text-sm text-gray-400 mb-6">{{ monitorUrl }}</div>
    <div class="flex items-center mb-4">
      <span class="text-2xl font-bold mr-4">{{ successPercentage }}%</span>
      <div class="flex gap-1">
        <span
          v-for="execution in executions"
          :key="execution.id"
          :title="
            `${new Date(execution.startedTime).toLocaleString('pt-BR')}\n\n${execution.errors.length ? execution.errors.join('\n') : 'Success'}`
          "
          :class="[
            'h-3 w-3 rounded-full',
            execution.errors.length === 0 ? 'bg-blue-500' : 'bg-red-500'
          ]"
        ></span>
      </div>
    </div>
    <div class="grid grid-cols-2 gap-4 text-sm">
      <div class="flex flex-col items-center">
        <span class="font-bold">Last response</span>
        <span class="text-gray-300">{{ lastResponse }}</span>
      </div>
      <div class="flex flex-col items-center">
        <span class="font-bold">Average response</span>
        <span class="text-gray-300">{{ averageResponse }}</span>
      </div>
      <div class="flex flex-col items-center">
        <span class="font-bold">Uptime</span>
        <span class="text-gray-300">{{ uptime }}</span>
      </div>
      <div class="flex flex-col items-center">
        <span class="font-bold">Uptime 24h</span>
        <span class="text-gray-300">{{ uptime24h }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  monitorName: String,
  monitorType: String,
  monitorUrl: String,
  lastResponse: String,
  averageResponse: String,
  uptime: String,
  uptime24h: String,
  successPercentage: Number,
  executions: Array<any>
});  
</script>

<style>
/* Opcional: Adicione aqui classes customizadas se necessário */
</style>

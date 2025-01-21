<template>
<v-dialog v-model="dialog" max-width="800px">
<v-card class="!bg-[#1E293B] p-8 rounded-lg shadow-lg">
<div class="px-6 py-6">
    <h2 class="text-xl font-semibold text-[#E2E8F0] mb-6">Adicionar Monitor</h2>

    <v-form @submit.prevent="handleSubmit">
    <div class="space-y-6">
        <!-- Basic Information -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="space-y-2">
            <label class="text-sm text-gray-300">Nome</label>
            <input
                v-model="formData.name"
                type="text"
                class="w-full px-3 py-2 bg-[#0F172A] rounded-md border border-gray-600 focus:border-[#3576DF] focus:outline-none text-gray-100"
                required
            />
            </div>

            <div class="space-y-2">
            <label class="text-sm text-gray-300">Descrição</label>
            <input
                v-model="formData.description"
                type="text"
                class="w-full px-3 py-2 bg-[#0F172A] rounded-md border border-gray-600 focus:border-[#3576DF] focus:outline-none text-gray-100"
            />
            </div>
        </div>

        <!-- Service Selection -->
        <div class="space-y-2">
            <label class="text-sm text-gray-300">Serviço</label>
            <select
            v-model="formData.serviceId"
            class="w-full px-3 py-2 bg-[#0F172A] rounded-md border border-gray-600 focus:border-[#3576DF] focus:outline-none text-gray-100"
            required
            >
            <option value="" disabled selected>Selecione um serviço</option>
            <option
                v-for="service in services"
                :key="service.id"
                :value="service.id"
            >
                {{ service.name }}
            </option>
            </select>
        </div>

        <!-- Interval Rate -->
        <div class="space-y-2">
            <label class="text-sm text-gray-300">Acionar a cada...</label>
            <select
            v-model="selectedInterval"
            class="w-full px-3 py-2 bg-[#0F172A] rounded-md border border-gray-600 focus:border-[#3576DF] focus:outline-none text-gray-100"
            required
            >
            <option
                v-for="option in intervalOptions"
                :key="option"
                :value="option"
            >
                {{ option }}
            </option>
            </select>
        </div>

        <!-- HTTP Configuration -->
        <div class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="space-y-2">
                <label class="text-sm text-gray-300">URL</label>
                <input
                v-model="formData.http.url"
                type="text"
                class="w-full px-3 py-2 bg-[#0F172A] rounded-md border border-gray-600 focus:border-[#3576DF] focus:outline-none text-gray-100"
                required
                />
            </div>

            <div class="space-y-2">
                <label class="text-sm text-gray-300">Método HTTP</label>
                <select
                v-model="formData.http.method"
                class="w-full px-3 py-2 bg-[#0F172A] rounded-md border border-gray-600 focus:border-[#3576DF] focus:outline-none text-gray-100"
                required
                >
                <option
                    v-for="method in ['GET', 'POST', 'PUT', 'DELETE']"
                    :key="method"
                    :value="method"
                >
                    {{ method }}
                </option>
                </select>
            </div>
            </div>

            <div class="space-y-2">
            <label class="text-sm text-gray-300">Body</label>
            <textarea
                v-model="formData.http.body"
                rows="4"
                class="w-full px-3 py-2 bg-[#0F172A] rounded-md border border-gray-600 focus:border-[#3576DF] focus:outline-none text-gray-100"
            ></textarea>
            </div>
        </div>

        <!-- Validations -->
        <div class="space-y-4">
            <div
            v-for="(validation, index) in formData.validations"
            :key="index"
            class="grid grid-cols-1 md:grid-cols-3 gap-4"
            >
            <div class="space-y-2">
                <label class="text-sm text-gray-300">Campo</label>
                <select
                v-model="validation.field"
                class="w-full px-3 py-2 bg-[#0F172A] rounded-md border border-gray-600 focus:border-[#3576DF] focus:outline-none text-gray-100"
                required
                >
                <option value="status">status</option>
                <option value="body">body</option>
                </select>
            </div>

            <div class="space-y-2">
                <label class="text-sm text-gray-300">Operação</label>
                <select
                v-model="validation.operation"
                class="w-full px-3 py-2 bg-[#0F172A] rounded-md border border-gray-600 focus:border-[#3576DF] focus:outline-none text-gray-100"
                required
                >
                <option value="equals">equals</option>
                <option value="not_equals">not equals</option>
                <option value="contains">contains</option>
                </select>
            </div>

            <div class="space-y-2">
                <label class="text-sm text-gray-300">Valor</label>
                <input
                v-model="validation.value"
                type="text"
                class="w-full px-3 py-2 bg-[#0F172A] rounded-md border border-gray-600 focus:border-[#3576DF] focus:outline-none text-gray-100"
                required
                />
            </div>
            </div>
        </div>

        <div class="flex justify-end">
            <button
            type="button"
            @click="addValidation"
            class="px-4 py-2 text-sm font-medium text-[#3576DF] hover:text-[#285bb8] focus:outline-none"
            >
            Adicionar Validação
            </button>
        </div>
    </div>
    </v-form>
</div>

<div class="flex justify-end space-x-4 mt-6 px-6 py-6">
    <button
    @click="dialog = false"
    class="px-4 py-2 text-sm font-medium text-red-500 hover:text-red-400 focus:outline-none"
    >
    Cancelar
    </button>
    <button
    @click="handleSubmit"
    :disabled="loading"
    class="px-4 py-2 text-sm font-medium bg-[#3576DF] hover:bg-[#285bb8] rounded-md focus:outline-none disabled:opacity-50"
    >
    {{ loading ? 'Criando...' : 'Criar Monitor' }}
    </button>
</div>
</v-card>
</v-dialog>
</template>

<script setup lang="ts">
// Script section permanece o mesmo
import { ref, reactive, onMounted } from 'vue';
import monitorApi from '@/api/monitorApi';
import serviceApi from '@/api/servicesApi';

const emit = defineEmits(['monitor-added']);
const dialog = ref(false);
const loading = ref(false);
const services = ref([]);
const selectedInterval = ref('1 minuto');

const intervalOptions = [
'1 minuto',
'2 minutos',
'3 minutos',
'5 minutos',
'10 minutos',
'20 minutos',
'30 minutos',
'1 hora'
];

const formData = reactive({
enabled: true,
name: '',
description: '',
serviceId: '',
intervalRate: 1,
onSuccess: [],
onFail: [],
saveSuccessWhen: 'SUCCESS',
http: {
url: '',
method: 'GET',
headers: {},
body: ''
},
validations: [
{
field: 'status',
operation: 'equals',
value: '200'
}
]
});

const getIntervalRate = (interval: string): number => {
const [value, unit] = interval.split(' ');
if (unit.startsWith('hora')) {
return 60;
}
return parseInt(value);
};

const addValidation = () => {
formData.validations.push({
field: 'status',
operation: 'equals',
value: ''
});
};

const handleSubmit = async () => {
try {
    loading.value = true;
    formData.intervalRate = getIntervalRate(selectedInterval.value);

    await monitorApi.createMonitor(formData);
    dialog.value = false;
    emit('monitor-added');
} catch (error) {
    console.error('Error creating monitor:', error);
} finally {
    loading.value = false;
}
};

const loadServices = async () => {
try {
    services.value = await serviceApi.findServices();
} catch (error) {
    console.error('Error loading services:', error);
}
};

onMounted(loadServices);

defineExpose({ dialog });
</script>
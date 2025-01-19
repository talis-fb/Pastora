<template>
    <v-dialog v-model="dialog" max-width="500px">
      <div class="bg-[#1E293B] rounded-lg p-6">
        <form @submit.prevent="handleSubmit" class="space-y-6">
          <!-- Icon Upload -->
          <div>
            <label class="block text-sm font-medium text-gray-300 mb-2">Ícone</label>
            <div class="flex items-center space-x-4">
              <div 
                class="w-16 h-16 border-2 border-dashed border-gray-600 rounded-lg flex items-center justify-center overflow-hidden"
                :class="{ 'border-[#3576DF]': isDragging }"
                @dragover.prevent="isDragging = true"
                @dragleave.prevent="isDragging = false"
                @drop.prevent="handleDrop"
              >
                <img v-if="previewUrl" :src="previewUrl" class="w-full h-full object-cover" />
                <span v-else class="material-icons-outlined text-gray-400">add_photo_alternate</span>
              </div>
              <input
                type="file"
                ref="fileInput"
                class="hidden"
                accept="image/*"
                @change="handleFileChange"
              />
              <button
                type="button"
                class="px-4 py-2 bg-gray-700 text-sm rounded-lg hover:bg-gray-600"
                @click="$refs.fileInput.click()"
              >
                Upload Photo
              </button>
            </div>
            <p v-if="errors.icon" class="mt-1 text-sm text-red-500">{{ errors.icon }}</p>
          </div>
  
          <!-- Name Input -->
          <div>
            <label for="name" class="block text-sm font-medium text-gray-300 mb-2">Nome*</label>
            <input
              id="name"
              v-model="name"
              type="text"
              required
              class="w-full px-4 py-2 bg-[#0F172A] rounded-lg focus:ring-2 focus:ring-[#3576DF] text-white"
              :class="{ 'border-red-500': errors.name }"
            />
            <p v-if="errors.name" class="mt-1 text-sm text-red-500">{{ errors.name }}</p>
          </div>
  
          <!-- Action Buttons -->
          <div class="flex justify-end space-x-4 pt-4">
            <button
              type="button"
              class="px-4 py-2 bg-gray-700 text-sm rounded-lg hover:bg-gray-600"
              @click="closeModal"
            >
              Cancel
            </button>
            <button
              type="submit"
              class="px-4 py-2 bg-[#3576DF] text-sm rounded-lg hover:bg-[#285bb8]"
              :disabled="loading"
            >
              {{ loading ? 'Adding...' : 'Add Service' }}
            </button>
          </div>
        </form>
      </div>
    </v-dialog>
  </template>
  
  <script setup lang="ts">
  import { ref } from 'vue';
  import serviceApi from '@/api/servicesApi';
  
  const dialog = ref(false);
  const name = ref('');
  const selectedFile = ref<File | null>(null);
  const previewUrl = ref<string | null>(null);
  const isDragging = ref(false);
  const loading = ref(false);
  const errors = ref({ name: '', icon: '' });
  
  const emit = defineEmits(['service-added']);
  
  const handleFileChange = (event: Event) => {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];
      if (validateFile(file)) {
        selectedFile.value = file;
        createPreviewUrl(file);
      }
    }
  };
  
  const handleDrop = (event: DragEvent) => {
    isDragging.value = false;
    const file = event.dataTransfer?.files[0];
    if (file && validateFile(file)) {
      selectedFile.value = file;
      createPreviewUrl(file);
    }
  };
  
  const validateFile = (file: File): boolean => {
    const validTypes = ['image/jpeg', 'image/png', 'image/gif'];
    if (!validTypes.includes(file.type)) {
      errors.value.icon = 'Please upload a valid image file (JPEG, PNG, or GIF)';
      return false;
    }
    if (file.size > 5 * 1024 * 1024) { // 5MB limit
      errors.value.icon = 'Image size should be less than 5MB';
      return false;
    }
    errors.value.icon = '';
    return true;
  };
  
  const createPreviewUrl = (file: File) => {
    previewUrl.value = URL.createObjectURL(file);
  };
  
  const validateForm = (): boolean => {
    let isValid = true;
    errors.value = { name: '', icon: '' };
  
    if (!name.value.trim()) {
      errors.value.name = 'Name is required';
      isValid = false;
    }
  
    if (!selectedFile.value) {
      errors.value.icon = 'Icon is required';
      isValid = false;
    }
  
    return isValid;
  };
  
  const handleSubmit = async () => {
    if (!validateForm()) return;
  
    loading.value = true;
    try {
      await serviceApi.createService({
        name: name.value,
        icon: selectedFile.value as File
      });
      emit('service-added');
      closeModal();
    } catch (error: any) {
      console.error('Error creating service:', error);
    } finally {
      loading.value = false;
    }
  };
  
  const closeModal = () => {
    dialog.value = false;
    name.value = '';
    selectedFile.value = null;
    previewUrl.value = null;
    errors.value = { name: '', icon: '' };
  };
  
  defineExpose({
    dialog
  });
  </script>
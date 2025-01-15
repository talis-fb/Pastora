<template>
  <div class="flex items-center bg-gray-800 rounded-lg p-4 cursor-pointer">
    <img :src="image" alt="Service Logo" class="h-12 w-12 mr-4">
    <div class="flex-1">
      <h3 class="text-lg font-semibold">{{ name }}</h3>
      <div class="flex space-x-2 text-sm text-gray-400 mt-1">
        <span class="px-2 py-1 bg-gray-700 rounded-full">
          HTTP
        </span>
      </div>
    </div>
    <!-- <div class="flex flex-col items-end">
      <span class="text-lg font-bold">{{ status }}</span>
      <div class="w-32 h-2 rounded-full mt-1" :class="statusColor"></div>
    </div> -->
  </div>
</template>

<script>
export default {
  props: {
    name: String,
    image: String,
  },
};
</script>

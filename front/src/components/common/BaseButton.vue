<script setup>
const props = defineProps({
  type: {
    type: String,
    default: 'button'
  },
  disabled: {
    type: Boolean,
    default: false
  },
  class: {
    type: String,
    default: '',
  },
})
const emit = defineEmits(['click'])
</script>

<template>
  <button
    :type="type"
    :disabled="disabled"
    @click="emit('click', $event)"
    :class="[
      'w-full h-12 py-3 rounded-2xl text-white text-base font-semibold',
      'bg-gradient-to-r from-button-from to-button-to hover:from-[#5a63d8] hover:to-[#693f99] transition',
      'disabled:opacity-50',
      props.class,
    ]"
  >
    <slot />
  </button>
</template>

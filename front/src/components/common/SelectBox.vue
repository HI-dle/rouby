<script setup>
import { ref, watch } from 'vue'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'

const props = defineProps({
  modelValue: {
    type: [String, Number],
    default: '',
  },
  options: {
    type: Array,
    required: true,
    validator: (opts) =>
      Array.isArray(opts) &&
      opts.every(
        (opt) =>
          opt &&
          typeof opt.label === 'string' &&
          (typeof opt.value === 'string' || typeof opt.value === 'number'),
      ),
  },
  triggerClass: {
    type: String,
    default: '',
  },
})
const emit = defineEmits(['update:modelValue'])
const selected = ref(props.modelValue)

watch(
  () => props.modelValue,
  (val) => {
    selected.value = val
  },
  { immediate: true },
)

watch(
  () => selected.value,
  (val) => {
    emit('update:modelValue', val)
  },
)
</script>

<template>
  <Select v-model="selected">
    <SelectTrigger
      :class="[
        'w-32 h-auto text-base focus:outline-none focus:ring-0 focus:border-transparent focus:shadow-[0_0_3px_2px_theme(colors.main-color/30%)] transition',
        props.triggerClass,
      ]"
    >
      <SelectValue placeholder="없음" />
    </SelectTrigger>
    <SelectContent>
      <SelectGroup>
        <SelectItem
          v-for="opt in options"
          :key="opt.value"
          :value="opt.value"
          class="group cursor-pointer px-3 py-2 text-base rounded-md transition-colors focus:bg-focus-color data-[state=checked]:bg-focus-color"
        >
          {{ opt.label }}
        </SelectItem>
      </SelectGroup>
    </SelectContent>
  </Select>
</template>

<script setup>
import { X, Gem } from 'lucide-vue-next'
import { useToast } from '@/shared/composable/useToast'

const { toasts, dismiss } = useToast()

function handleClick(t) {
  try {
    if (typeof t.onClick === 'function') t.onClick()
  } finally {
    dismiss(t.id)
  }
}

function iconClass(variant) {
  const map = {
    default: 'text-neutral-400',
    info: 'text-blue-500',
    success: 'text-green-500',
    warning: 'text-yellow-500',
    error: 'text-red-500',
    notification: 'text-main-color',
  }
  return map[variant] || map.default
}
</script>

<template>
  <Teleport to="body">
    <div
      class="fixed inset-x-0 top-4 z-[9999] flex justify-center pointer-events-none"
      role="status"
      aria-live="polite"
    >
      <TransitionGroup name="toast" tag="div" class="space-y-2 w-full max-w-md">
        <div
          v-for="t in toasts"
          :key="t.id"
          class="mx-4 pointer-events-auto rounded-2xl shadow-lg border p-4 bg-white/95 backdrop-blur dark:bg-neutral-900/95 dark:border-neutral-800"
        >
          <button class="w-full text-left" @click="handleClick(t)">
            <div class="flex items-start gap-3">
              <div class="mt-0.5">
                <span :class="iconClass(t.variant)"><Gem /></span>
              </div>
              <div class="flex-1">
                <p v-if="t.title" class="font-medium leading-tight">
                  {{ t.title }}
                </p>
                <p class="text-sm text-neutral-700 dark:text-neutral-300">
                  {{ t.message }}
                </p>
              </div>
              <button
                aria-label="Dismiss"
                class="text-neutral-400 hover:text-neutral-700 dark:hover:text-neutral-200"
                @click.stop="dismiss(t.id)"
              >
                <X />
              </button>
            </div>
          </button>
        </div>
      </TransitionGroup>
    </div>
  </Teleport>
</template>

<style scoped>
.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translateY(-8px) scale(0.98);
}
.toast-enter-active,
.toast-leave-active {
  transition: all 0.18s ease;
}
</style>

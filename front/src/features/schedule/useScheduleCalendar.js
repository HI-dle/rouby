import { ref, computed, watch, nextTick, reactive } from 'vue'
import { addDays, addMonths, formatISO, subMonths } from 'date-fns'
import { useRouter } from 'vue-router'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'
import { isAllDay } from '@/shared/utils/dateUtils'

export const useScheduleCalendar = (
  schedulesForSelectedMonth,
  baseDate,
  goToScheduleDetailOf,
) => {
  const router = useRouter()
  const calendarRef = ref(null)

  const convertedSchedules = computed(() =>
    (schedulesForSelectedMonth.value ?? []).map((raw) => ({
      id: raw.id,
      instanceDate: raw.instanceDate,
      allDay: isAllDay(raw.startAt, raw.endAt),
      title: raw.title,
      start: new Date(raw.startAt),
      end: new Date(raw.endAt),
    })),
  )

  const goToScheduleDetailFromFCall = (info) => {
    goToScheduleDetailOf(info.event.id, info.event.extendedProps.instanceDate)
  }

  let isSelectTriggered = false
  const onRangeSelect = (info) => {
    isSelectTriggered = true
    router.push({
      path: '/schedule/create',
      query: {
        start: formatISO(info.start),
        end: formatISO(info.end),
        allDay: info.allDay,
      },
    })
    setTimeout(() => (isSelectTriggered = false), 0)
  }

  const onDateClick = (info) => {
    if (isSelectTriggered) return
    router.push({
      path: '/schedule/create',
      query: {
        start: formatISO(info.date),
        end: formatISO(addDays(info.date, 1)),
        allDay: info.allDay,
      },
    })
  }

  const prevMonth = () => {
    baseDate.value = subMonths(baseDate.value, 1)
    calendarRef.value.getApi().prev()
  }

  const nextMonth = () => {
    baseDate.value = addMonths(baseDate.value, 1)
    calendarRef.value.getApi().next()
  }

  const calendarOptions = reactive({
    plugins: [dayGridPlugin, interactionPlugin],
    initialView: 'dayGridMonth',
    initialDate: baseDate.value,
    dayCellContent: (arg) => ({ html: String(arg.date.getDate()) }),
    events: [],
    dayMaxEvents: 3,
    eventClick: goToScheduleDetailFromFCall,
    selectable: true,
    selectMirror: true,
    longPressDelay: 100,
    selectMinDistance: 1,
    dragScroll: true,
    editable: true,
    select: onRangeSelect,
    dateClick: onDateClick,
    headerToolbar: false,
    locale: 'ko',
    height: 'auto',
  })

  watch(
    convertedSchedules,
    async (events) => {
      await nextTick()
      const api = calendarRef.value?.getApi()
      if (!api || !events || events.length === 0) return
      api.removeAllEvents()
      api.addEventSource(events)
    },
    { immediate: true },
  )

  return {
    calendarRef,
    calendarOptions,
    prevMonth,
    nextMonth,
  }
}

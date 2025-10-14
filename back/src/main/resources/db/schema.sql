-- USER_DEVICE
ALTER TABLE user_device ALTER COLUMN id SET DEFAULT nextval('user_device_seq');

-- SCHEDULE
ALTER TABLE schedule ALTER COLUMN id SET DEFAULT nextval('schedule_seq');
DROP INDEX IF EXISTS idx_schedule_parent_not_deleted;
CREATE INDEX idx_schedule_parent_not_deleted
    ON schedule(parent_schedule_id)
    WHERE deleted_at IS NULL;

-- ROUTINE_TASKS
CREATE INDEX IF NOT EXISTS idx_rt_child_parent_active
    ON routine_tasks(parent_routine_task_id)
    WHERE deleted_at IS NULL;

-- DAILY_TASKS
CREATE INDEX IF NOT EXISTS idx_daily_tasks_task_rid_date_cover
    ON daily_tasks(routine_task_id, task_date)
    INCLUDE (current_value);

-- NOTIFICATION_EVENT
CREATE INDEX IF NOT EXISTS idx_ne_inprog_lease_until
    ON notification_event (lease_until)
    WHERE status='IN_PROGRESS';

-- FEEDBACK
CREATE UNIQUE INDEX IF NOT EXISTS uq_feedback_user_daily_slot
    ON feedback (user_id, feedback_date, slot)
    WHERE status <> 'FAILED';
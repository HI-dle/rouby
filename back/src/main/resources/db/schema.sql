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

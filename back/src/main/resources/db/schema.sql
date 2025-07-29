-- SCHEDULE
ALTER TABLE schedule ALTER COLUMN id SET DEFAULT nextval('schedule_seq');
DROP INDEX IF EXISTS idx_schedule_parent_not_deleted;
CREATE INDEX idx_schedule_parent_not_deleted
    ON schedule(parent_schedule_id)
    WHERE deleted_at IS NULL;
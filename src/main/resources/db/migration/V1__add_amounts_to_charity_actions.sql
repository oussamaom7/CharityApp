-- Add goal_amount and current_amount columns to charity_actions table
ALTER TABLE charity_actions
    ADD COLUMN goal_amount DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    ADD COLUMN current_amount DOUBLE PRECISION NOT NULL DEFAULT 0.0;

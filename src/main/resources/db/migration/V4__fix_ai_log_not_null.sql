-- product_id, ai_model, task_type의 NOT NULL 제약조건 해제
ALTER TABLE p_ai_log ALTER COLUMN product_id DROP NOT NULL;
ALTER TABLE p_ai_log ADD COLUMN IF NOT EXISTS task_type VARCHAR(255);
ALTER TABLE p_ai_log ALTER COLUMN ai_model DROP NOT NULL;
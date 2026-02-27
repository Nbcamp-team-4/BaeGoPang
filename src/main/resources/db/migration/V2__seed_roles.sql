-- V2__seed_roles.sql
-- 역할 마스터 데이터 시드
-- =====================

INSERT INTO p_role (id, role, created_at)
VALUES
    (gen_random_uuid(), 'CUSTOMER', CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'OWNER',    CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'MANAGER',  CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'ADMIN',    CURRENT_TIMESTAMP)
    ON CONFLICT (role) DO NOTHING;
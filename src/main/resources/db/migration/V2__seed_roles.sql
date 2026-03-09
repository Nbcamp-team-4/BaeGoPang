-- V2__seed_roles.sql
-- 역할 마스터 데이터 시드
-- =====================

INSERT INTO p_role (id, type, created_at)
VALUES
    (gen_random_uuid(), 'ROLE_CUSTOMER', CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'ROLE_OWNER',    CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'ROLE_MANAGER',  CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'ROLE_ADMIN',    CURRENT_TIMESTAMP)
    ON CONFLICT (type) DO NOTHING;
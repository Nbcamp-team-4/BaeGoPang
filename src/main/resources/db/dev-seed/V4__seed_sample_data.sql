-- V4__seed_sample_data.sql
-- 개발/로컬 전용 샘플 데이터
-- =========================================
-- 목적: Postman 테스트를 위한 최소 참조 데이터 제공
-- 주의: 운영 환경에서는 실행되지 않도록 db/dev-seed 경로로만 둔다.

-- 1) user (customer/owner 최소)
INSERT INTO p_user (id, login_id, email, password, name, phone, status, created_at)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'minji_customer', 'customer@test.com', 'hashed_pw', '민지(고객)', '010-1111-1111', 'ACTIVE', NOW()),
    ('22222222-2222-2222-2222-222222222222', 'minji_owner',    'owner@test.com',    'hashed_pw', '민지(사장)', '010-2222-2222', 'ACTIVE', NOW())
    ON CONFLICT (id) DO NOTHING;

-- 2) region (store FK)
INSERT INTO p_region (id, name, geom, is_active, created_at)
VALUES (
           'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
           '광화문',
           ST_Multi(ST_GeomFromText('POLYGON((126.970 37.570, 126.990 37.570, 126.990 37.585, 126.970 37.585, 126.970 37.570))', 4326)),
           true,
           NOW()
       )
    ON CONFLICT (id) DO NOTHING;

-- 3) category
INSERT INTO p_category (id, name, created_at)
VALUES
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '한식', NOW()),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', '치킨', NOW())
    ON CONFLICT (id) DO NOTHING;

-- 4) store
INSERT INTO p_store (
    id, user_id, region_id, name, description, address, location, phone, status,
    delivery_min_minutes, delivery_max_minutes, delivery_fee, minimum_order_amount,
    created_at
)
VALUES (
           'dddddddd-dddd-dddd-dddd-dddddddddddd',
           '22222222-2222-2222-2222-222222222222',
           'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
           '민지네 치킨',
           '바삭한 후라이드 맛집',
           '서울 종로구 어딘가 123',
           ST_SetSRID(ST_MakePoint(126.978, 37.575), 4326),
           '02-000-0000',
           'OPEN',
           20, 40, 2000, 12000,
           NOW()
       )
    ON CONFLICT (id) DO NOTHING;

-- 5) store-category mapping
INSERT INTO p_store_category (id, store_id, category_id, created_at)
VALUES
    ('dddd0000-0000-0000-0000-000000000001', 'dddddddd-dddd-dddd-dddd-dddddddddddd', 'cccccccc-cccc-cccc-cccc-cccccccccccc', NOW())
    ON CONFLICT (id) DO NOTHING;

-- 6) products
INSERT INTO p_product (id, store_id, name, price, description, use_ai_description, is_sold_out, is_hidden, created_at)
VALUES
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'dddddddd-dddd-dddd-dddd-dddddddddddd', '후라이드 치킨', 18000, '기본 후라이드', false, false, false, NOW()),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'dddddddd-dddd-dddd-dddd-dddddddddddd', '양념 치킨',     19000, '달콤 양념',     true,  false, false, NOW())
    ON CONFLICT (id) DO NOTHING;

-- 7) product options / items (Postman 테스트용: 양념 치킨)
INSERT INTO p_product_option (id, product_id, name, is_required, created_at)
VALUES
    ('10101010-1010-1010-1010-101010101010', 'ffffffff-ffff-ffff-ffff-ffffffffffff', '맵기', true, NOW())
    ON CONFLICT (id) DO NOTHING;

INSERT INTO p_product_option_item (id, product_option_id, name, additional_price, created_at)
VALUES
    ('30303030-3030-3030-3030-303030303030', '10101010-1010-1010-1010-101010101010', '순한맛', 0, NOW()),
    ('40404040-4040-4040-4040-404040404040', '10101010-1010-1010-1010-101010101010', '매운맛', 0, NOW())
    ON CONFLICT (id) DO NOTHING;

-- 8) default address (주문 테스트 대비)
INSERT INTO p_user_address (
    id, user_id, address_name, phone, address, detail_address,
    latitude, longitude, is_default, created_at
)
VALUES (
           'abababab-abab-abab-abab-abababababab',
           '11111111-1111-1111-1111-111111111111',
           '집',
           '010-1111-1111',
           '서울 종로구 집주소 1',
           '101호',
           37.5750000,
           126.9780000,
           true,
           NOW()
       )
    ON CONFLICT (id) DO NOTHING;
-- V4__seed_sample_data.sql
-- 샘플 데이터 (개발/테스트용 최소 세트)
-- ================================

-- 0) 고정 UUID를 쓰면 반복 실행/참조가 편해서 여기선 고정값 사용
-- (운영에서는 당연히 이렇게 안 넣음)

-- 1) users
INSERT INTO p_user (id, login_id, email, password, name, phone, status, created_at)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'minji_customer', 'customer@test.com', 'hashed_pw', '민지(고객)', '010-1111-1111', 'ACTIVE', CURRENT_TIMESTAMP),
    ('22222222-2222-2222-2222-222222222222', 'minji_owner',    'owner@test.com',    'hashed_pw', '민지(사장)', '010-2222-2222', 'ACTIVE', CURRENT_TIMESTAMP)
    ON CONFLICT (login_id) DO NOTHING;

-- 2) roles already seeded in V2, assign roles to users
-- role_id 조회해서 user_roles에 넣기 (role은 unique라 subquery 가능)
INSERT INTO p_user_roles (id, user_id, role_id, created_at)
VALUES
    (gen_random_uuid(), '11111111-1111-1111-1111-111111111111', (SELECT id FROM p_role WHERE role='CUSTOMER'), CURRENT_TIMESTAMP),
    (gen_random_uuid(), '22222222-2222-2222-2222-222222222222', (SELECT id FROM p_role WHERE role='OWNER'),    CURRENT_TIMESTAMP)
    ON CONFLICT (user_id, role_id) DO NOTHING;

-- 3) region (간단한 사각형 폴리곤 예시)
INSERT INTO p_region (id, name, geom, is_active, created_at)
VALUES (
           'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
           '광화문',
           ST_Multi(ST_GeomFromText('POLYGON((126.970 37.570, 126.990 37.570, 126.990 37.585, 126.970 37.585, 126.970 37.570))', 4326)),
           true,
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (name) DO NOTHING;

-- 4) category
INSERT INTO p_category (id, name, created_at)
VALUES
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '한식', CURRENT_TIMESTAMP),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', '치킨', CURRENT_TIMESTAMP)
    ON CONFLICT (name) DO NOTHING;

-- 5) store
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
           ST_GeomFromText('POINT(126.978 37.575)', 4326),
           '02-000-0000',
           'OPEN',
           20, 40, 2000, 12000,
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (id) DO NOTHING;

-- 6) store-category mapping
INSERT INTO p_store_category (id, store_id, category_id, created_at)
VALUES
    (gen_random_uuid(), 'dddddddd-dddd-dddd-dddd-dddddddddddd', 'cccccccc-cccc-cccc-cccc-cccccccccccc', CURRENT_TIMESTAMP)
    ON CONFLICT (store_id, category_id) DO NOTHING;

-- 7) products
INSERT INTO p_product (id, store_id, name, price, description, use_ai_description, is_sold_out, is_hidden, created_at)
VALUES
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'dddddddd-dddd-dddd-dddd-dddddddddddd', '후라이드 치킨', 18000, '기본 후라이드', false, false, false, CURRENT_TIMESTAMP),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'dddddddd-dddd-dddd-dddd-dddddddddddd', '양념 치킨',     19000, '달콤 양념',     true,  false, false, CURRENT_TIMESTAMP)
    ON CONFLICT (id) DO NOTHING;

-- 8) product options (맵기/소스)
INSERT INTO p_product_option (id, product_id, name, is_required, created_at)
VALUES
    ('10101010-1010-1010-1010-101010101010', 'ffffffff-ffff-ffff-ffff-ffffffffffff', '맵기', true, CURRENT_TIMESTAMP),
    ('20202020-2020-2020-2020-202020202020', 'ffffffff-ffff-ffff-ffff-ffffffffffff', '소스추가', false, CURRENT_TIMESTAMP)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO p_product_option_item (id, product_option_id, name, additional_price, created_at)
VALUES
    ('30303030-3030-3030-3030-303030303030', '10101010-1010-1010-1010-101010101010', '순한맛', 0, CURRENT_TIMESTAMP),
    ('40404040-4040-4040-4040-404040404040', '10101010-1010-1010-1010-101010101010', '매운맛', 0, CURRENT_TIMESTAMP),
    ('50505050-5050-5050-5050-505050505050', '20202020-2020-2020-2020-202020202020', '치즈소스', 500, CURRENT_TIMESTAMP)
    ON CONFLICT (id) DO NOTHING;

-- 9) address (default)
INSERT INTO p_user_address (
    id, user_id, address_name, phone, address, detail_address, latitude, longitude, is_default, created_at
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
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (id) DO NOTHING;

-- 10) cart + cart_item + cart_item_option
INSERT INTO p_cart (id, user_id, store_id, status, created_at)
VALUES (
           'cdcdcdcd-cdcd-cdcd-cdcd-cdcdcdcdcdcd',
           '11111111-1111-1111-1111-111111111111',
           'dddddddd-dddd-dddd-dddd-dddddddddddd',
           'ACTIVE',
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (id) DO NOTHING;

INSERT INTO p_cart_item (id, cart_id, product_id, quantity, created_at)
VALUES (
           'edededed-eded-eded-eded-edededededed',
           'cdcdcdcd-cdcd-cdcd-cdcd-cdcdcdcdcdcd',
           'ffffffff-ffff-ffff-ffff-ffffffffffff',
           1,
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (id) DO NOTHING;

INSERT INTO p_cart_item_option (id, cart_item_id, product_option_id, product_option_item_id, created_at)
VALUES
    (gen_random_uuid(), 'edededed-eded-eded-eded-edededededed', '10101010-1010-1010-1010-101010101010', '30303030-3030-3030-3030-303030303030', CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'edededed-eded-eded-eded-edededededed', '20202020-2020-2020-2020-202020202020', '50505050-5050-5050-5050-505050505050', CURRENT_TIMESTAMP)
    ON CONFLICT DO NOTHING;

-- 11) order + order_items + options (장바구니 -> 주문 전환 예시)
INSERT INTO p_order (
    id, user_id, store_id, delivery_address_id, order_no, status, total_amount, order_date, created_at
)
VALUES (
           'abab0000-0000-0000-0000-000000000000',
           '11111111-1111-1111-1111-111111111111',
           'dddddddd-dddd-dddd-dddd-dddddddddddd',
           'abababab-abab-abab-abab-abababababab',
           'ORD-20260227-0001',
           'PAID',
           19500,
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (order_no) DO NOTHING;

INSERT INTO p_order_item (
    id, order_id, product_id, product_name, unit_price, quantity, line_total_amount, created_at
)
VALUES (
           'bcbc0000-0000-0000-0000-000000000000',
           'abab0000-0000-0000-0000-000000000000',
           'ffffffff-ffff-ffff-ffff-ffffffffffff',
           '양념 치킨',
           19000,
           1,
           19500,
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (id) DO NOTHING;

INSERT INTO p_order_item_option (
    id, order_item_id, option_name, option_item_name, extra_price, created_at
)
VALUES
    (gen_random_uuid(), 'bcbc0000-0000-0000-0000-000000000000', '맵기', '순한맛', 0, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'bcbc0000-0000-0000-0000-000000000000', '소스추가', '치즈소스', 500, CURRENT_TIMESTAMP)
    ON CONFLICT DO NOTHING;

-- 12) payment + payment_log
INSERT INTO p_pg_provider (id, code, name, status, created_at)
VALUES ('99999999-9999-9999-9999-999999999999', 'TOSS', '토스페이먼츠', 'ACTIVE', CURRENT_TIMESTAMP)
    ON CONFLICT (code) DO NOTHING;

INSERT INTO p_payment (id, order_id, method, status, amount, paid_at, created_at)
VALUES (
           'cccc0000-0000-0000-0000-000000000000',
           'abab0000-0000-0000-0000-000000000000',
           'CARD',
           'PAID',
           19500,
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (id) DO NOTHING;

INSERT INTO p_payment_log (id, payment_id, pg_provider_id, status, pg_tid, reason, created_at)
VALUES (
           'dddd0000-0000-0000-0000-000000000000',
           'cccc0000-0000-0000-0000-000000000000',
           (SELECT id FROM p_pg_provider WHERE code='TOSS'),
           'SUCCESS',
           'TID-EXAMPLE-0001',
           NULL,
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (id) DO NOTHING;

-- 13) review + image
INSERT INTO p_review (id, order_id, user_id, store_id, rating, content, is_hidden, created_at)
VALUES (
           'eeee0000-0000-0000-0000-000000000000',
           'abab0000-0000-0000-0000-000000000000',
           '11111111-1111-1111-1111-111111111111',
           'dddddddd-dddd-dddd-dddd-dddddddddddd',
           5,
           '진짜 맛있어요! 다음에도 주문할게요.',
           false,
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (order_id) DO NOTHING;

INSERT INTO p_review_image (id, review_id, image_url, sort_order, created_at)
VALUES (
           'ffff0000-0000-0000-0000-000000000000',
           'eeee0000-0000-0000-0000-000000000000',
           'https://example.com/review/1.jpg',
           0,
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (id) DO NOTHING;

-- 14) ai logs (둘 다 유지)
INSERT INTO p_ai_log (id, product_id, request_text, response_text, ai_model, created_at, created_by)
VALUES (
           '12121212-1212-1212-1212-121212121212',
           'ffffffff-ffff-ffff-ffff-ffffffffffff',
           '맵기 조절 가능한 양념치킨 추천해줘',
           '이 양념치킨은 순한맛/매운맛 선택이 가능하며 치즈소스를 추가할 수 있어요.',
           'gpt-4o',
           CURRENT_TIMESTAMP,
           '22222222-2222-2222-2222-222222222222'
       )
    ON CONFLICT (id) DO NOTHING;

INSERT INTO p_product_ai_log (id, product_id, prompt, generated_text, model_name, created_at, created_by)
VALUES (
           '34343434-3434-3434-3434-343434343434',
           'ffffffff-ffff-ffff-ffff-ffffffffffff',
           '상품 설명을 2문장으로 매력적으로 작성해줘',
           '달콤한 양념과 바삭한 식감이 완벽한 조화를 이루는 양념 치킨! 취향에 따라 맵기를 고르고 치즈소스까지 추가해 더 풍성하게 즐겨보세요.',
           'gpt-4o',
           CURRENT_TIMESTAMP,
           '22222222-2222-2222-2222-222222222222'
       )
    ON CONFLICT (id) DO NOTHING;
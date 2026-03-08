-- V3__seed_sample_data.sql
-- 개발용 샘플 데이터
-- ============================================

-- 1. 사용자
INSERT INTO p_user (
    id, login_id, email, password, name, phone, status, created_at
) VALUES
      (
          '11111111-1111-1111-1111-111111111111',
          'customer01',
          'customer01@test.com',
          '$2a$10$sample.customer.password',
          '고객1',
          '010-1111-1111',
          'ACTIVE',
          CURRENT_TIMESTAMP
      ),
      (
          '22222222-2222-2222-2222-222222222222',
          'owner01',
          'owner01@test.com',
          '$2a$10$sample.owner.password',
          '사장1',
          '010-2222-2222',
          'ACTIVE',
          CURRENT_TIMESTAMP
      ),
      (
          '33333333-3333-3333-3333-333333333333',
          'admin01',
          'admin01@test.com',
          '$2a$10$sample.admin.password',
          '관리자1',
          '010-3333-3333',
          'ACTIVE',
          CURRENT_TIMESTAMP
      )
    ON CONFLICT (login_id) DO NOTHING;

-- 2. 사용자 역할 매핑
INSERT INTO p_user_roles (id, user_id, role_id, created_at)
SELECT gen_random_uuid(), '11111111-1111-1111-1111-111111111111', r.id, CURRENT_TIMESTAMP
FROM p_role r
WHERE r.role = 'CUSTOMER'
    ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO p_user_roles (id, user_id, role_id, created_at)
SELECT gen_random_uuid(), '22222222-2222-2222-2222-222222222222', r.id, CURRENT_TIMESTAMP
FROM p_role r
WHERE r.role = 'OWNER'
    ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO p_user_roles (id, user_id, role_id, created_at)
SELECT gen_random_uuid(), '33333333-3333-3333-3333-333333333333', r.id, CURRENT_TIMESTAMP
FROM p_role r
WHERE r.role = 'ADMIN'
    ON CONFLICT (user_id, role_id) DO NOTHING;

-- 3. 지역
INSERT INTO p_region (id, name, geom, is_active, created_at)
VALUES (
           '44444444-4444-4444-4444-444444444444',
           '강남구',
           ST_GeomFromText(
                   'MULTIPOLYGON(((127.00 37.49, 127.10 37.49, 127.10 37.55, 127.00 37.55, 127.00 37.49)))',
                   4326
           ),
           true,
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (name) DO NOTHING;

-- 4. 카테고리
INSERT INTO p_category (id, name, created_at)
VALUES
    ('55555555-5555-5555-5555-555555555551', '치킨', CURRENT_TIMESTAMP),
    ('55555555-5555-5555-5555-555555555552', '피자', CURRENT_TIMESTAMP),
    ('55555555-5555-5555-5555-555555555553', '분식', CURRENT_TIMESTAMP)
    ON CONFLICT (name) DO NOTHING;

-- 5. 매장
INSERT INTO p_store (
    id, user_id, region_id, name, description, address, location,
    phone, status, open_time, close_time,
    delivery_min_minutes, delivery_max_minutes, delivery_fee, minimum_order_amount,
    created_at
) VALUES (
             '66666666-6666-6666-6666-666666666666',
             '22222222-2222-2222-2222-222222222222',
             '44444444-4444-4444-4444-444444444444',
             '배고팡치킨 강남점',
             '개발용 샘플 매장',
             '서울 강남구 테헤란로 123',
             ST_GeomFromText('POINT(127.0276 37.4979)', 4326),
             '02-1234-5678',
             'OPEN',
             '10:00:00',
             '23:00:00',
             20,
             40,
             3000,
             15000,
             CURRENT_TIMESTAMP
         )
    ON CONFLICT DO NOTHING;

-- 6. 매장 카테고리
INSERT INTO p_store_category (id, store_id, category_id, created_at)
VALUES (
           gen_random_uuid(),
           '66666666-6666-6666-6666-666666666666',
           '55555555-5555-5555-5555-555555555551',
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (store_id, category_id) DO NOTHING;

-- 7. 상품
INSERT INTO p_product (
    id, store_id, name, price, description, use_ai_description,
    is_sold_out, is_hidden, created_at
) VALUES
      (
          '77777777-7777-7777-7777-777777777771',
          '66666666-6666-6666-6666-666666666666',
          '후라이드치킨',
          18000,
          '바삭한 후라이드치킨',
          false,
          false,
          false,
          CURRENT_TIMESTAMP
      ),
      (
          '77777777-7777-7777-7777-777777777772',
          '66666666-6666-6666-6666-666666666666',
          '양념치킨',
          19000,
          '달콤매콤 양념치킨',
          false,
          false,
          false,
          CURRENT_TIMESTAMP
      )
    ON CONFLICT DO NOTHING;

-- 8. 상품 옵션
INSERT INTO p_product_option (
    id, product_id, name, is_required, created_at
) VALUES (
             '88888888-8888-8888-8888-888888888881',
             '77777777-7777-7777-7777-777777777771',
             '사이즈',
             true,
             CURRENT_TIMESTAMP
         )
    ON CONFLICT DO NOTHING;

-- 9. 상품 옵션 아이템
INSERT INTO p_product_option_item (
    id, product_option_id, name, additional_price, created_at
) VALUES
      (
          '99999999-9999-9999-9999-999999999991',
          '88888888-8888-8888-8888-888888888881',
          '보통',
          0,
          CURRENT_TIMESTAMP
      ),
      (
          '99999999-9999-9999-9999-999999999992',
          '88888888-8888-8888-8888-888888888881',
          '대',
          2000,
          CURRENT_TIMESTAMP
      )
    ON CONFLICT DO NOTHING;

-- 10. 사용자 주소
INSERT INTO p_user_address (
    id, user_id, address_name, phone, address, detail_address,
    latitude, longitude, is_default, created_at
) VALUES (
             'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
             '11111111-1111-1111-1111-111111111111',
             '집',
             '010-1111-1111',
             '서울 강남구 역삼동 123-45',
             '101동 1001호',
             37.4979,
             127.0276,
             true,
             CURRENT_TIMESTAMP
         )
    ON CONFLICT DO NOTHING;

-- 11. 장바구니
INSERT INTO p_cart (
    id, user_id, store_id, status, created_at
) VALUES (
             'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
             '11111111-1111-1111-1111-111111111111',
             '66666666-6666-6666-6666-666666666666',
             'ACTIVE',
             CURRENT_TIMESTAMP
         )
    ON CONFLICT DO NOTHING;

-- 12. 장바구니 아이템
INSERT INTO p_cart_item (
    id, cart_id, product_id, quantity, created_at
) VALUES (
             'cccccccc-cccc-cccc-cccc-cccccccccccc',
             'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
             '77777777-7777-7777-7777-777777777771',
             2,
             CURRENT_TIMESTAMP
         )
    ON CONFLICT DO NOTHING;

-- 13. 장바구니 아이템 옵션
INSERT INTO p_cart_item_option (
    id, cart_item_id, product_option_id, product_option_item_id, created_at
) VALUES (
             gen_random_uuid(),
             'cccccccc-cccc-cccc-cccc-cccccccccccc',
             '88888888-8888-8888-8888-888888888881',
             '99999999-9999-9999-9999-999999999991',
             CURRENT_TIMESTAMP
         )
    ON CONFLICT DO NOTHING;

-- 14. 주문
INSERT INTO p_order (
    id, user_id, store_id, delivery_address_id, order_no, status,
    request_memo, total_amount, order_date, created_at
) VALUES (
             'dddddddd-dddd-dddd-dddd-dddddddddddd',
             '11111111-1111-1111-1111-111111111111',
             '66666666-6666-6666-6666-666666666666',
             'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
             'ORD-20260308-0001',
             'PENDING_PAYMENT',
             '문 앞에 놔주세요',
             36000,
             CURRENT_TIMESTAMP,
             CURRENT_TIMESTAMP
         )
    ON CONFLICT (order_no) DO NOTHING;

-- 15. 주문 아이템
INSERT INTO p_order_item (
    id, order_id, product_id, product_name, unit_price, quantity, line_total_amount, created_at
) VALUES (
             'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
             'dddddddd-dddd-dddd-dddd-dddddddddddd',
             '77777777-7777-7777-7777-777777777771',
             '후라이드치킨',
             18000,
             2,
             36000,
             CURRENT_TIMESTAMP
         )
    ON CONFLICT DO NOTHING;

-- 16. 주문 아이템 옵션
INSERT INTO p_order_item_option (
    id, order_item_id, option_name, option_item_name, extra_price, created_at
) VALUES (
             gen_random_uuid(),
             'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
             '사이즈',
             '보통',
             0,
             CURRENT_TIMESTAMP
         )
    ON CONFLICT DO NOTHING;

-- 17. 결제
INSERT INTO p_payment (
    id, order_id, status, amount, payment_key, paid_at, created_at
) VALUES (
             'ffffffff-ffff-ffff-ffff-ffffffffffff',
             'dddddddd-dddd-dddd-dddd-dddddddddddd',
             'READY',
             36000,
             NULL,
             NULL,
             CURRENT_TIMESTAMP
         )
    ON CONFLICT DO NOTHING;

-- 18. 결제 로그
-- INSERT INTO p_payment_log (
--     id, payment_id, payment_key, status, reason, created_at
-- ) VALUES (
--              gen_random_uuid(),
--              'ffffffff-ffff-ffff-ffff-ffffffffffff',
--              NULL,
--              'SUCCESS',
--              '초기 결제 생성 로그',
--              CURRENT_TIMESTAMP
--          )
--     ON CONFLICT DO NOTHING;

-- 19. PG 제공자
INSERT INTO p_pg_provider (
    id, code, name, status, created_at
) VALUES (
             gen_random_uuid(),
             'TOSS',
             '토스페이먼츠',
             'ACTIVE',
             CURRENT_TIMESTAMP
         )
    ON CONFLICT (code) DO NOTHING;
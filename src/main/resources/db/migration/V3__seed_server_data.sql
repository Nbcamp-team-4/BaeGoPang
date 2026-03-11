-- V3__seed_server_data.sql
-- 서버용 통합 seed 데이터
-- 기존 로컬용 V3/V4 내용을 통합
-- 기준 비밀번호 hash: test02.password
-- $2a$10$/jHxyXBDV7LDpZUcyokttO0Q4SOOyLYiOpE9MHy6O6HwTypDhXayy

-- =========================================================
-- 1. 사용자
-- =========================================================

INSERT INTO p_user (
    id, login_id, email, password, name, phone, status, refresh_token,
    created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
) VALUES
      (
          '11111111-1111-1111-1111-111111111111',
          'customer01',
          'customer01@test.com',
          '$2a$10$sample.customer.password',
          '고객1',
          '010-1111-1111',
          'ACTIVE',
          NULL,
          CURRENT_TIMESTAMP,
          NULL,
          CURRENT_TIMESTAMP,
          NULL,
          NULL,
          NULL
      ),
      (
          '22222222-2222-2222-2222-222222222222',
          'owner01',
          'owner01@test.com',
          '$2a$10$sample.owner.password',
          '사장1',
          '010-2222-2222',
          'ACTIVE',
          NULL,
          CURRENT_TIMESTAMP,
          NULL,
          CURRENT_TIMESTAMP,
          NULL,
          NULL,
          NULL
      ),
      (
          '33333333-3333-3333-3333-333333333333',
          'admin01',
          'admin01@test.com',
          '$2a$10$sample.admin.password',
          '관리자1',
          '010-3333-3333',
          'ACTIVE',
          NULL,
          CURRENT_TIMESTAMP,
          NULL,
          CURRENT_TIMESTAMP,
          NULL,
          NULL,
          NULL
      ),
      (
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          'owner11',
          'owner11@gmail.com',
          '$2a$10$/jHxyXBDV7LDpZUcyokttO0Q4SOOyLYiOpE9MHy6O6HwTypDhXayy',
          '오너',
          '010-1111-2222',
          'ACTIVE',
          NULL,
          TIMESTAMP '2026-03-10 23:49:56.67671',
          NULL,
          TIMESTAMP '2026-03-10 23:49:56.67671',
          NULL,
          NULL,
          NULL
      ),
      (
          'eb4a7a2b-60dd-4ae9-8199-0b69bd6ce465',
          '1234',
          'jfsadf@email.com',
          '$2a$10$Ahk2Np1kRTpC.AySOhojPu8nDZ3U4eUWpCGZ1E1bs5FoxT7IpaFm6',
          '4조',
          '010-2222-3333',
          'ACTIVE',
          'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0IiwidHlwZSI6IlJFRlJFU0giLCJleHAiOjE3NzQ0MDIzNjksInVzZXJJZCI6ImViNGE3YTJiLTYwZGQtNGFlOS04MTk5LTBiNjliZDZjZTQ2NSIsImlhdCI6MTc3MzE5Mjc2OX0.jrQWCFJNPth6Uf6w57Z0l4nsiTv2qrc5B9MfVfk9qoc',
          TIMESTAMP '2026-03-10 23:50:27.370535',
          NULL,
          TIMESTAMP '2026-03-11 01:32:49.046995',
          NULL,
          NULL,
          NULL
      ),
      (
          '18e12f49-528e-48dd-afb3-ec27784e15c2',
          'test02',
          'test22@gmail.com',
          '$2a$10$/jHxyXBDV7LDpZUcyokttO0Q4SOOyLYiOpE9MHy6O6HwTypDhXayy',
          '테스트유저',
          '010-2345-2222',
          'ACTIVE',
          'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MDIiLCJ0eXBlIjoiUkVGUkVTSCIsImV4cCI6MTc3NDQwMzAzNCwidXNlcklkIjoiMThlMTJmNDktNTI4ZS00OGRkLWFmYjMtZWMyNzc4NGUxNWMyIiwiaWF0IjoxNzczMTkzNDM0fQ.dltV4He6DuEGMAF8hGQa2WyIDji96SkFszABLztlpTI',
          TIMESTAMP '2026-03-10 22:57:35.841469',
          NULL,
          TIMESTAMP '2026-03-11 01:43:54.231532',
          NULL,
          NULL,
          NULL
      ),
      (
          '33645e6a-f0d4-4544-8e71-8e83d7636671',
          'clzls',
          'jjjj@email.com',
          '$2a$10$/jHxyXBDV7LDpZUcyokttO0Q4SOOyLYiOpE9MHy6O6HwTypDhXayy',
          '치킨',
          '010-2222-9999',
          'ACTIVE',
          'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjbHpscyIsInR5cGUiOiJSRUZSRVNIIiwiZXhwIjoxNzc0NDAzMzM2LCJ1c2VySWQiOiIzMzY0NWU2YS1mMGQ0LTQ1NDQtOGU3MS04ZTgzZDc2MzY2NzEiLCJpYXQiOjE3NzMxOTM3MzZ9.Syo98wNmZuapo79cP99lCcr2sfJmPjxqdWew6ZiDSnM',
          TIMESTAMP '2026-03-11 01:48:35.210216',
          NULL,
          TIMESTAMP '2026-03-11 01:48:56.011022',
          NULL,
          NULL,
          NULL
      ),
      (
          'f2020202-2020-4020-8020-202020202020',
          'store02',
          'store02@gmail.com',
          '$2a$10$/jHxyXBDV7LDpZUcyokttO0Q4SOOyLYiOpE9MHy6O6HwTypDhXayy',
          '스토어02',
          '010-2020-2020',
          'ACTIVE',
          NULL,
          CURRENT_TIMESTAMP,
          NULL,
          CURRENT_TIMESTAMP,
          NULL,
          NULL,
          NULL
      )
    ON CONFLICT (id) DO UPDATE
                            SET
                                login_id = EXCLUDED.login_id,
                            email = EXCLUDED.email,
                            password = EXCLUDED.password,
                            name = EXCLUDED.name,
                            phone = EXCLUDED.phone,
                            status = EXCLUDED.status,
                            refresh_token = EXCLUDED.refresh_token,
                            updated_at = CURRENT_TIMESTAMP,
                            deleted_at = NULL,
                            deleted_by = NULL;

-- =========================================================
-- 2. 사용자 역할 매핑
-- =========================================================

INSERT INTO p_user_roles (id, user_id, role_id, created_at)
SELECT gen_random_uuid(), '11111111-1111-1111-1111-111111111111', r.id, CURRENT_TIMESTAMP
FROM p_role r
WHERE r.type = 'ROLE_CUSTOMER'
    ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO p_user_roles (id, user_id, role_id, created_at)
SELECT gen_random_uuid(), '22222222-2222-2222-2222-222222222222', r.id, CURRENT_TIMESTAMP
FROM p_role r
WHERE r.type = 'ROLE_OWNER'
    ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO p_user_roles (id, user_id, role_id, created_at)
SELECT gen_random_uuid(), '33333333-3333-3333-3333-333333333333', r.id, CURRENT_TIMESTAMP
FROM p_role r
WHERE r.type = 'ROLE_ADMIN'
    ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO p_user_roles (id, user_id, role_id, created_at)
SELECT gen_random_uuid(), '18e12f49-528e-48dd-afb3-ec27784e15c2', r.id, CURRENT_TIMESTAMP
FROM p_role r
WHERE r.type = 'ROLE_CUSTOMER'
    ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO p_user_roles (id, user_id, role_id, created_at)
SELECT gen_random_uuid(), 'cd609fac-c9dd-45e0-b254-22b68b4cb634', r.id, CURRENT_TIMESTAMP
FROM p_role r
WHERE r.type = 'ROLE_OWNER'
    ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO p_user_roles (id, user_id, role_id, created_at)
SELECT gen_random_uuid(), 'eb4a7a2b-60dd-4ae9-8199-0b69bd6ce465', r.id, CURRENT_TIMESTAMP
FROM p_role r
WHERE r.type = 'ROLE_CUSTOMER'
    ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO p_user_roles (id, user_id, role_id, created_at)
SELECT gen_random_uuid(), '33645e6a-f0d4-4544-8e71-8e83d7636671', r.id, CURRENT_TIMESTAMP
FROM p_role r
WHERE r.type = 'ROLE_OWNER'
    ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO p_user_roles (id, user_id, role_id, created_at)
SELECT gen_random_uuid(), 'f2020202-2020-4020-8020-202020202020', r.id, CURRENT_TIMESTAMP
FROM p_role r
WHERE r.type = 'ROLE_OWNER'
    ON CONFLICT (user_id, role_id) DO NOTHING;

-- =========================================================
-- 3. 지역
-- =========================================================

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

-- =========================================================
-- 4. 카테고리
-- =========================================================

INSERT INTO p_category (id, name, created_at)
VALUES
    ('55555555-5555-5555-5555-555555555551', '치킨', CURRENT_TIMESTAMP),
    ('55555555-5555-5555-5555-555555555552', '피자', CURRENT_TIMESTAMP),
    ('55555555-5555-5555-5555-555555555553', '분식', CURRENT_TIMESTAMP)
    ON CONFLICT (name) DO NOTHING;

-- =========================================================
-- 5. 매장
-- =========================================================

INSERT INTO p_store (
    id, user_id, region_id, name, description, address, location,
    phone, image_url, open_time, close_time, status,
    delivery_min_minutes, delivery_max_minutes, delivery_fee, minimum_order_amount,
    average_rating, review_count, total_rating_sum,
    created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
) VALUES
      (
          '66666666-6666-6666-6666-666666666666',
          '22222222-2222-2222-2222-222222222222',
          '44444444-4444-4444-4444-444444444444',
          '배고팡치킨 강남점',
          '개발용 샘플 매장',
          '서울 강남구 테헤란로 123',
          ST_GeomFromText('POINT(127.0276 37.4979)', 4326),
          '02-1234-5678',
          NULL,
          '10:00:00',
          '23:00:00',
          'OPEN',
          20,
          40,
          3000,
          15000,
          0.0,
          0,
          0,
          CURRENT_TIMESTAMP,
          '22222222-2222-2222-2222-222222222222',
          CURRENT_TIMESTAMP,
          '22222222-2222-2222-2222-222222222222',
          NULL,
          NULL
      ),
      (
          '61111111-1111-1111-1111-111111111111',
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          '44444444-4444-4444-4444-444444444444',
          '스토어치킨 강남점',
          '후라이드와 양념치킨이 대표 메뉴인 테스트 매장',
          '서울 강남구 테헤란로 111',
          ST_GeomFromText('POINT(127.0276 37.4979)', 4326),
          '02-1111-1111',
          NULL,
          '10:00:00',
          '23:30:00',
          'OPEN',
          20,
          40,
          3000,
          15000,
          0.0,
          0,
          0,
          CURRENT_TIMESTAMP,
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          CURRENT_TIMESTAMP,
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          NULL,
          NULL
      ),
      (
          '62222222-2222-2222-2222-222222222222',
          '33645e6a-f0d4-4544-8e71-8e83d7636671',
          '44444444-4444-4444-4444-444444444444',
          '광화문떡볶이',
          '떡볶이와 분식 메뉴 테스트 매장',
          '서울 종로구 세종대로 222',
          ST_GeomFromText('POINT(127.0276 37.4979)', 4326),
          '02-2222-2222',
          NULL,
          '10:30:00',
          '22:30:00',
          'OPEN',
          15,
          35,
          2500,
          12000,
          0.0,
          0,
          0,
          CURRENT_TIMESTAMP,
          '33645e6a-f0d4-4544-8e71-8e83d7636671',
          CURRENT_TIMESTAMP,
          '33645e6a-f0d4-4544-8e71-8e83d7636671',
          NULL,
          NULL
      ),
      (
          '63333333-3333-3333-3333-333333333333',
          'f2020202-2020-4020-8020-202020202020',
          '44444444-4444-4444-4444-444444444444',
          'store02피자 강남점',
          '피자 대표 테스트 매장',
          '서울 강남구 테헤란로 202',
          ST_GeomFromText('POINT(127.0276 37.4979)', 4326),
          '02-2020-2020',
          NULL,
          '11:00:00',
          '23:00:00',
          'OPEN',
          25,
          45,
          3500,
          18000,
          0.0,
          0,
          0,
          CURRENT_TIMESTAMP,
          'f2020202-2020-4020-8020-202020202020',
          CURRENT_TIMESTAMP,
          'f2020202-2020-4020-8020-202020202020',
          NULL,
          NULL
      )
    ON CONFLICT (id) DO UPDATE
                            SET
                                user_id = EXCLUDED.user_id,
                            region_id = EXCLUDED.region_id,
                            name = EXCLUDED.name,
                            description = EXCLUDED.description,
                            address = EXCLUDED.address,
                            location = EXCLUDED.location,
                            phone = EXCLUDED.phone,
                            image_url = EXCLUDED.image_url,
                            open_time = EXCLUDED.open_time,
                            close_time = EXCLUDED.close_time,
                            status = EXCLUDED.status,
                            delivery_min_minutes = EXCLUDED.delivery_min_minutes,
                            delivery_max_minutes = EXCLUDED.delivery_max_minutes,
                            delivery_fee = EXCLUDED.delivery_fee,
                            minimum_order_amount = EXCLUDED.minimum_order_amount,
                            average_rating = EXCLUDED.average_rating,
                            review_count = EXCLUDED.review_count,
                            total_rating_sum = EXCLUDED.total_rating_sum,
                            updated_at = CURRENT_TIMESTAMP,
                            deleted_at = NULL,
                            deleted_by = NULL;

-- =========================================================
-- 6. 매장 카테고리
-- =========================================================

INSERT INTO p_store_category (id, store_id, category_id, created_at)
VALUES
    (gen_random_uuid(), '66666666-6666-6666-6666-666666666666', '55555555-5555-5555-5555-555555555551', CURRENT_TIMESTAMP),
    (gen_random_uuid(), '61111111-1111-1111-1111-111111111111', '55555555-5555-5555-5555-555555555551', CURRENT_TIMESTAMP),
    (gen_random_uuid(), '62222222-2222-2222-2222-222222222222', '55555555-5555-5555-5555-555555555553', CURRENT_TIMESTAMP),
    (gen_random_uuid(), '63333333-3333-3333-3333-333333333333', '55555555-5555-5555-5555-555555555552', CURRENT_TIMESTAMP)
    ON CONFLICT (store_id, category_id) DO NOTHING;

-- =========================================================
-- 7. 상품
-- =========================================================

INSERT INTO p_product (
    id, store_id, name, price, description, use_ai_description, image_url,
    is_sold_out, is_hidden,
    created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
) VALUES
      (
          '77777777-7777-7777-7777-777777777771',
          '66666666-6666-6666-6666-666666666666',
          '후라이드치킨',
          18000,
          '바삭한 후라이드치킨',
          false,
          NULL,
          false,
          false,
          CURRENT_TIMESTAMP,
          '22222222-2222-2222-2222-222222222222',
          CURRENT_TIMESTAMP,
          '22222222-2222-2222-2222-222222222222',
          NULL,
          NULL
      ),
      (
          '77777777-7777-7777-7777-777777777772',
          '66666666-6666-6666-6666-666666666666',
          '양념치킨',
          19000,
          '달콤매콤 양념치킨',
          false,
          NULL,
          false,
          false,
          CURRENT_TIMESTAMP,
          '22222222-2222-2222-2222-222222222222',
          CURRENT_TIMESTAMP,
          '22222222-2222-2222-2222-222222222222',
          NULL,
          NULL
      ),
      (
          '71111111-1111-1111-1111-111111111111',
          '61111111-1111-1111-1111-111111111111',
          '후라이드치킨',
          18000,
          '바삭한 기본 후라이드치킨',
          false,
          NULL,
          false,
          false,
          CURRENT_TIMESTAMP,
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          CURRENT_TIMESTAMP,
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          NULL,
          NULL
      ),
      (
          '71111111-1111-1111-1111-111111111112',
          '61111111-1111-1111-1111-111111111111',
          '양념치킨',
          19000,
          '달콤매콤한 양념치킨',
          false,
          NULL,
          false,
          false,
          CURRENT_TIMESTAMP,
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          CURRENT_TIMESTAMP,
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          NULL,
          NULL
      ),
      (
          '71111111-1111-1111-1111-111111111113',
          '61111111-1111-1111-1111-111111111111',
          '간장치킨',
          19500,
          '짭짤달콤 간장소스 치킨',
          false,
          NULL,
          false,
          false,
          CURRENT_TIMESTAMP,
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          CURRENT_TIMESTAMP,
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          NULL,
          NULL
      ),
      (
          '72222222-2222-2222-2222-222222222221',
          '62222222-2222-2222-2222-222222222222',
          '로제떡볶이',
          14500,
          '부드러운 로제소스 떡볶이',
          false,
          NULL,
          false,
          false,
          CURRENT_TIMESTAMP,
          '33645e6a-f0d4-4544-8e71-8e83d7636671',
          CURRENT_TIMESTAMP,
          '33645e6a-f0d4-4544-8e71-8e83d7636671',
          NULL,
          NULL
      ),
      (
          '72222222-2222-2222-2222-222222222222',
          '62222222-2222-2222-2222-222222222222',
          '치즈떡볶이',
          13500,
          '치즈가 올라간 매콤 떡볶이',
          false,
          NULL,
          false,
          false,
          CURRENT_TIMESTAMP,
          '33645e6a-f0d4-4544-8e71-8e83d7636671',
          CURRENT_TIMESTAMP,
          '33645e6a-f0d4-4544-8e71-8e83d7636671',
          NULL,
          NULL
      ),
      (
          '72222222-2222-2222-2222-222222222223',
          '62222222-2222-2222-2222-222222222222',
          '순대세트',
          12000,
          '떡볶이와 함께 먹기 좋은 순대세트',
          false,
          NULL,
          false,
          false,
          CURRENT_TIMESTAMP,
          '33645e6a-f0d4-4544-8e71-8e83d7636671',
          CURRENT_TIMESTAMP,
          '33645e6a-f0d4-4544-8e71-8e83d7636671',
          NULL,
          NULL
      ),
      (
          '73333333-3333-3333-3333-333333333331',
          '63333333-3333-3333-3333-333333333333',
          '페퍼로니피자',
          21900,
          '기본에 충실한 페퍼로니 피자',
          false,
          NULL,
          false,
          false,
          CURRENT_TIMESTAMP,
          'f2020202-2020-4020-8020-202020202020',
          CURRENT_TIMESTAMP,
          'f2020202-2020-4020-8020-202020202020',
          NULL,
          NULL
      ),
      (
          '73333333-3333-3333-3333-333333333332',
          '63333333-3333-3333-3333-333333333333',
          '치즈피자',
          19900,
          '치즈 풍미가 진한 클래식 피자',
          false,
          NULL,
          false,
          false,
          CURRENT_TIMESTAMP,
          'f2020202-2020-4020-8020-202020202020',
          CURRENT_TIMESTAMP,
          'f2020202-2020-4020-8020-202020202020',
          NULL,
          NULL
      ),
      (
          '73333333-3333-3333-3333-333333333333',
          '63333333-3333-3333-3333-333333333333',
          '불고기피자',
          22900,
          '달콤한 불고기 토핑 피자',
          false,
          NULL,
          false,
          false,
          CURRENT_TIMESTAMP,
          'f2020202-2020-4020-8020-202020202020',
          CURRENT_TIMESTAMP,
          'f2020202-2020-4020-8020-202020202020',
          NULL,
          NULL
      )
    ON CONFLICT (id) DO UPDATE
                            SET
                                store_id = EXCLUDED.store_id,
                            name = EXCLUDED.name,
                            price = EXCLUDED.price,
                            description = EXCLUDED.description,
                            use_ai_description = EXCLUDED.use_ai_description,
                            image_url = EXCLUDED.image_url,
                            is_sold_out = EXCLUDED.is_sold_out,
                            is_hidden = EXCLUDED.is_hidden,
                            updated_at = CURRENT_TIMESTAMP,
                            deleted_at = NULL,
                            deleted_by = NULL;

-- =========================================================
-- 8. 상품 옵션
-- =========================================================

INSERT INTO p_product_option (
    id, product_id, name, is_required,
    created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
) VALUES
      (
          '88888888-8888-8888-8888-888888888881',
          '77777777-7777-7777-7777-777777777771',
          '사이즈',
          true,
          CURRENT_TIMESTAMP,
          '22222222-2222-2222-2222-222222222222',
          CURRENT_TIMESTAMP,
          '22222222-2222-2222-2222-222222222222',
          NULL,
          NULL
      ),
      (
          '81111111-1111-1111-1111-111111111111',
          '71111111-1111-1111-1111-111111111111',
          '사이즈',
          true,
          CURRENT_TIMESTAMP,
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          CURRENT_TIMESTAMP,
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          NULL,
          NULL
      ),
      (
          '83333333-3333-3333-3333-333333333333',
          '73333333-3333-3333-3333-333333333331',
          '도우 선택',
          true,
          CURRENT_TIMESTAMP,
          'f2020202-2020-4020-8020-202020202020',
          CURRENT_TIMESTAMP,
          'f2020202-2020-4020-8020-202020202020',
          NULL,
          NULL
      )
    ON CONFLICT (id) DO UPDATE
                            SET
                                product_id = EXCLUDED.product_id,
                            name = EXCLUDED.name,
                            is_required = EXCLUDED.is_required,
                            updated_at = CURRENT_TIMESTAMP,
                            deleted_at = NULL,
                            deleted_by = NULL;

-- =========================================================
-- 9. 상품 옵션 아이템
-- =========================================================

INSERT INTO p_product_option_item (
    id, product_option_id, name, additional_price,
    created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
) VALUES
      (
          '99999999-9999-9999-9999-999999999991',
          '88888888-8888-8888-8888-888888888881',
          '보통',
          0,
          CURRENT_TIMESTAMP,
          '22222222-2222-2222-2222-222222222222',
          CURRENT_TIMESTAMP,
          '22222222-2222-2222-2222-222222222222',
          NULL,
          NULL
      ),
      (
          '99999999-9999-9999-9999-999999999992',
          '88888888-8888-8888-8888-888888888881',
          '대',
          2000,
          CURRENT_TIMESTAMP,
          '22222222-2222-2222-2222-222222222222',
          CURRENT_TIMESTAMP,
          '22222222-2222-2222-2222-222222222222',
          NULL,
          NULL
      ),
      (
          '91111111-1111-1111-1111-111111111111',
          '81111111-1111-1111-1111-111111111111',
          '보통',
          0,
          CURRENT_TIMESTAMP,
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          CURRENT_TIMESTAMP,
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          NULL,
          NULL
      ),
      (
          '91111111-1111-1111-1111-111111111112',
          '81111111-1111-1111-1111-111111111111',
          '대',
          2000,
          CURRENT_TIMESTAMP,
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          CURRENT_TIMESTAMP,
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          NULL,
          NULL
      ),
      (
          '93333333-3333-3333-3333-333333333331',
          '83333333-3333-3333-3333-333333333333',
          '씬도우',
          0,
          CURRENT_TIMESTAMP,
          'f2020202-2020-4020-8020-202020202020',
          CURRENT_TIMESTAMP,
          'f2020202-2020-4020-8020-202020202020',
          NULL,
          NULL
      ),
      (
          '93333333-3333-3333-3333-333333333332',
          '83333333-3333-3333-3333-333333333333',
          '오리지널',
          1000,
          CURRENT_TIMESTAMP,
          'f2020202-2020-4020-8020-202020202020',
          CURRENT_TIMESTAMP,
          'f2020202-2020-4020-8020-202020202020',
          NULL,
          NULL
      )
    ON CONFLICT (id) DO UPDATE
                            SET
                                product_option_id = EXCLUDED.product_option_id,
                            name = EXCLUDED.name,
                            additional_price = EXCLUDED.additional_price,
                            updated_at = CURRENT_TIMESTAMP,
                            deleted_at = NULL,
                            deleted_by = NULL;

-- =========================================================
-- 10. 사용자 주소
-- =========================================================

INSERT INTO p_user_address (
    id, user_id, address_name, phone, address, detail_address,
    latitude, longitude, is_default,
    created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
) VALUES
      (
          'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
          '11111111-1111-1111-1111-111111111111',
          '집',
          '010-1111-1111',
          '서울 강남구 역삼동 123-45',
          '101동 1001호',
          37.4979,
          127.0276,
          true,
          CURRENT_TIMESTAMP,
          NULL,
          CURRENT_TIMESTAMP,
          NULL,
          NULL,
          NULL
      ),
      (
          '8d3016de-cad5-4314-8df5-90e6d00e1c0c',
          '18e12f49-528e-48dd-afb3-ec27784e15c2',
          '집',
          '010-2345-2222',
          '서울 광화문',
          '문 앞',
          37.4979000,
          127.0276000,
          true,
          CURRENT_TIMESTAMP,
          NULL,
          CURRENT_TIMESTAMP,
          NULL,
          NULL,
          NULL
      ),
      (
          'ede6ed52-6f8a-480e-a13d-8df8dbfaa85a',
          'cd609fac-c9dd-45e0-b254-22b68b4cb634',
          '집',
          '010-1111-2222',
          '강남구',
          '101호',
          37.4979000,
          127.0276000,
          true,
          CURRENT_TIMESTAMP,
          NULL,
          CURRENT_TIMESTAMP,
          NULL,
          NULL,
          NULL
      ),
      (
          '1fa9da99-133a-42ce-afef-7cb5202d5055',
          'eb4a7a2b-60dd-4ae9-8199-0b69bd6ce465',
          '집',
          '010-2222-3333',
          '서울 강남구',
          '3030호',
          37.4979000,
          127.0276000,
          true,
          CURRENT_TIMESTAMP,
          NULL,
          CURRENT_TIMESTAMP,
          NULL,
          NULL,
          NULL
      ),
      (
          'cc19e2e4-8e11-456c-9818-b37fb3a6459f',
          '33645e6a-f0d4-4544-8e71-8e83d7636671',
          '매장',
          '020-2020-2020',
          '서울 광화문',
          '101',
          37.4979000,
          127.0276000,
          true,
          CURRENT_TIMESTAMP,
          NULL,
          CURRENT_TIMESTAMP,
          NULL,
          NULL,
          NULL
      ),
      (
          'b2020202-2020-4020-8020-202020202020',
          'f2020202-2020-4020-8020-202020202020',
          '매장',
          '010-2020-2020',
          '서울 강남구 테헤란로 202',
          '202호',
          37.4979000,
          127.0276000,
          true,
          CURRENT_TIMESTAMP,
          NULL,
          CURRENT_TIMESTAMP,
          NULL,
          NULL,
          NULL
      )
    ON CONFLICT (id) DO UPDATE
                            SET
                                user_id = EXCLUDED.user_id,
                            address_name = EXCLUDED.address_name,
                            phone = EXCLUDED.phone,
                            address = EXCLUDED.address,
                            detail_address = EXCLUDED.detail_address,
                            latitude = EXCLUDED.latitude,
                            longitude = EXCLUDED.longitude,
                            is_default = EXCLUDED.is_default,
                            updated_at = CURRENT_TIMESTAMP,
                            deleted_at = NULL,
                            deleted_by = NULL;

-- =========================================================
-- 11. 장바구니
-- =========================================================

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

-- =========================================================
-- 12. 장바구니 아이템
-- =========================================================

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

-- =========================================================
-- 13. 장바구니 아이템 옵션
-- =========================================================

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

-- =========================================================
-- 14. 주문
-- =========================================================

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

-- =========================================================
-- 15. 주문 아이템
-- =========================================================

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

-- =========================================================
-- 16. 주문 아이템 옵션
-- =========================================================

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

-- =========================================================
-- 17. 결제
-- =========================================================

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

-- =========================================================
-- 18. PG 제공자
-- =========================================================

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
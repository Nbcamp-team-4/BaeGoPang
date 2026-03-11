-- V4__seed_store_test_data.sql
-- 현재 서버 기준 user / role / address seed + owner store/menu seed
-- 기준 비밀번호 hash: test02.password
-- $2a$10$/jHxyXBDV7LDpZUcyokttO0Q4SOOyLYiOpE9MHy6O6HwTypDhXayy

-- =========================================================
-- 0. 공통 기준값
-- =========================================================

-- test02 customer
-- 18e12f49-528e-48dd-afb3-ec27784e15c2

-- 공통 좌표
-- latitude  = 37.4979
-- longitude = 127.0276

-- =========================================================
-- 1. p_user
-- 현재 서버 기준 user seed
-- owner 계정들은 test02와 동일한 password hash로 맞춤
-- =========================================================

INSERT INTO p_user (
    id, login_id, email, password, name, phone, status, refresh_token,
    created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
) VALUES
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
-- 2. p_user_roles
-- role_id 하드코딩 대신 p_role.type 으로 조회
-- =========================================================

-- test02 -> ROLE_CUSTOMER
INSERT INTO p_user_roles (
    id, user_id, role_id, created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
)
SELECT
    'ceab47cd-65a6-4699-81d7-c22cc8688dc2',
    '18e12f49-528e-48dd-afb3-ec27784e15c2',
    r.id,
    TIMESTAMP '2026-03-10 22:57:35.882033',
    NULL,
    TIMESTAMP '2026-03-10 22:57:35.882033',
    NULL,
    NULL,
    NULL
FROM p_role r
WHERE r.type = 'ROLE_CUSTOMER'
    ON CONFLICT (user_id, role_id) DO NOTHING;

-- owner11 -> ROLE_OWNER
INSERT INTO p_user_roles (
    id, user_id, role_id, created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
)
SELECT
    'd45e6332-0d58-4af2-b5d4-462381fb27c3',
    'cd609fac-c9dd-45e0-b254-22b68b4cb634',
    r.id,
    TIMESTAMP '2026-03-10 23:49:56.694872',
    NULL,
    TIMESTAMP '2026-03-10 23:49:56.694872',
    NULL,
    NULL,
    NULL
FROM p_role r
WHERE r.type = 'ROLE_OWNER'
    ON CONFLICT (user_id, role_id) DO NOTHING;

-- 1234 -> ROLE_CUSTOMER
INSERT INTO p_user_roles (
    id, user_id, role_id, created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
)
SELECT
    '1bbcdab7-f69e-4095-a877-e322a1eabb43',
    'eb4a7a2b-60dd-4ae9-8199-0b69bd6ce465',
    r.id,
    TIMESTAMP '2026-03-10 23:50:27.371388',
    NULL,
    TIMESTAMP '2026-03-10 23:50:27.371388',
    NULL,
    NULL,
    NULL
FROM p_role r
WHERE r.type = 'ROLE_CUSTOMER'
    ON CONFLICT (user_id, role_id) DO NOTHING;

-- clzls -> ROLE_OWNER
INSERT INTO p_user_roles (
    id, user_id, role_id, created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
)
SELECT
    'e947c64e-bd13-4bd0-ac6e-dd74f998efe6',
    '33645e6a-f0d4-4544-8e71-8e83d7636671',
    r.id,
    TIMESTAMP '2026-03-11 01:48:35.210831',
    NULL,
    TIMESTAMP '2026-03-11 01:48:35.210831',
    NULL,
    NULL,
    NULL
FROM p_role r
WHERE r.type = 'ROLE_OWNER'
    ON CONFLICT (user_id, role_id) DO NOTHING;

-- store02 -> ROLE_OWNER
INSERT INTO p_user_roles (
    id, user_id, role_id, created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
)
SELECT
    'a2020202-2020-4020-8020-202020202020',
    'f2020202-2020-4020-8020-202020202020',
    r.id,
    CURRENT_TIMESTAMP,
    NULL,
    CURRENT_TIMESTAMP,
    NULL,
    NULL,
    NULL
FROM p_role r
WHERE r.type = 'ROLE_OWNER'
    ON CONFLICT (user_id, role_id) DO NOTHING;

-- =========================================================
-- 3. p_user_address
-- 모든 좌표를 동일하게 맞춤
-- =========================================================

INSERT INTO p_user_address (
    id, user_id, address_name, phone, address, detail_address,
    latitude, longitude, is_default,
    created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
) VALUES
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
          TIMESTAMP '2026-03-10 22:57:35.947974',
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
          TIMESTAMP '2026-03-10 23:49:56.745737',
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
          TIMESTAMP '2026-03-10 23:50:27.376096',
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
          TIMESTAMP '2026-03-11 01:48:35.213401',
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
-- 4. 지역 / 카테고리
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

INSERT INTO p_category (id, name, created_at)
VALUES
    ('55555555-5555-5555-5555-555555555551', '치킨', CURRENT_TIMESTAMP),
    ('55555555-5555-5555-5555-555555555552', '피자', CURRENT_TIMESTAMP),
    ('55555555-5555-5555-5555-555555555553', '분식', CURRENT_TIMESTAMP)
    ON CONFLICT (name) DO NOTHING;

-- =========================================================
-- 5. 가게 생성
-- V1 기준 p_store 컬럼 구조 반영
-- average_rating / review_count / total_rating_sum 사용
-- =========================================================

INSERT INTO p_store (
    id, user_id, region_id, name, description, address, location,
    phone, image_url, open_time, close_time, status,
    delivery_min_minutes, delivery_max_minutes, delivery_fee, minimum_order_amount,
    average_rating, review_count, total_rating_sum,
    created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
) VALUES
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
-- 6. 매장 카테고리 연결
-- =========================================================

INSERT INTO p_store_category (id, store_id, category_id, created_at)
SELECT gen_random_uuid(), '61111111-1111-1111-1111-111111111111', '55555555-5555-5555-5555-555555555551', CURRENT_TIMESTAMP
    WHERE NOT EXISTS (
    SELECT 1
    FROM p_store_category
    WHERE store_id = '61111111-1111-1111-1111-111111111111'
      AND category_id = '55555555-5555-5555-5555-555555555551'
);

INSERT INTO p_store_category (id, store_id, category_id, created_at)
SELECT gen_random_uuid(), '62222222-2222-2222-2222-222222222222', '55555555-5555-5555-5555-555555555553', CURRENT_TIMESTAMP
    WHERE NOT EXISTS (
    SELECT 1
    FROM p_store_category
    WHERE store_id = '62222222-2222-2222-2222-222222222222'
      AND category_id = '55555555-5555-5555-5555-555555555553'
);

INSERT INTO p_store_category (id, store_id, category_id, created_at)
SELECT gen_random_uuid(), '63333333-3333-3333-3333-333333333333', '55555555-5555-5555-5555-555555555552', CURRENT_TIMESTAMP
    WHERE NOT EXISTS (
    SELECT 1
    FROM p_store_category
    WHERE store_id = '63333333-3333-3333-3333-333333333333'
      AND category_id = '55555555-5555-5555-5555-555555555552'
);

-- =========================================================
-- 7. 메뉴 생성
-- 리스트/상세가 어느 정도 보이도록 매장별 3개씩
-- =========================================================

INSERT INTO p_product (
    id, store_id, name, price, description, use_ai_description, is_sold_out, is_hidden,
    created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
) VALUES
      (
          '71111111-1111-1111-1111-111111111111',
          '61111111-1111-1111-1111-111111111111',
          '후라이드치킨',
          18000,
          '바삭한 기본 후라이드치킨',
          false,
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
                            is_sold_out = false,
                            is_hidden = false,
                            updated_at = CURRENT_TIMESTAMP,
                            deleted_at = NULL,
                            deleted_by = NULL;

-- =========================================================
-- 8. 옵션 그룹 / 옵션 아이템
-- 상세 화면용 최소 옵션
-- =========================================================

INSERT INTO p_product_option (
    id, product_id, name, is_required,
    created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
) VALUES
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

INSERT INTO p_product_option_item (
    id, product_option_id, name, additional_price,
    created_at, created_by, updated_at, updated_by, deleted_at, deleted_by
) VALUES
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
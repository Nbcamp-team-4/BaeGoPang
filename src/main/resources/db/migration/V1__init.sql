-- V1__init.sql
-- PostgreSQL + Flyway 초기 스키마
-- =====================================================

-- 1) Extensions
-- -----------------------------------------------------
CREATE EXTENSION IF NOT EXISTS "pgcrypto";   -- gen_random_uuid()
CREATE EXTENSION IF NOT EXISTS "postgis";    -- geometry(Point/MultiPolygon)

-- 2) ENUM Types
-- -----------------------------------------------------
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'user_status') THEN
CREATE TYPE user_status AS ENUM ('ACTIVE','BLOCKED','WITHDRAWN','DELETED');
END IF;

  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'role_type') THEN
CREATE TYPE role_type AS ENUM ('CUSTOMER','OWNER','MANAGER','ADMIN');
END IF;

  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'store_status') THEN
CREATE TYPE store_status AS ENUM ('OPEN','CLOSED','INACTIVE');
END IF;

  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'order_status') THEN
CREATE TYPE order_status AS ENUM ('PENDING','PAID','CANCELED','COMPLETED');
END IF;

  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'cart_status') THEN
CREATE TYPE cart_status AS ENUM ('ACTIVE','ORDERED','ABANDONED');
END IF;

  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'payment_status') THEN
CREATE TYPE payment_status AS ENUM ('READY','PAID','CANCELED');
END IF;

  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'pg_provider_status') THEN
CREATE TYPE pg_provider_status AS ENUM ('ACTIVE','DEACTIVE');
END IF;

  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'pg_log_status') THEN
CREATE TYPE pg_log_status AS ENUM ('SUCCESS','FAIL');
END IF;
END $$;

-- 3) Tables
-- -----------------------------------------------------

-- =======================
-- p_user
-- =======================
CREATE TABLE IF NOT EXISTS p_user (
                                      id           uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    login_id     varchar(50)  NOT NULL UNIQUE,
    email        varchar(255) NOT NULL UNIQUE,
    password     varchar(255) NOT NULL,
    name         varchar(100) NOT NULL,
    phone        varchar(20)  NOT NULL UNIQUE,
    status       user_status  NOT NULL DEFAULT 'ACTIVE',

    created_at   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   uuid         NULL,
    updated_at   timestamp    NULL,
    updated_by   uuid         NULL,
    deleted_at   timestamp    NULL,
    deleted_by   uuid         NULL
    );

ALTER TABLE p_user
    ADD CONSTRAINT fk_user_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_user_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_user_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

-- =======================
-- p_role
-- =======================
CREATE TABLE IF NOT EXISTS p_role (
                                      id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    role        role_type NOT NULL UNIQUE,

    created_at  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  uuid NULL,
    updated_at  timestamp NULL,
    updated_by  uuid NULL,
    deleted_at  timestamp NULL,
    deleted_by  uuid NULL
    );

ALTER TABLE p_role
    ADD CONSTRAINT fk_role_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_role_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_role_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

-- =======================
-- p_user_roles (M:N)
-- =======================
CREATE TABLE IF NOT EXISTS p_user_roles (
                                            id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    uuid NOT NULL,
    role_id    uuid NOT NULL,

    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NULL,
    updated_at timestamp NULL,
    updated_by uuid NULL,
    deleted_at timestamp NULL,
    deleted_by uuid NULL,

    CONSTRAINT uq_user_roles UNIQUE (user_id, role_id)
    );

ALTER TABLE p_user_roles
    ADD CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES p_user(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES p_role(id) ON DELETE RESTRICT,
  ADD CONSTRAINT fk_user_roles_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_user_roles_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_user_roles_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_user_roles_user_id ON p_user_roles(user_id);
CREATE INDEX IF NOT EXISTS ix_user_roles_role_id ON p_user_roles(role_id);

-- =======================
-- p_region
-- =======================
CREATE TABLE IF NOT EXISTS p_region (
                                        id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name       varchar(50) NOT NULL UNIQUE,
    geom       geometry(MultiPolygon, 4326) NOT NULL,
    is_active  boolean NOT NULL DEFAULT true,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- =======================
-- p_category
-- =======================
CREATE TABLE IF NOT EXISTS p_category (
                                          id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name       varchar(30) NOT NULL UNIQUE,

    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NULL,
    updated_at timestamp NULL,
    updated_by uuid NULL,
    deleted_at timestamp NULL,
    deleted_by uuid NULL
    );

ALTER TABLE p_category
    ADD CONSTRAINT fk_category_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_category_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_category_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

-- =======================
-- p_store
-- =======================
CREATE TABLE IF NOT EXISTS p_store (
                                       id                    uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id               uuid NULL,
    region_id             uuid NOT NULL,

    name                  varchar(100) NOT NULL,
    description           text NULL,
    address               varchar(255) NOT NULL,
    location              geometry(Point, 4326) NOT NULL,

    phone                 varchar(20) NULL,
    image_url             varchar(500) NULL,

    open_time             time NULL,
    close_time            time NULL,

    status                store_status NOT NULL DEFAULT 'OPEN',

    delivery_min_minutes  int NOT NULL,
    delivery_max_minutes  int NOT NULL,
    delivery_fee          int NOT NULL DEFAULT 0,
    minimum_order_amount  int NOT NULL DEFAULT 0,

    created_at            timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by            uuid NULL,
    updated_at            timestamp NULL,
    updated_by            uuid NULL,
    deleted_at            timestamp NULL,
    deleted_by            uuid NULL
    );

ALTER TABLE p_store
    ADD CONSTRAINT fk_store_user FOREIGN KEY (user_id) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_store_region FOREIGN KEY (region_id) REFERENCES p_region(id) ON DELETE RESTRICT,
  ADD CONSTRAINT fk_store_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_store_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_store_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_store_region_id ON p_store(region_id);
CREATE INDEX IF NOT EXISTS ix_store_user_id ON p_store(user_id);

-- =======================
-- p_store_category
-- =======================
CREATE TABLE IF NOT EXISTS p_store_category (
                                                id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    store_id    uuid NOT NULL,
    category_id uuid NOT NULL,

    created_at  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  uuid NULL,
    updated_at  timestamp NULL,
    updated_by  uuid NULL,
    deleted_at  timestamp NULL,
    deleted_by  uuid NULL,

    CONSTRAINT uq_store_category UNIQUE (store_id, category_id)
    );

ALTER TABLE p_store_category
    ADD CONSTRAINT fk_store_category_store FOREIGN KEY (store_id) REFERENCES p_store(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_store_category_category FOREIGN KEY (category_id) REFERENCES p_category(id) ON DELETE RESTRICT,
  ADD CONSTRAINT fk_store_category_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_store_category_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_store_category_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_store_category_store_id ON p_store_category(store_id);
CREATE INDEX IF NOT EXISTS ix_store_category_category_id ON p_store_category(category_id);

-- =======================
-- p_product
-- =======================
CREATE TABLE IF NOT EXISTS p_product (
                                         id                 uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    store_id            uuid NOT NULL,

    name               varchar(200) NOT NULL,
    price              int NOT NULL,
    description        text NULL,
    use_ai_description boolean NOT NULL DEFAULT false,
    image_url          varchar(500) NULL,

    is_sold_out         boolean NOT NULL DEFAULT false,
    is_hidden           boolean NOT NULL DEFAULT false,

    created_at         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by         uuid NULL,
    updated_at         timestamp NULL,
    updated_by         uuid NULL,
    deleted_at         timestamp NULL,
    deleted_by         uuid NULL
    );

ALTER TABLE p_product
    ADD CONSTRAINT fk_product_store FOREIGN KEY (store_id) REFERENCES p_store(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_product_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_product_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_product_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_product_store_id ON p_product(store_id);

-- =======================
-- p_product_option
-- =======================
CREATE TABLE IF NOT EXISTS p_product_option (
                                                id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id  uuid NOT NULL,

    name        varchar(100) NOT NULL,
    is_required boolean NOT NULL DEFAULT false,

    created_at  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  uuid NULL,
    updated_at  timestamp NULL,
    updated_by  uuid NULL,
    deleted_at  timestamp NULL,
    deleted_by  uuid NULL
    );

ALTER TABLE p_product_option
    ADD CONSTRAINT fk_product_option_product FOREIGN KEY (product_id) REFERENCES p_product(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_product_option_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_product_option_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_product_option_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_product_option_product_id ON p_product_option(product_id);

-- =======================
-- p_product_option_item
-- =======================
CREATE TABLE IF NOT EXISTS p_product_option_item (
                                                     id                uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    product_option_id uuid NOT NULL,

    name              varchar(100) NOT NULL,
    additional_price  int NOT NULL DEFAULT 0,

    created_at        timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by        uuid NULL,
    updated_at        timestamp NULL,
    updated_by        uuid NULL,
    deleted_at        timestamp NULL,
    deleted_by        uuid NULL
    );

ALTER TABLE p_product_option_item
    ADD CONSTRAINT fk_option_item_option FOREIGN KEY (product_option_id) REFERENCES p_product_option(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_option_item_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_option_item_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_option_item_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_option_item_option_id ON p_product_option_item(product_option_id);

-- =======================
-- p_ai_log (유지: p_product_ai_log와 통합 안 함)
-- =======================
CREATE TABLE IF NOT EXISTS p_ai_log (
                                        id           uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id   uuid NOT NULL,

    request_text  text NOT NULL,
    response_text text NOT NULL,
    ai_model      varchar(100) NOT NULL,

    created_at   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   uuid NULL,
    updated_at   timestamp NULL,
    updated_by   uuid NULL,
    deleted_at   timestamp NULL,
    deleted_by   uuid NULL
    );

ALTER TABLE p_ai_log
    ADD CONSTRAINT fk_ai_log_product FOREIGN KEY (product_id) REFERENCES p_product(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_ai_log_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_ai_log_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_ai_log_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_ai_log_product_id ON p_ai_log(product_id);
CREATE INDEX IF NOT EXISTS ix_ai_log_created_at ON p_ai_log(created_at DESC);

-- =======================
-- p_product_ai_log  (p_ai_log 대체로 쓰는 걸 추천)
-- =======================
CREATE TABLE IF NOT EXISTS p_product_ai_log (
                                                id             uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id     uuid NOT NULL,

    prompt         text NOT NULL,
    generated_text text NOT NULL,
    model_name     varchar(50) NOT NULL,

    created_at     timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     uuid NULL
    );

ALTER TABLE p_product_ai_log
    ADD CONSTRAINT fk_product_ai_log_product FOREIGN KEY (product_id) REFERENCES p_product(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_product_ai_log_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_product_ai_log_product_id ON p_product_ai_log(product_id);

-- =======================
-- p_user_address
-- =======================
CREATE TABLE IF NOT EXISTS p_user_address (
                                              id             uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         uuid NOT NULL,

    address_name   varchar(50) NOT NULL,
    phone          varchar(20) NOT NULL,
    address        varchar(255) NOT NULL,
    detail_address varchar(255) NULL,

    latitude       decimal(10,7) NULL,
    longitude      decimal(10,7) NULL,

    is_default     boolean NOT NULL DEFAULT false,

    created_at     timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     uuid NULL,
    updated_at     timestamp NULL,
    updated_by     uuid NULL,
    deleted_at     timestamp NULL,
    deleted_by     uuid NULL
    );

ALTER TABLE p_user_address
    ADD CONSTRAINT fk_user_address_user FOREIGN KEY (user_id) REFERENCES p_user(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_user_address_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_user_address_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_user_address_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_user_address_user_id ON p_user_address(user_id);

-- Postgres partial unique index: 유저당 기본주소는 1개만
CREATE UNIQUE INDEX IF NOT EXISTS uq_user_address_default_per_user
    ON p_user_address(user_id)
    WHERE is_default = true AND deleted_at IS NULL;

-- =======================
-- p_order
-- =======================
CREATE TABLE IF NOT EXISTS p_order (
                                       id                  uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             uuid NOT NULL,
    store_id            uuid NOT NULL,
    delivery_address_id uuid NULL,

    order_no            varchar(50) NOT NULL UNIQUE,
    status              order_status NOT NULL DEFAULT 'PENDING',

    request_memo        varchar(255) NULL,
    canceled_reason     varchar(255) NULL,

    total_amount        int NOT NULL,
    order_date          timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at        timestamp NULL,

    created_at          timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          uuid NULL,
    updated_at          timestamp NULL,
    updated_by          uuid NULL,
    deleted_at          timestamp NULL,
    deleted_by          uuid NULL
    );

ALTER TABLE p_order
    ADD CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES p_user(id) ON DELETE RESTRICT,
  ADD CONSTRAINT fk_order_store FOREIGN KEY (store_id) REFERENCES p_store(id) ON DELETE RESTRICT,
  ADD CONSTRAINT fk_order_delivery_address FOREIGN KEY (delivery_address_id) REFERENCES p_user_address(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_order_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_order_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_order_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_order_user_date ON p_order(user_id, order_date DESC);
CREATE INDEX IF NOT EXISTS ix_order_store_status ON p_order(store_id, status);

-- =======================
-- p_order_item
-- =======================
CREATE TABLE IF NOT EXISTS p_order_item (
                                            id                uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id           uuid NOT NULL,
    product_id         uuid NOT NULL,

    product_name       varchar(255) NOT NULL,
    unit_price         int NOT NULL,
    quantity           int NOT NULL,
    line_total_amount  int NOT NULL,

    created_at         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by         uuid NULL,
    updated_at         timestamp NULL,
    updated_by         uuid NULL,
    deleted_at         timestamp NULL,
    deleted_by         uuid NULL
    );

ALTER TABLE p_order_item
    ADD CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES p_order(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES p_product(id) ON DELETE RESTRICT,
  ADD CONSTRAINT fk_order_item_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_order_item_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_order_item_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_order_item_order_id ON p_order_item(order_id);

-- =======================
-- p_order_item_option
-- =======================
CREATE TABLE IF NOT EXISTS p_order_item_option (
                                                   id              uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    order_item_id   uuid NOT NULL,

    option_name     varchar(100) NOT NULL,
    option_item_name varchar(100) NOT NULL,
    extra_price     int NOT NULL DEFAULT 0,

    created_at      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      uuid NULL,
    updated_at      timestamp NULL,
    updated_by      uuid NULL,
    deleted_at      timestamp NULL,
    deleted_by      uuid NULL
    );

ALTER TABLE p_order_item_option
    ADD CONSTRAINT fk_order_item_option_item FOREIGN KEY (order_item_id) REFERENCES p_order_item(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_order_item_option_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_order_item_option_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_order_item_option_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_order_item_option_order_item_id ON p_order_item_option(order_item_id);

-- =======================
-- p_review
-- =======================
CREATE TABLE IF NOT EXISTS p_review (
                                        id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id    uuid NOT NULL UNIQUE,
    user_id     uuid NOT NULL,
    store_id    uuid NOT NULL,

    rating      int NOT NULL,
    content     text NULL,
    is_hidden   boolean NOT NULL DEFAULT false,

    created_at  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  uuid NULL,
    updated_at  timestamp NULL,
    updated_by  uuid NULL,
    deleted_at  timestamp NULL,
    deleted_by  uuid NULL
    );

ALTER TABLE p_review
    ADD CONSTRAINT fk_review_order FOREIGN KEY (order_id) REFERENCES p_order(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES p_user(id) ON DELETE RESTRICT,
  ADD CONSTRAINT fk_review_store FOREIGN KEY (store_id) REFERENCES p_store(id) ON DELETE RESTRICT,
  ADD CONSTRAINT fk_review_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_review_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_review_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_review_store_id ON p_review(store_id);

-- =======================
-- p_review_image
-- =======================
CREATE TABLE IF NOT EXISTS p_review_image (
                                              id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    review_id   uuid NOT NULL,

    image_url   varchar(500) NOT NULL,
    sort_order  int NOT NULL DEFAULT 0,

    created_at  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  uuid NULL,
    updated_at  timestamp NULL,
    updated_by  uuid NULL,
    deleted_at  timestamp NULL,
    deleted_by  uuid NULL
    );

ALTER TABLE p_review_image
    ADD CONSTRAINT fk_review_image_review FOREIGN KEY (review_id) REFERENCES p_review(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_review_image_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_review_image_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_review_image_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_review_image_review_id ON p_review_image(review_id);

-- =======================
-- p_cart
-- =======================
CREATE TABLE IF NOT EXISTS p_cart (
                                      id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     uuid NOT NULL,
    store_id    uuid NOT NULL,
    status      cart_status NOT NULL DEFAULT 'ACTIVE',

    created_at  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  uuid NULL,
    updated_at  timestamp NULL,
    updated_by  uuid NULL,
    deleted_at  timestamp NULL,
    deleted_by  uuid NULL
    );

ALTER TABLE p_cart
    ADD CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES p_user(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_cart_store FOREIGN KEY (store_id) REFERENCES p_store(id) ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cart_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_cart_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_cart_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_cart_user_status ON p_cart(user_id, status);

-- 유저당 ACTIVE 카트 1개 제한 (soft delete 고려)
CREATE UNIQUE INDEX IF NOT EXISTS uq_cart_one_active_per_user
    ON p_cart(user_id)
    WHERE status = 'ACTIVE' AND deleted_at IS NULL;

-- =======================
-- p_cart_item
-- =======================
CREATE TABLE IF NOT EXISTS p_cart_item (
                                           id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    cart_id     uuid NOT NULL,
    product_id  uuid NOT NULL,

    quantity   int NOT NULL,

    created_at  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  uuid NULL,
    updated_at  timestamp NULL,
    updated_by  uuid NULL,
    deleted_at  timestamp NULL,
    deleted_by  uuid NULL
    );

ALTER TABLE p_cart_item
    ADD CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES p_cart(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES p_product(id) ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cart_item_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_cart_item_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_cart_item_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_cart_item_cart_id ON p_cart_item(cart_id);

-- =======================
-- p_cart_item_option
-- =======================
CREATE TABLE IF NOT EXISTS p_cart_item_option (
                                                  id                    uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    cart_item_id           uuid NOT NULL,
    product_option_id      uuid NOT NULL,
    product_option_item_id uuid NOT NULL,

    created_at            timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by            uuid NULL,
    updated_at            timestamp NULL,
    updated_by            uuid NULL,
    deleted_at            timestamp NULL,
    deleted_by            uuid NULL
    );

ALTER TABLE p_cart_item_option
    ADD CONSTRAINT fk_cart_item_option_cart_item FOREIGN KEY (cart_item_id) REFERENCES p_cart_item(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_cart_item_option_option FOREIGN KEY (product_option_id) REFERENCES p_product_option(id) ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cart_item_option_option_item FOREIGN KEY (product_option_item_id) REFERENCES p_product_option_item(id) ON DELETE RESTRICT,
  ADD CONSTRAINT fk_cart_item_option_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_cart_item_option_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_cart_item_option_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_cart_item_option_cart_item_id ON p_cart_item_option(cart_item_id);

-- =======================
-- p_pg_provider
-- =======================
CREATE TABLE IF NOT EXISTS p_pg_provider (
                                             id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    code       varchar(50) NOT NULL,
    name       varchar(50) NULL,
    status     pg_provider_status NOT NULL DEFAULT 'ACTIVE',

    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NULL,
    updated_at timestamp NULL,
    updated_by uuid NULL,
    deleted_at timestamp NULL,
    deleted_by uuid NULL
    );

ALTER TABLE p_pg_provider
    ADD CONSTRAINT uq_pg_provider_code UNIQUE (code),
  ADD CONSTRAINT fk_pg_provider_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_pg_provider_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_pg_provider_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

-- =======================
-- p_payment
-- =======================
CREATE TABLE IF NOT EXISTS p_payment (
                                         id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id    uuid NOT NULL,

    method     varchar(50) NULL,
    status     payment_status NOT NULL DEFAULT 'READY',
    amount     int NOT NULL,
    paid_at    timestamp NULL,

    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NULL,
    updated_at timestamp NULL,
    updated_by uuid NULL,
    deleted_at timestamp NULL,
    deleted_by uuid NULL
    );

ALTER TABLE p_payment
    ADD CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES p_order(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_payment_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_payment_updated_by FOREIGN KEY (updated_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_payment_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_payment_order_id ON p_payment(order_id);

-- =======================
-- p_payment_log
-- =======================
CREATE TABLE IF NOT EXISTS p_payment_log (
                                             id             uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    payment_id      uuid NOT NULL,
    pg_provider_id  uuid NOT NULL,

    status         pg_log_status NULL,
    pg_tid         varchar(255) NULL,
    reason         text NULL,

    created_at     timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     uuid NULL,

    deleted_at     timestamp NULL,
    deleted_by     uuid NULL
    );

ALTER TABLE p_payment_log
    ADD CONSTRAINT fk_payment_log_payment FOREIGN KEY (payment_id) REFERENCES p_payment(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_payment_log_pg_provider FOREIGN KEY (pg_provider_id) REFERENCES p_pg_provider(id) ON DELETE RESTRICT,
  ADD CONSTRAINT fk_payment_log_created_by FOREIGN KEY (created_by) REFERENCES p_user(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_payment_log_deleted_by FOREIGN KEY (deleted_by) REFERENCES p_user(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS ix_payment_log_payment_id ON p_payment_log(payment_id);
CREATE INDEX IF NOT EXISTS ix_payment_log_pg_provider_id ON p_payment_log(pg_provider_id);
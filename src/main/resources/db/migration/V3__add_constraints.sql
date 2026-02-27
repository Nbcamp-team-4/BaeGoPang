-- V3__add_constraints.sql
-- 데이터 무결성 제약조건 추가
-- =========================

-- 1) 리뷰 평점 범위 (1~5)
ALTER TABLE p_review
    ADD CONSTRAINT ck_review_rating_range
        CHECK (rating BETWEEN 1 AND 5);

-- 2) 수량 체크 (>= 1)
ALTER TABLE p_cart_item
    ADD CONSTRAINT ck_cart_item_quantity_positive
        CHECK (quantity >= 1);

ALTER TABLE p_order_item
    ADD CONSTRAINT ck_order_item_quantity_positive
        CHECK (quantity >= 1);

-- 3) 금액 음수 방지
ALTER TABLE p_product
    ADD CONSTRAINT ck_product_price_non_negative
        CHECK (price >= 0);

ALTER TABLE p_order
    ADD CONSTRAINT ck_order_total_amount_non_negative
        CHECK (total_amount >= 0);

ALTER TABLE p_order_item
    ADD CONSTRAINT ck_order_item_unit_price_non_negative
        CHECK (unit_price >= 0);

ALTER TABLE p_order_item
    ADD CONSTRAINT ck_order_item_line_total_non_negative
        CHECK (line_total_amount >= 0);

ALTER TABLE p_order_item_option
    ADD CONSTRAINT ck_order_item_option_extra_price_non_negative
        CHECK (extra_price >= 0);

ALTER TABLE p_payment
    ADD CONSTRAINT ck_payment_amount_non_negative
        CHECK (amount >= 0);

ALTER TABLE p_product_option_item
    ADD CONSTRAINT ck_option_item_additional_price_non_negative
        CHECK (additional_price >= 0);

-- 4) 배달 예상시간 논리 체크 (min <= max)
ALTER TABLE p_store
    ADD CONSTRAINT ck_store_delivery_time_range
        CHECK (delivery_min_minutes <= delivery_max_minutes);

-- 5) 리뷰 이미지 정렬값 음수 방지
ALTER TABLE p_review_image
    ADD CONSTRAINT ck_review_image_sort_order_non_negative
        CHECK (sort_order >= 0);
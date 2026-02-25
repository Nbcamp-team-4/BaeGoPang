팀4 조의 프로젝트입니다

기간 🎳2026  2/24 ~ 3/12 

🥡 배고팡 (Baegopang)
📌 소개
- 한 줄 정리: 배달 주문 관리 플랫폼 개발
- 내용:
- 광화문 근처에서 운영될 음식점들의 배달 주문 관리, 결제, 주문 내역 관리 기능 제공
- 전국적으로 작동 가능한 시스템을 구축하되, 실제 주문은 광화문 근처 음식점으로 한정

⚙️ 프로젝트 핵심 기술
백엔드 :  Spring Boot 3.X, Java 25, Spring Security + JWT 인증, JPA, PostgreSQL, Gradle, Redis
AI : Gemini AI API
협업 : Git, Github, Postman, Swagger, Notion, Slack


👥 팀 역할 분담
1. 😊권진석 — 로그인/회원가입 + 권한 (Auth/Identity)
- 회원가입/로그인/로그아웃, JWT 인증 (Access/Refresh)
- 권한(Role: CUSTOMER / OWNER / ADMIN), 인증 필터, 예외 처리
- (선택) 휴대폰/이메일 인증, 비밀번호 재설정
산출물
- DB: users, user_roles, (선택) refresh_tokens
- API: /auth/*
- 공통: SecurityConfig, JwtProvider, AuthExceptionHandler
계약
- JWT Claims 스펙 정의 (로그인된 사용자 id/role 전달 방식)

2.😊 이호영 — 상품/가게 (Store/Catalog)
- 카테고리/가게 목록·상세
- 메뉴 CRUD, 옵션 구성, 품절 처리
- (선택) 가게 운영시간/휴무, 검색/정렬
산출물
- DB: categories, stores, store_categories
- DB: menus, menu_options, menu_option_items
- API: /stores/*, /menus/*, /categories/*, /owner/*
계약
- 주문팀에 메뉴 조회/가격/품절 규칙 제공 (주문 생성 시 스냅샷 기준)

3. 😊김민지 — 장바구니 + 주문 (Cart/Order)
- 장바구니(한 가게만 담기), 옵션 선택, 수량 변경
- 주문 생성/조회/취소
- 주문 상태 머신 (CREATED / PAID / CANCELED + 사장 처리 상태)
산출물
- DB: carts, cart_items, cart_item_options
- DB: orders, order_items, order_item_options
- API: /cart/*, /orders/*
계약
- 결제팀과: 결제 성공 시 주문 상태 전이 이벤트/콜백
- 상품팀과: 주문 생성 시 가격/옵션 검증 및 스냅샷 저장

4. 😊최미은 — 결제 + PG 연동 (Payment)
- 결제 준비/승인/실패/환불
- PG 연동 모듈 (실PG 또는 모의 PG), 웹훅 처리
- 결제 로그/트랜잭션 기록
산출물
- DB: payments, (선택) payment_transactions
- API: /payments/*, /payments/webhook
계약
- 주문팀에 결제 결과 통지 방식 합의 (동기 호출 vs 이벤트)

5. 😊김도형 — 리뷰 + AI API 연동 (Review + AI)
- 리뷰 작성/조회, 가게별 평점 집계
- 리뷰 이미지/사장님 답글 (선택)
- AI API 연동: 리뷰 요약, 악성 리뷰 필터링, 답글 초안 생성
산출물
- DB: reviews, (선택) review_images, review_replies
- DB: (선택) ai_requests, ai_moderations
- API: /reviews/*, /stores/{id}/reviews, /ai/*
계약
- 주문팀과: 리뷰는 배달 완료된 주문만 작성 가능
- 상품팀과: 가게 평점 평균 업데이트 방식 (배치 vs 실시간)


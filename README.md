# BaeGoPang 프로젝트

기간 🎳2026  2/24 ~ 3/12 

## 🥡 배고팡 (Baegopang)
📌 소개
- 한 줄 정리: 배달 주문 관리 플랫폼 개발
- 내용:
- 광화문 근처에서 운영될 음식점들의 배달 주문 관리, 결제, 주문 내역 관리 기능 제공
- 전국적으로 작동 가능한 시스템을 구축하되, 실제 주문은 광화문 근처 음식점으로 한정

## ⚙️ 프로젝트 핵심 기술
### Backend
- Language : Java 21
- Framework : Spring Boot 3.X
- Security : Spring Security
- Module : Spring Data JPA
### Database
- PostgreSQL
- Flyway (DB Migration)
### Authentication
- JWT (Access Token / Refresh Token)
### Payment
- Toss Payments API
- API : Gemini AI API, Toss Payments API
### Infrastructure
- AWS S3 (파일 업로드)
### Tool  
- Git, Github, Postman,Notion, Slack
### Documentation
- Swagger (OpenAPI)

---

# Project Structure

본 프로젝트는 **도메인 중심 패키지 구조 (Domain Driven Structure)** 를 기반으로 설계되었습니다.

각 도메인은 다음과 같은 계층으로 구성됩니다.

- api : Controller 및 Request / Response DTO
- entity : JPA Entity
- repository : 데이터 접근 계층
- service : 비즈니스 로직
- exception : 도메인 예외 처리

공통 기능은 global 패키지에서 관리합니다.
```
domain
 ├─ address
 │  ├─ api
 │  │  ├─ request
 │  │  └─ response
 │  ├─ dto
 │  ├─ entity
 │  ├─ repository
 │  └─ service
 ├─ auth
 ├─ cart
 ├─ category
 ├─ order
 ├─ payment
 ├─ payment_log
 ├─ product
 ├─ region
 ├─ review
 ├─ store
 └─ user

global
 ├─ config
 ├─ jwt
 ├─ resolver
 ├─ file
 └─ common
 resources
└─ db
   ├─ migration
   └─ dev-seed
```
---

👥 팀 역할 분담
1. 😊권진석 — 로그인/회원가입 + 권한 (Auth/Identity)
- 회원가입/로그인/로그아웃, JWT 인증 (Access/Refresh)
- 권한(Role: CUSTOMER / OWNER / ADMIN), 인증 필터, 예외 처리
산출물
- DB: users, user_roles, (선택) refresh_tokens
- API: /auth/*
- 공통: SecurityConfig, JwtProvider, AuthExceptionHandler
계약
- JWT Claims 스펙 정의 (로그인된 사용자 id/role 전달 방식)

2.😊 이호영 — 상품/가게 (Store/Catalog)
- 카테고리/가게 목록·상세
- 메뉴 CRUD, 옵션 구성, 품절 처리
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
- DB: payments
- API: /payments/*, /payments/webhook
계약
- 주문팀에 결제 결과 통지 방식 합의 (동기 호출 vs 이벤트)

5. 😊김도형 — 리뷰 + AI API 연동 (Review + AI)
- 리뷰 작성/조회, 가게별 평점 집계
- 리뷰 이미지
- AI API 연동: 리뷰 요약, 악성 리뷰 필터링, 답글 초안 생성
산출물
- DB: reviews, (선택) review_images, review_replies
- DB: (선택) ai_requests, ai_moderations
- API: /reviews/*, /stores/{id}/reviews, /ai/*
계약
- 주문팀과: 리뷰는 배달 완료된 주문만 작성 가능
- 상품팀과: 가게 평점 평균 업데이트 방식 (배치 vs 실시간)
  

## API Documentation

Swagger를 사용하여 API 문서를 제공합니다.
http://localhost:8080/swagger-ui/index.html

## How to Run

### 1. 환경 변수 설정

DB_URL=jdbc:postgresql://localhost:5432/baegopang
DB_USERNAME=postgres
DB_PASSWORD=postgres

JWT_SECRET=your-secret-key

AWS_ACCESS_KEY=
AWS_SECRET_KEY=
AWS_S3_BUCKET=

### 2. 프로젝트 실행
./gradlew bootRun

## ERD
https://www.erdcloud.com/d/HZggcjpBFCWuzNAQu



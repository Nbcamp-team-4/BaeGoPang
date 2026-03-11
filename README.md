# BaeGoPang - 배달 주문 관리 커머스 플랫폼
## 🧭 목차
* [🚀 프로젝트 소개](#-프로젝트-소개)
* [🎯 핵심 기능](#-핵심-기능)
* [🛠️ 기술 스택](#️-기술-스택)
* [🏗️ 시스템 구조 및 설계](#️-시스템-구조-및-설계)
* [👥 팀 역할 분담](#-팀-역할-분담)
* [📄 API 문서](#-api-문서)
* [⚙️ 실행 방법](#️-실행-방법)
* [🗄️ ERD](#️-erd)

---
## 📅 프로젝트 기간

2026.02.24 ~ 2026.03.12

## 🚀 프로젝트 소개

**BaeGoPang(배고팡)** 은  
배달 서비스 기반의 **커머스 플랫폼 백엔드 프로젝트**입니다.

사용자는 상품을 조회하고 주문 및 결제를 진행할 수 있으며,  
매장과 상품을 관리하고 리뷰를 작성할 수 있습니다.

또한 서비스 전반에 걸쳐 다음과 같은 기능을 포함합니다.

- **JWT 기반 인증/인가**
- **Toss Payments 결제 연동**
- **AWS S3 파일 업로드**
- **AI API 연동 기능**
- **Swagger 기반 API 문서화**
- **Flyway 기반 DB 마이그레이션 관리**

### 📌 프로젝트 목표

광화문 인근 음식점들의 배달 주문 흐름을 중심으로,  
실제 서비스 운영을 고려한 **주문 / 결제 / 리뷰 / 관리자 기능**을 통합적으로 구현하는 것을 목표로 했습니다.

- 사용자: 상품 조회, 장바구니, 주문, 결제, 리뷰 작성
- 점주: 매장 및 상품 관리
- 관리자: 사용자 및 권한 관리, 서비스 운영 기능

---
## 🎯 핵심 기능
| 기능                     | 설명                                                                        | 기술 요소                          |
| ---------------------- | ------------------------------------------------------------------------- | ------------------------------ |
| 🔐 **JWT 인증 / 인가 시스템** | Access Token / Refresh Token 기반 로그인 인증 및 권한 관리 (CUSTOMER / OWNER / ADMIN) | Spring Security, JWT           |
| 👤 **사용자 및 관리자 관리**    | 사용자 정보 조회 및 수정, 관리자 권한 관리 기능 제공                                           | Spring Security, Role 기반 접근 제어 |
| 📍 **배송지 관리**          | 배송지 등록 / 수정 / 삭제 및 기본 배송지 설정 기능 제공                                        | JPA, REST API                  |
| 🏪 **매장 및 상품 관리**      | 카테고리별 매장 조회, 상품 CRUD 및 옵션 관리 기능                                           | Spring Boot, JPA               |
| 🛒 **장바구니 시스템**        | 한 매장 기준 장바구니 구성, 옵션 선택 및 수량 변경                                            | JPA, 서비스 로직                    |
| 📦 **주문 관리 시스템**       | 주문 생성 / 조회 / 취소 및 주문 상태 관리                                                | 주문 상태 머신                       |
| 💳 **결제 시스템**          | Toss Payments API 기반 결제 승인 / 실패 / 환불 처리                                   | Toss Payments API              |
| ⭐ **리뷰 시스템**           | 리뷰 작성 및 조회, 가게 평점 집계 및 리뷰 이미지 업로드                                         | JPA, AWS S3                    |
| 🤖 **AI 기능**           | 리뷰 요약, 악성 리뷰 필터링, 답글 초안 생성                                                | Gemini AI API                  |
| 📄 **API 문서화**         | Swagger 기반 API 문서 자동 생성                                                   | Swagger / OpenAPI              |
| 🗄️ **DB 버전 관리**       | Flyway 기반 데이터베이스 마이그레이션 관리                                                | Flyway                         |
| ☁️ **파일 업로드**          | AWS S3를 활용한 이미지 파일 저장 및 관리                                                | AWS S3                         |

---
## 🛠️ 기술 스택

### Backend
- **Java 21**
- **Spring Boot 3.x**
- **Spring Security**
- **Spring Data JPA**
- **Hibernate**

### Database
- **PostgreSQL**
- **Flyway**

### Authentication
- **JWT (Access Token / Refresh Token)**

### Payment
- **Toss Payments API**

### AI / External API
- **Gemini AI API**
- **Toss Payments API**

### Storage / Infra
- **AWS S3**

### Dev Tools
- **Git**
- **GitHub**
- **Postman**
- **Notion**
- **Slack**

### Documentation
- **Swagger / OpenAPI**

---

## 🏗️ 시스템 구조 및 설계

본 프로젝트는 **도메인 중심 패키지 구조 (Domain Driven Structure)** 를 기반으로 설계되었습니다.

각 도메인은 다음 계층으로 구성됩니다.

- **api** : Controller, Request/Response DTO
- **dto** : 도메인 내부 데이터 전달 객체
- **entity** : JPA Entity
- **repository** : 데이터 접근 계층
- **service** : 비즈니스 로직
- **exception** : 도메인 예외 처리

공통 기능은 global 패키지에서 관리합니다.

### 📂 Project Structure

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
     ├─ dto
     ├─ entity
     └─ exception
 resources
└─ db
   ├─ migration
   └─ dev-seed
```
## 📌 설계 특징

- 도메인별 관심사를 분리하여 유지보수성과 확장성을 높임
- 공통 응답/예외/엔티티는 global.common 에서 관리
- DB 버전 관리는 Flyway를 통해 일관성 있게 유지
- 인증/인가 로직은 Spring Security + JWT 구조로 분리
---

## 👥 팀 역할 분담
| 이름      | 담당 기능                     | 주요 구현 내용                                                            | 산출물 (DB / API)                                                                                                                                                |
| ------- | ------------------------- | ------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **권진석** | 사용자 / 관리자 / 배송지           | 사용자 관리, 관리자 기능, 권한(Role: CUSTOMER / OWNER / ADMIN), 배송지 CRUD, 예외 처리 | **DB**: users, user_role, address <br>**API**: `/user/*`, `/admin/*`, `/address/*`                                                                     |
| **이호영** | 상품 / 가게 | 카테고리 및 가게 목록·상세 조회, 메뉴 CRUD, 옵션 구성, 품절 처리, 지역                           | **DB**: category, store, store_category <br>**DB**: product, product_option, product_option_items<br>**API**: `/store/*`, `/menu/*`, `/category/*`, `/owner/*` |
| **김민지** | 장바구니 / 주문 / AWS           | 장바구니(한 가게 기준), 옵션 선택, 수량 변경, 주문 생성/조회/취소, 주문 상태 관리, AWS 배포          | **DB**: cart, cart_item, cart_item_option, region<br>**DB**: order, order_item, order_item_option<br>**API**: `/cart/*`, `/order/*` , `/regions/*`                                   |
| **최미은** | 결제 / 로그인 / 회원가입           | Toss 결제 연동, 결제 승인/실패/환불 처리, 결제 로그 관리, JWT 인증 및 로그인/회원가입 구현          | **DB**: payment, payment_log<br>**API**: `/payment/*`, `/payment_log/*`, `/auth/*`                                                                            |
| **김도형** | 리뷰 / AI API               | 리뷰 작성 및 조회, 가게 평점 집계, 리뷰 이미지 업로드, AI 리뷰 요약 및 악성 리뷰 필터링              | **DB**: review, review_image, ai_log<br>**API**: `/reviews/*`, `/ai/*`                                                                                         |



## 📄 API 문서

Swagger를 사용하여 API 문서를 제공합니다.

http://localhost:8080/swagger-ui/index.html

## ⚙️ 실행 방법

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

## 🗄️ ERD
<img width="4980" height="2482" alt="baegopa_main" src="https://github.com/user-attachments/assets/41b64375-a62e-46e5-9620-3d5fe4e32f53" />





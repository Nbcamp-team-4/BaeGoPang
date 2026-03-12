# BaeGoPang - 배달 주문 관리 커머스 플랫폼
## 🧭 목차
* [🚀 프로젝트 소개](#-프로젝트-소개)
* [🎯 핵심 기능](#-핵심-기능)
* [🛠️ 기술 스택](#-기술-스택)
* [🏗️ 시스템 구조 및 설계](#️-시스템-구조-및-설계)
* [👥 팀 역할 분담](#-팀-역할-분담)
* [📄 API 문서](#-api-문서)
* [⚙️ 실행 방법](#️-실행-방법)
* [🗄️ ERD](#️-erd)
  
---
## 📅 프로젝트 기간

2026.02.24 ~ 2026.03.12

---

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
- **PostGIS 기반 위치/지역 데이터 처리**

---

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
| 🤖 **AI 기능**           | 리뷰 요약, 악성 리뷰 필터링, 답글 초안 생성                                                | OPEN AI API                  |
| 📄 **API 문서화**         | Swagger 기반 API 문서 자동 생성                                                   | Swagger / OpenAPI              |
| 🗄️ **DB 버전 관리**       | Flyway 기반 데이터베이스 마이그레이션 관리                                                | Flyway                         |
| ☁️ **파일 업로드**          | AWS S3를 활용한 이미지 파일 저장 및 관리                                                | AWS S3                         |

---
## 🧰 기술 스택

### Backend
![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)

### Security / Authentication
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)

### Database
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)

### Infrastructure / Storage
![AWS S3](https://img.shields.io/badge/AWS_S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)

### External API / AI
![OpenAI](https://img.shields.io/badge/OpenAI_API-412991?style=for-the-badge&logo=openai&logoColor=white)
![Toss Payments](https://img.shields.io/badge/Toss_Payments-0064FF?style=for-the-badge&logo=simpleicons&logoColor=white)

### DevOps / Collaboration
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)

### Documentation
![Swagger](https://img.shields.io/badge/Swagger/OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

---
## 🏗️ 시스템 구조 및 설계

본 프로젝트는 **도메인 중심 패키지 구조(Domain-Oriented Package Structure)** 를 기반으로 설계했습니다.  
기능별로 패키지를 분리하여 응집도를 높이고, 각 도메인 안에서 API, Entity, Repository, Service, Exception을 관리하도록 구성했습니다.

공통 기능은 `global` 패키지에서 관리하며, 인증 / 인가, 공통 응답, 예외 처리, 파일 업로드 등 여러 도메인에서 함께 사용하는 기능을 분리했습니다.

### 📂 Project Structure

```text
src/main/java/com/team/project

├── domain
│   ├── address
│   ├── ai
│   ├── auth
│   ├── cart
│   ├── category
│   ├── order
│   ├── payment
│   ├── payment_log
│   ├── product
│   ├── region
│   ├── review
│   ├── store
│   └── user
│
└── global
    ├── common
    ├── config
    ├── file
    ├── jwt
    └── resolver
```
각 도메인은 아래와 같은 공통 구조를 따릅니다.

```
domain/{domain-name}

├── api
│   ├── request
│   └── response
├── entity
├── repository
├── service
├── exception
└── model (dto / vo)
```

---


## 📌 설계 특징

- 도메인별 관심사를 분리하여 유지보수성과 확장성을 높였습니다.
- 공통 응답 / 예외 / 엔티티는 global.common에서 관리합니다.
- 인증 / 인가 로직은 Spring Security + JWT 구조로 분리했습니다.
- DB 버전 관리는 Flyway를 통해 일관성 있게 유지했습니다.

---

## 👥 팀 역할 분담
<table>
  <thead>
    <tr>
      <th width="110">이름</th>
      <th width="190">담당 영역</th>
      <th width="420">주요 구현 내용</th>
      <th width="380">산출물 (DB / API)</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><strong>권진석</strong></td>
      <td>사용자 / 관리자 / 배송지</td>
      <td>
        사용자 정보 관리, 관리자 기능 구현, 권한(Role: CUSTOMER / OWNER / ADMIN) 관리,
        배송지 등록 / 조회 / 수정 / 삭제 기능 구현
      </td>
      <td>
        DB: user, user_role, role, address<br>
        API: /users/*, /admins/*, /address/*
      </td>
    </tr>
    <tr>
      <td><strong>김도형</strong></td>
      <td>리뷰 / 리뷰 이미지 / AI</td>
      <td>
        리뷰 작성 및 조회 기능 구현, 리뷰 이미지 처리,
        AI 기반 리뷰 요약 기능 구현
      </td>
      <td>
        DB: review, review_image, ai_log<br>
        API: /reviews/*, /review_images/*, /ai/*
      </td>
    </tr>
    <tr>
      <td><strong>김민지</strong></td>
      <td>장바구니 / 주문 / AWS</td>
      <td>
        장바구니 관리, 주문 생성 / 조회 / 취소 기능 구현,
        AWS 기반 배포 및 인프라 구성
      </td>
      <td>
        DB: cart, cart_item, cart_item_option, order, order_item<br>
        API: /carts/*, /orders/*
      </td>
    </tr>
    <tr>
      <td><strong>이호영</strong></td>
      <td>상품 / 가게 / 지역 / 카테고리</td>
      <td>
        배송지 기반 가게 매핑, PostGIS 멀티폴리곤을 활용한 지역 식별,
        카테고리 / 가게 목록 조회, 메뉴 CRUD 및 옵션 구성 구현
      </td>
      <td>
        DB: category, region, store, store_category, product, product_option, product_option_item<br>
        API: /categories/*, /regions/*, /stores/*, /products/*, /images/*
      </td>
    </tr>
    <tr>
      <td><strong>최미은</strong></td>
      <td>결제 / 로그인 / 회원가입</td>
      <td>
        Toss Payments 결제 연동,
        JWT 기반 인증 및 로그인 / 회원가입 기능 구현
      </td>
      <td>
        DB: payment, payment_log, refreshToken<br>
        API: /payment/*, /payment_log/*, /auth/*
      </td>
    </tr>
  </tbody>
</table>

------

## 📄 API 문서

Swagger를 사용하여 API 문서를 제공합니다.

- http://localhost:8080/swagger-ui/index.html
- https://baegopang.kro.kr/swagger-ui/index.html

----

## ⚙️ 실행 방법

### 1. 환경 변수 설정
```
DB_URL=jdbc:postgresql://localhost:5432/baegopang<br>
DB_USERNAME=postgres<br>
DB_PASSWORD=postgres

JWT_SECRET=your-secret-key

AWS_ACCESS_KEY=<br>
AWS_SECRET_KEY=<br>
AWS_S3_BUCKET=
```

### 2. 프로젝트 실행
```
./gradlew bootRun
```

---

## 🗄️ ERD
<img width="800" height="2482" alt="baegopa_main" src="https://github.com/user-attachments/assets/41b64375-a62e-46e5-9620-3d5fe4e32f53" />

## 🚚 Architecture
<img width="700" height="675" alt="baepopang_infra_6" src="https://github.com/user-attachments/assets/ffcf8922-be69-4be9-9b7b-5d3a96616221" />

---

## 🖥️ Frontend Repository

BaeGoPang의 프론트엔드는 별도의 저장소에서 관리됩니다.

[![BaeGoPang Frontend](https://img.shields.io/badge/BaeGoPang-Frontend-0A66C2?style=for-the-badge&logo=github)](https://github.com/Nbcamp-team-4/BaeGoPangFront)

🔗 https://github.com/Nbcamp-team-4/BaeGoPangFront

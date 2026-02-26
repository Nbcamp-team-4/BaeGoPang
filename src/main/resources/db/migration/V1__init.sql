-- 예시: 스키마(테이블) 초기 생성
create table if not exists p_user (
    id uuid primary key,
    login_id varchar(50) not null unique,
    email varchar(255) not null unique,
    password varchar(255) not null,
    name varchar(100) not null,
    phone varchar(20) not null unique,
    status varchar(20) not null,
    created_at timestamp not null,
    updated_at timestamp null
);
package com._team._project.global;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;



    @Getter
    @MappedSuperclass
    @EntityListeners(AuditingEntityListener.class)
    public abstract class BaseEntity {
        @CreatedDate
        @Column(updatable = false)
        private LocalDateTime createdAt;
        @CreatedBy
        @Column(updatable = false)
        //스프링 단기 심화반 | 내일배움캠프AI 검증 비즈니스 프로젝트 | 수강생 가이드
        private String createdBy;
        @LastModifiedDate
        private LocalDateTime updatedAt;
        @LastModifiedBy
        private String updatedBy;
        private LocalDateTime deletedAt;
        private String deletedBy;
    }



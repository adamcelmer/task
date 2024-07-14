package com.magnoliacms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class AuditableEntity implements Serializable {

    @Id
    private String id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "modified_at", nullable = false)
    private Instant modifiedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        modifiedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        modifiedAt = Instant.now();
    }
}
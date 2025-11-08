package com.example.springtutorial.entities;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class Metadata {

    @Builder.Default
    protected Boolean isDeleted = false;

    @CreatedDate
    @Builder.Default
    protected LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Builder.Default
    protected LocalDateTime lastModifiedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "created_by")
    protected Person createdBy;

    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    protected Person lastModifiedBy;
}

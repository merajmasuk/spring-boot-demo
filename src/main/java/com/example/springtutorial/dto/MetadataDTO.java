package com.example.springtutorial.dto;

import com.example.springtutorial.entities.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class MetadataDTO {
    protected Boolean isDeleted;
    protected String createdAt;
    protected String lastModifiedAt;
    protected Person createdBy;
    protected Person lastModifiedBy;
}

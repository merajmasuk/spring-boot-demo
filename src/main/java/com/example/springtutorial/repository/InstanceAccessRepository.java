package com.example.springtutorial.repository;

import com.example.springtutorial.entities.Instance;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(path = "instances")
public interface InstanceAccessRepository extends PagingAndSortingRepository<Instance, UUID> {
    List<Instance> findByTitleContainingAndIsDeleted(String title, boolean deleted);
}

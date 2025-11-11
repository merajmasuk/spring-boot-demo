package com.example.springtutorial.repository;

import com.example.springtutorial.entities.Instance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstanceRepository extends JpaRepository<Instance, UUID> {
    Optional<Instance> findByIdAndIsDeleted(UUID id, boolean deleted);
    Optional<Instance> findByTitleAndIsDeleted(String title, boolean deleted);
    Page<Instance> findByTitleContainingAndIsDeleted(String title, boolean deleted, Pageable pageable);
}

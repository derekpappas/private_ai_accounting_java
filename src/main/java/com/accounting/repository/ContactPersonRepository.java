package com.accounting.repository;

import com.accounting.model.entity.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {
    boolean existsByEmail(String email);
    Optional<ContactPerson> findByEmail(String email);
} 
package com.accounting.repository;

import com.accounting.model.entity.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {
    boolean existsByEmail(String email);
} 
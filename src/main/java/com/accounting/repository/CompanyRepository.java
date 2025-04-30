package com.accounting.repository;

import com.accounting.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query(value = "INSERT INTO companies (name, corporation_type, contact_person_id) VALUES (:name, cast(:corporationType as corporation_type), :contactPersonId) RETURNING *", nativeQuery = true)
    Company createCompanyWithEnumCast(
        @Param("name") String name,
        @Param("corporationType") String corporationType,
        @Param("contactPersonId") Long contactPersonId
    );

    boolean existsByName(String name);
} 
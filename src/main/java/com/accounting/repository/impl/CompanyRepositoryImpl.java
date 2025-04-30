package com.accounting.repository.impl;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.model.entity.Company;
import com.accounting.model.entity.ContactPerson;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class CompanyRepositoryImpl {
    private final JdbcTemplate jdbcTemplate;

    public CompanyRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Company createCompany(String name, String corporationType, Long contactPersonId) {
        String sql = "INSERT INTO companies (name, corporation_type, contact_person_id) " +
                    "VALUES (?, ?::corporation_type, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.setString(2, corporationType);
                if (contactPersonId != null) {
                    ps.setLong(3, contactPersonId);
                } else {
                    ps.setNull(3, java.sql.Types.BIGINT);
                }
                return ps;
            }, keyHolder);

            Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();

            Company company = new Company();
            company.setId(id);
            company.setName(name);
            company.setCorporationType(corporationType);
            if (contactPersonId != null) {
                company.setContactPerson(new ContactPerson());
                company.getContactPerson().setId(contactPersonId);
            }

            return company;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create company: " + e.getMessage(), e);
        }
    }
} 
package com.accounting.config;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class TestCleanupConfiguration {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void clearDatabase() {
        try {
            entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
            
            // Delete from tables in reverse order of dependencies
            entityManager.createNativeQuery("DELETE FROM \"transactions\"").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM \"files\"").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM \"credit_cards\"").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM \"bank_info\"").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM \"companies\"").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM \"contact_persons\"").executeUpdate();
            
            entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
            
            entityManager.flush();
            entityManager.clear();
        } catch (Exception e) {
            // Log but don't fail if tables don't exist yet
            System.out.println("Warning: Could not clean database tables. They may not exist yet.");
        }
    }
} 
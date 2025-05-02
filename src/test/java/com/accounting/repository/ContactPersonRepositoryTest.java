package com.accounting.repository;

import com.accounting.model.entity.ContactPerson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ContactPersonRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContactPersonRepository contactPersonRepository;

    @Test
    void findByEmail_ShouldReturnContactPerson() {
        // Given
        ContactPerson person = new ContactPerson();
        person.setName("John Doe");
        person.setEmail("john.doe@example.com");
        person.setPhone("1234567890");
        entityManager.persist(person);
        entityManager.flush();

        // When
        ContactPerson found = contactPersonRepository.findByEmail("john.doe@example.com")
                .orElse(null);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("John Doe");
        assertThat(found.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(found.getPhone()).isEqualTo("1234567890");
    }

    @Test
    void save_ShouldPersistContactPerson() {
        // Given
        ContactPerson person = new ContactPerson();
        person.setName("Jane Doe");
        person.setEmail("jane.doe@example.com");
        person.setPhone("0987654321");

        // When
        ContactPerson saved = contactPersonRepository.save(person);
        entityManager.flush();

        // Then
        ContactPerson found = entityManager.find(ContactPerson.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Jane Doe");
        assertThat(found.getEmail()).isEqualTo("jane.doe@example.com");
        assertThat(found.getPhone()).isEqualTo("0987654321");
    }
} 
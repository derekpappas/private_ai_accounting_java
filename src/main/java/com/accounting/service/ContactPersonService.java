package com.accounting.service;

import com.accounting.model.entity.ContactPerson;
import java.util.List;
import java.util.Optional;

public interface ContactPersonService {
    ContactPerson createContactPerson(ContactPerson contactPerson);
    Optional<ContactPerson> getContactPersonById(Long id);
    List<ContactPerson> getAllContactPersons();
    ContactPerson updateContactPerson(ContactPerson contactPerson);
    void deleteContactPerson(Long id);
    boolean existsByEmail(String email);
} 
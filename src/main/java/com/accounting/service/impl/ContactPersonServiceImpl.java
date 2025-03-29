package com.accounting.service.impl;

import com.accounting.exception.AccountingException;
import com.accounting.model.entity.ContactPerson;
import com.accounting.repository.ContactPersonRepository;
import com.accounting.service.ContactPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactPersonServiceImpl implements ContactPersonService {
    private final ContactPersonRepository contactPersonRepository;

    @Override
    public ContactPerson createContactPerson(ContactPerson contactPerson) {
        if (contactPersonRepository.existsByEmail(contactPerson.getEmail())) {
            throw new AccountingException("Contact person with email " + contactPerson.getEmail() + " already exists");
        }
        return contactPersonRepository.save(contactPerson);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContactPerson> getContactPersonById(Long id) {
        return contactPersonRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactPerson> getAllContactPersons() {
        return contactPersonRepository.findAll();
    }

    @Override
    public ContactPerson updateContactPerson(ContactPerson contactPerson) {
        if (!contactPersonRepository.existsById(contactPerson.getId())) {
            throw new AccountingException("Contact person not found with id: " + contactPerson.getId());
        }
        return contactPersonRepository.save(contactPerson);
    }

    @Override
    public void deleteContactPerson(Long id) {
        contactPersonRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return contactPersonRepository.existsByEmail(email);
    }
} 
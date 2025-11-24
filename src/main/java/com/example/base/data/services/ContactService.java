package com.example.base.data.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.base.data.models.ContactModel;
import com.example.base.data.repositories.ContactRepository;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Transactional(readOnly = true)
    public List<ContactModel> findAll() {
        return contactRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ContactModel> findById(Long id) {
        return contactRepository.findById(id);
    }

    @Transactional
    public ContactModel save(ContactModel contact) {
        return contactRepository.save(contact);
    }

    @Transactional
    public void delete(Long id) {
        contactRepository.deleteById(id);
    }
}

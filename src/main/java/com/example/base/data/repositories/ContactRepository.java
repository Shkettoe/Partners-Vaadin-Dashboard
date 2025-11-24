package com.example.base.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.base.data.models.ContactModel;

@Repository
public interface ContactRepository extends JpaRepository<ContactModel, Long> {

}

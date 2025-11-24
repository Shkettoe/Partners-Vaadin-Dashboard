package com.example.base.data.repositories;

import org.springframework.stereotype.Repository;

import com.example.base.data.models.PartnerModel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerModel, Long> {
    Optional<PartnerModel> findByName(String name);

    Optional<PartnerModel> findByShortName(String shortName);

    Optional<PartnerModel> findByTaxNumber(String taxNumber);
}

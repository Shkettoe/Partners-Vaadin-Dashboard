package com.example.base.data.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.base.data.models.PartnerModel;
import com.example.base.data.repositories.PartnerRepository;

@Service
public class PartnerService {
    private final PartnerRepository partnerRepository;

    public PartnerService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    @Transactional(readOnly = true)
    public List<PartnerModel> findAll() {
        return partnerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PartnerModel> findById(Long id) {
        return partnerRepository.findById(id);
    }

    @Transactional
    public PartnerModel save(PartnerModel partner) {
        return partnerRepository.save(partner);
    }

    @Transactional
    public void delete(Long id) {
        partnerRepository.deleteById(id);
    }

    @Transactional
    public void bulkDelete(List<Long> ids) {
        partnerRepository.deleteAllById(ids);
    }

    @Transactional
    public PartnerModel update(Long id, PartnerModel updatedPartner) {
        return partnerRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedPartner.getName());
                    existing.setShortName(updatedPartner.getShortName());
                    existing.setAddress(updatedPartner.getAddress());
                    existing.setPostCode(updatedPartner.getPostCode());
                    existing.setTaxNumber(updatedPartner.getTaxNumber());
                    existing.setShippmentContactPerson(updatedPartner.getShippmentContactPerson());
                    existing.setShippmentShortName(updatedPartner.getShippmentShortName());
                    existing.setShippmentPostCode(updatedPartner.getShippmentPostCode());
                    existing.setShippmentPhone(updatedPartner.getShippmentPhone());

                    if (updatedPartner.getContacts() != null) {
                        existing.getContacts().clear();
                        updatedPartner.getContacts().forEach(contact -> {
                            contact.setPartner(existing);
                            existing.getContacts().add(contact);
                        });
                    }

                    return partnerRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + id));
    }

    @Transactional
    public void bulkUpdate(List<PartnerModel> partners, PartnerModel updatedPartnerData) {
        var updatedPartners = new ArrayList<PartnerModel>();
        // remove null and empty values from updatedPartnerData
        partners.forEach(partner -> {
            updatedPartners.add(partnerRepository.findById(partner.getId()).map(existing -> {
                existing.setName(updatedPartnerData.getName() != null && !updatedPartnerData.getName().isEmpty()
                        ? updatedPartnerData.getName()
                        : existing.getName());
                existing.setShortName(
                        updatedPartnerData.getShortName() != null && !updatedPartnerData.getShortName().isEmpty()
                                ? updatedPartnerData.getShortName()
                                : existing.getShortName());
                existing.setAddress(
                        updatedPartnerData.getAddress() != null && !updatedPartnerData.getAddress().isEmpty()
                                ? updatedPartnerData.getAddress()
                                : existing.getAddress());
                existing.setPostCode(
                        updatedPartnerData.getPostCode() != null && !updatedPartnerData.getPostCode().isEmpty()
                                ? updatedPartnerData.getPostCode()
                                : existing.getPostCode());
                existing.setTaxNumber(
                        updatedPartnerData.getTaxNumber() != null && !updatedPartnerData.getTaxNumber().isEmpty()
                                ? updatedPartnerData.getTaxNumber()
                                : existing.getTaxNumber());
                existing.setShippmentContactPerson(updatedPartnerData.getShippmentContactPerson() != null
                        && !updatedPartnerData.getShippmentContactPerson().isEmpty()
                                ? updatedPartnerData.getShippmentContactPerson()
                                : existing.getShippmentContactPerson());
                existing.setShippmentShortName(updatedPartnerData.getShippmentShortName() != null
                        && !updatedPartnerData.getShippmentShortName().isEmpty()
                                ? updatedPartnerData.getShippmentShortName()
                                : existing.getShippmentShortName());
                existing.setShippmentPostCode(updatedPartnerData.getShippmentPostCode() != null
                        && !updatedPartnerData.getShippmentPostCode().isEmpty()
                                ? updatedPartnerData.getShippmentPostCode()
                                : existing.getShippmentPostCode());
                existing.setShippmentPhone(updatedPartnerData.getShippmentPhone() != null
                        && !updatedPartnerData.getShippmentPhone().isEmpty()
                                ? updatedPartnerData.getShippmentPhone()
                                : existing.getShippmentPhone());

                return existing;
            }).orElseThrow(() -> new RuntimeException("Partner not found with id: " + partner.getId())));
        });
        partnerRepository.saveAll(updatedPartners);
    }
}

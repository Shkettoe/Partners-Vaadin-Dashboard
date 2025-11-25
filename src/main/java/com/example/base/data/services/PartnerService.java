package com.example.base.data.services;

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
                    return partnerRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + id));
    }
}

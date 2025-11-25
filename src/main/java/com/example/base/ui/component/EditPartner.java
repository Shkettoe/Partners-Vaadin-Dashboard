package com.example.base.ui.component;

import java.util.Optional;

import com.example.base.data.models.PartnerModel;
import com.example.base.data.services.PartnerService;
import com.vaadin.flow.data.binder.Binder;

public class EditPartner {
    private final Optional<PartnerModel> partner;
    private final PartnerService partnerService;

    public EditPartner(Optional<PartnerModel> partner, PartnerService partnerService) {
        this.partner = partner;
        this.partnerService = partnerService;
    }
}

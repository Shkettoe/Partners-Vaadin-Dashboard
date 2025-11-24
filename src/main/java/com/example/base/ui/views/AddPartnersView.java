package com.example.base.ui.views;

import com.example.base.data.services.PartnerService;
import com.example.base.ui.component.PartnerComponent;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route("add-partners")
@RouteAlias("create-partners")
@Menu(title = "Dodaj partnerja")
public class AddPartnersView extends Main {

    private final PartnerService partnerService;

    public AddPartnersView(PartnerService partnerService) {
        this.partnerService = partnerService;

        var partnerComponent = new PartnerComponent();
        partnerComponent.setOnSave(partner -> {
            this.partnerService.save(partner);
            Notification.show("Partner je bil uspešno shranjen", 3000, Notification.Position.TOP_CENTER);
        });
        add(partnerComponent);
    }
}

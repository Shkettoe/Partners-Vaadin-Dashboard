package com.example.base.ui.views;

import java.util.Collection;

import com.example.base.data.models.ContactModel;
import com.example.base.data.models.PartnerModel;
import com.example.base.data.services.PartnerService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route(":id")
public class PartnerView extends Main implements BeforeEnterObserver {
    private final PartnerService partnerService;
    private Long partnerId;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.partnerId = Long.parseLong(event.getRouteParameters().get("id").get());
        renderPartner();
    }

    public PartnerView(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    private void renderPartner() {
        var partner = this.partnerService.findById(partnerId);
        var container = new Div();
        if (partner.isPresent()) {
            container.add(partnerDataFields(partner.get()));
            container.add(this.contactsGrid(partner.get().getContacts()));
            add(container);
        } else {
            Notification.show("No partner");
            add(new Paragraph("No partner"));
        }
    }

    private Div partnerDataFields(PartnerModel partner) {
        var partnerField = new Div();
        partnerField.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Width.FULL, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Gap.SMALL);

        partnerField.add(new Paragraph("Naziv: " + partner.getName()));
        partnerField.add(new Paragraph("Kratek naziv: " + partner.getShortName()));
        partnerField.add(new Paragraph("Naslov: " + partner.getAddress()));
        partnerField.add(new Paragraph("Pošta: " + partner.getPostCode()));
        partnerField.add(new Paragraph("Davčna številka: " + partner.getTaxNumber()));
        partnerField.add(new Paragraph("Kontaktna oseba: " + partner.getShippmentContactPerson()));
        partnerField.add(new Paragraph("Kratek naziv: " + partner.getShippmentShortName()));
        partnerField.add(new Paragraph("Pošta: " + partner.getShippmentPostCode()));
        partnerField.add(new Paragraph("Telefonska številka: " + partner.getShippmentPhone()));

        return partnerField;
    }

    private Grid<ContactModel> contactsGrid(Collection<ContactModel> contacts) {
        var contactsGrid = new Grid<>(ContactModel.class, false);

        contactsGrid.addColumn(ContactModel::getName).setHeader("Naziv");
        contactsGrid.addColumn(ContactModel::getPhone).setHeader("Telefon");
        contactsGrid.addColumn(ContactModel::getEmail).setHeader("Email");

        contactsGrid.setItems(contacts);

        return contactsGrid;
    }

}
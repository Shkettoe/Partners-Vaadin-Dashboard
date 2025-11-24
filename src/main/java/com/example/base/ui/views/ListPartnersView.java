package com.example.base.ui.views;

import com.example.base.data.models.PartnerModel;
import com.example.base.data.services.PartnerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("")
@RouteAlias("partners")
@Menu(order = 1, title = "Partnerji")
public class ListPartnersView extends Main {

        private final PartnerService partnerService;

        private Grid<PartnerModel> partnersGrid() {
                var grid = new Grid<PartnerModel>(PartnerModel.class, false);
                // grid.addItemClickListener(item -> {
                // UI.getCurrent().navigate(PartnerView.class, 1);
                // });
                grid.addColumn(PartnerModel::getName)
                                .setHeader("Naziv")
                                .setSortable(true)
                                .setAutoWidth(true);
                grid.addColumn(PartnerModel::getShortName)
                                .setHeader("Kratek naziv")
                                .setSortable(true)
                                .setAutoWidth(true);
                grid.addColumn(PartnerModel::getAddress)
                                .setHeader("Naslov")
                                .setAutoWidth(true);
                grid.addColumn(PartnerModel::getPostCode)
                                .setHeader("Pošta")
                                .setSortable(true)
                                .setWidth("120px");
                grid.addColumn(PartnerModel::getTaxNumber)
                                .setHeader("Davčna št.")
                                .setWidth("150px");
                grid.addClassNames(
                                LumoUtility.Border.ALL,
                                LumoUtility.BorderColor.CONTRAST_10);
                return grid;
        }

        public ListPartnersView(PartnerService partnerService) {
                this.partnerService = partnerService;

                var grid = partnersGrid();
                grid.setItems(this.partnerService.findAll());

                grid.addClassNames(
                                LumoUtility.Border.ALL,
                                LumoUtility.BorderColor.CONTRAST_10);

                add(grid);
        }
}

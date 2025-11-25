package com.example.base.ui.views;

import com.example.base.data.models.PartnerModel;
import com.example.base.data.services.PartnerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("")
@RouteAlias("partners")
@Menu(order = 1, title = "Partnerji")
public class ListPartnersView extends Main {

        private final PartnerService partnerService;

        private Grid<PartnerModel> partnersGrid() {
                var grid = new Grid<PartnerModel>(PartnerModel.class, false);
                grid.addItemDoubleClickListener(item -> {
                        UI.getCurrent().navigate(PartnerView.class,
                                        new RouteParameters("id", item.getItem().getId().toString()));
                });
                grid.addColumn(PartnerModel::getName)
                                .setHeader("Naziv")
                                .setSortable(true)
                                .setAutoWidth(true).setClassName("hoverable-column");
                ;
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
                grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
                grid.addClassName("hoverable-grid");
                grid.setSelectionMode(Grid.SelectionMode.MULTI);
                return grid;
        }

        private void editItem(PartnerModel item) {
                UI.getCurrent().navigate(PartnerView.class, new RouteParameters("id", item.getId().toString()));
        }

        private void removeItem(PartnerModel item) {
                // Are you sure you want to delete this item?
                var confirmDialog = new ConfirmDialog();
                confirmDialog.setHeader("Ali ste prepričani, da želite izbrisati tega partnerja?");
                confirmDialog.setConfirmText("Da");
                confirmDialog.setRejectable(true);
                confirmDialog.setRejectText("Ne");
                confirmDialog.addConfirmListener(e -> this.partnerService.delete(item.getId()));
                confirmDialog.addCancelListener(e -> Notification.show("Operacija je prekinjena", 3000,
                                Notification.Position.TOP_CENTER));
                confirmDialog.open();
        }

        public ListPartnersView(PartnerService partnerService) {
                this.partnerService = partnerService;

                addClassNames(LumoUtility.Padding.XLARGE, LumoUtility.Display.FLEX,
                                LumoUtility.FlexDirection.COLUMN,
                                LumoUtility.JustifyContent.BETWEEN,
                                LumoUtility.AlignItems.END,
                                LumoUtility.Gap.XLARGE);

                var grid = partnersGrid();
                grid.setItems(this.partnerService.findAll());
                grid.addColumn(new ComponentRenderer<Button, PartnerModel>(item -> {
                        var button = new Button(VaadinIcon.EDIT.create());
                        button.addThemeVariants(ButtonVariant.LUMO_WARNING);
                        button.addClickListener(e -> this.editItem(item));
                        return button;
                }));

                grid.addColumn(new ComponentRenderer<Button, PartnerModel>(item -> {
                        var button = new Button(VaadinIcon.TRASH.create());
                        button.addThemeVariants(ButtonVariant.LUMO_ERROR);
                        button.addClickListener(e -> this.removeItem(item));
                        return button;
                }));

                add(grid);
                var buttonsLayout = new HorizontalLayout();
                var editButton = new Button(VaadinIcon.EDIT.create());
                editButton.addThemeVariants(ButtonVariant.LUMO_WARNING);
                buttonsLayout.add(editButton);
                var deleteButton = new Button(VaadinIcon.TRASH.create());
                deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
                buttonsLayout.add(deleteButton);
                add(buttonsLayout);
        }
}

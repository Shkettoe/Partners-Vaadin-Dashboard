package com.example.base.ui.views;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.base.data.models.PartnerModel;
import com.example.base.data.services.PartnerService;
import com.example.base.ui.component.PartnerComponent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouteParameters;

@Route("")
@RouteAlias("partners")
@Menu(order = 1, title = "Partnerji")
public class ListPartnersView extends Main {

        // Dependencies
        private final PartnerService partnerService;

        // Components
        private final Grid<PartnerModel> grid = new Grid<PartnerModel>(PartnerModel.class, false);
        private final HorizontalLayout buttonsLayout = new HorizontalLayout();
        private final Button addButton = new Button(VaadinIcon.PLUS.create());
        private final Button editButton = new Button(VaadinIcon.EDIT.create());
        private final Button deleteButton = new Button(VaadinIcon.TRASH.create());

        /**
         * Constructor for ListPartnersView
         * 
         * @param partnerService PartnerService
         */
        public ListPartnersView(PartnerService partnerService) {
                this.partnerService = partnerService;

                setClassName("flex-view");

                // Grid
                configureGrid();

                setupButtons();
                add(grid, buttonsLayout);
                loadPartners();
        }

        /**
         * Setup buttons (WIP)
         */
        private void setupButtons() {
                addButton.addThemeVariants(ButtonVariant.LUMO_ICON);

                editButton.addThemeVariants(ButtonVariant.LUMO_WARNING);

                deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
                deleteButton.addClickListener(e -> {
                        Notification.show(
                                        String.format("%d partnerjev izbranih za brisanje",
                                                        grid.getSelectedItems().size()),
                                        3000, Notification.Position.TOP_CENTER);
                        removePartner(new ArrayList<>(
                                        grid.getSelectedItems().stream().map(PartnerModel::getId).toList()));
                });

                buttonsLayout.add(addButton, editButton, deleteButton);
        }

        /**
         * Grid for listing partners
         * 
         * @return Grid<PartnerModel>
         */
        private void configureGrid() {
                // Grid settings
                GridMultiSelectionModel<PartnerModel> selectionModel = (GridMultiSelectionModel<PartnerModel>) grid
                                .setSelectionMode(Grid.SelectionMode.MULTI);
                selectionModel.setDragSelect(true);
                grid.setRowsDraggable(true);

                // Event Listeners
                grid.addItemDoubleClickListener(item -> {
                        UI.getCurrent().navigate(PartnerView.class,
                                        new RouteParameters("id", item.getItem().getId().toString()));
                });

                // Columns
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

                // Actions column
                grid.addComponentColumn(item -> {
                        var editBtn = new Button(VaadinIcon.EDIT.create());
                        editBtn.addThemeVariants(ButtonVariant.LUMO_WARNING);
                        editBtn.addClickListener(e -> editPartner(item));
                        var deleteBtn = new Button(VaadinIcon.TRASH.create());
                        deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
                        deleteBtn.addClickListener(e -> removePartner(new ArrayList<>(Arrays.asList(item.getId()))));
                        var layout = new HorizontalLayout(editBtn, deleteBtn);
                        return layout;
                }).setHeader("Dejanje").setAutoWidth(true);

                // grid styling
                grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
                grid.addClassName("hoverable-grid");
        }

        /**
         * Load grid with partners
         */
        private void loadPartners() {
                grid.setItems(partnerService.findAll());
        }

        /**
         * Edit item
         * 
         * @param item PartnerModel
         */
        private void editPartner(PartnerModel partner) {
                var editDialog = new Dialog();
                var closeButtonLayout = new HorizontalLayout(
                                new Button(VaadinIcon.CLOSE.create(), e -> editDialog.close()));
                var partnerComponent = new PartnerComponent(partner, partner.getContacts());
                partnerComponent.setOnSave(p -> {
                        partnerService.update(partner.getId(), p);
                        loadPartners();
                        editDialog.close();
                });
                closeButtonLayout.setWidthFull();
                closeButtonLayout.setJustifyContentMode(JustifyContentMode.END);
                editDialog.setWidthFull();
                editDialog.add(closeButtonLayout, partnerComponent);
                editDialog.open();
        }

        /**
         * Remove item
         * 
         * @param item PartnerModel
         */
        private void removePartner(ArrayList<Long> ids) {
                // Are you sure dialog
                if (ids.size() < 1)
                        // No partners selected
                        return;
                else if (ids.size() > 1) {
                        // Remove multiple partners
                        var confirmDialog = new ConfirmDialog();
                        confirmDialog.setHeader(String.format("Ali ste prepričani, da želite izbrisati %d partnerjev?",
                                        ids.size()));
                        confirmDialog.setConfirmText("Da");
                        confirmDialog.setRejectable(true);
                        confirmDialog.setRejectText("Ne");
                        confirmDialog.addConfirmListener(e -> {
                                partnerService.bulkDelete(ids);
                                loadPartners();
                                Notification.show("Partnerji so bil uspešno izbrisani", 3000,
                                                Notification.Position.TOP_CENTER);
                        });
                        confirmDialog.addRejectListener(e -> Notification.show("Operacija je prekinjena", 3000,
                                        Notification.Position.TOP_CENTER));
                        confirmDialog.open();
                } else {
                        // Remove single partner
                        var confirmDialog = new ConfirmDialog();
                        confirmDialog.setHeader("Ali ste prepričani, da želite izbrisati tega partnerja?");
                        confirmDialog.setConfirmText("Da");
                        confirmDialog.setRejectable(true);
                        confirmDialog.setRejectText("Ne");
                        confirmDialog.addConfirmListener(e -> {
                                partnerService.delete(ids.get(0)); // Delete first and only item
                                loadPartners();
                                Notification.show("Partner je bil uspešno izbrisan", 3000,
                                                Notification.Position.TOP_CENTER);
                        });
                        confirmDialog.addRejectListener(e -> Notification.show("Operacija je prekinjena", 3000,
                                        Notification.Position.TOP_CENTER));
                        confirmDialog.open();

                }
        }
}

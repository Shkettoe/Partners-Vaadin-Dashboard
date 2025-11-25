package com.example.base.ui.views;

import com.example.base.data.models.PartnerModel;
import com.example.base.data.services.PartnerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouteParameters;

@Route("")
@RouteAlias("partners")
@Menu(order = 1, title = "Partnerji")
public class ListPartnersView extends Main {

        private final PartnerService partnerService;

        /**
         * Grid for listing partners
         * 
         * @return Grid<PartnerModel>
         */
        private Grid<PartnerModel> partnersGrid() {
                var grid = new Grid<PartnerModel>(PartnerModel.class, false);

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

                // grid styling
                grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
                grid.addClassName("hoverable-grid");

                return grid;
        }

        /**
         * Edit item
         * 
         * @param item PartnerModel
         */
        private void editItem(PartnerModel item) {
                UI.getCurrent().navigate(PartnerView.class, new RouteParameters("id", item.getId().toString()));
        }

        /**
         * Remove item
         * 
         * @param item PartnerModel
         */
        private void removeItem(PartnerModel item) {
                // Are you sure dialog
                var confirmDialog = new ConfirmDialog();
                confirmDialog.setHeader("Ali ste prepričani, da želite izbrisati tega partnerja?");
                confirmDialog.setConfirmText("Da");
                confirmDialog.setRejectable(true);
                confirmDialog.setRejectText("Ne");
                confirmDialog.addConfirmListener(e -> this.partnerService.delete(item.getId()));
                confirmDialog.addRejectListener(e -> Notification.show("Operacija je prekinjena", 3000,
                                Notification.Position.TOP_CENTER));
                confirmDialog.open();
        }

        /**
         * Constructor for ListPartnersView
         * 
         * @param partnerService PartnerService
         */
        public ListPartnersView(PartnerService partnerService) {
                this.partnerService = partnerService;

                setClassName("flex-view");

                // Grid
                var grid = partnersGrid();
                grid.setItems(this.partnerService.findAll());

                grid.addComponentColumn(item -> {
                        var editBtn = new Button(VaadinIcon.EDIT.create());
                        editBtn.addThemeVariants(ButtonVariant.LUMO_WARNING);
                        editBtn.addClickListener(e -> this.editItem(item));
                        var deleteBtn = new Button(VaadinIcon.TRASH.create());
                        deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
                        deleteBtn.addClickListener(e -> this.removeItem(item));
                        var layout = new HorizontalLayout(editBtn, deleteBtn);
                        return layout;
                }).setHeader("Dejanje").setAutoWidth(true);

                add(grid);

                // Buttons
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

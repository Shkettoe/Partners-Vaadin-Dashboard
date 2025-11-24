package com.example.base.ui.component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.example.base.data.models.ContactModel;
import com.example.base.data.models.PartnerModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class PartnerComponent extends Div {
    private Consumer<PartnerModel> onSave;

    public record ContactFields(TextField nameField, TextField phoneField, TextField emailField) {

    }

    private final List<ContactFields> contactFields = new ArrayList<>();

    public void setOnSave(Consumer<PartnerModel> onSave) {
        this.onSave = onSave;
    }

    private PartnerModel createPartnerModel() {
        try {
            var partnerModel = new PartnerModel();
            partnerBinder.writeBean(partnerModel);
            partnerModel.setName(nameField.getValue());
            partnerModel.setShortName(shortNameField.getValue());
            partnerModel.setAddress(addressField.getValue());
            partnerModel.setPostCode(postCodeField.getValue());
            partnerModel.setTaxNumber(taxNumberField.getValue());
            partnerModel.setShippmentContactPerson(shippmentNameField.getValue());
            partnerModel.setShippmentShortName(shippmentShortNameField.getValue());
            partnerModel.setShippmentPostCode(shippmentPostCodeField.getValue());
            partnerModel.setShippmentPhone(shippmentContactField.getValue());

            var contacts = new ArrayList<ContactModel>();
            this.contactFields.forEach(contactFields -> {
                try {
                    var contactModel = new ContactModel();
                    contactBinder.writeBean(contactModel);
                    contactModel.setName(contactFields.nameField().getValue());
                    contactModel.setPhone(contactFields.phoneField().getValue());
                    contactModel.setEmail(contactFields.emailField().getValue());
                    contactModel.setPartner(partnerModel);
                    contacts.add(contactModel);

                } catch (ValidationException e) {

                }
            });
            partnerModel.setContacts(contacts);

            return partnerModel;

        } catch (ValidationException e) {
            return null;
        }
    }

    private Div partnerDataFields() {
        var partnerField = new Div();
        partnerField.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Width.FULL, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Gap.SMALL);

        partnerField.add(nameField);
        partnerField.add(shortNameField);
        partnerField.add(addressField);
        partnerField.add(postCodeField);
        partnerField.add(taxNumberField);

        return partnerField;
    }

    private final Div contactsContainer = new Div();
    private TextField nameField = new TextField("Naziv");
    private TextField shortNameField = new TextField("Kratek Naziv");
    private TextField addressField = new TextField("Naslov");
    private TextField postCodeField = new TextField("Pošta");
    private TextField taxNumberField = new TextField("Davčna številka");
    private TextField shippmentNameField = new TextField("Naziv dostavne točke");
    private TextField shippmentShortNameField = new TextField("Kratek Naziv");
    private TextField shippmentPostCodeField = new TextField("Pošta");
    private TextField shippmentContactField = new TextField("Telefonska številka");

    private Button saveButton = new Button("Shrani", VaadinIcon.CHECK.create());
    private Button resetButton = new Button("Ponastavi", VaadinIcon.REFRESH.create());

    private Div createSingleContactFields() {
        var nameField = new TextField("Kontaktna oseba");
        var phoneField = new TextField("Telefon");
        var emailField = new TextField("Email");

        contactBinder.forField(nameField).asRequired("Naziv je obvezno polje").bind(ContactModel::getName,
                ContactModel::setName);
        contactBinder.forField(phoneField).asRequired("Telefon je obvezno polje")
                .withValidator(new RegexpValidator("Nepravilna telefonska številka", "\\d{9}"))
                .bind(ContactModel::getPhone, ContactModel::setPhone);
        contactBinder.forField(emailField).asRequired("Email je obvezno polje")
                .withValidator(new RegexpValidator("Nepravilna email naslov",
                        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
                .bind(ContactModel::getEmail, ContactModel::setEmail);

        var contactFields = new ContactFields(nameField, phoneField, emailField);
        this.contactFields.add(contactFields);

        var contactDiv = new Div();
        contactDiv.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Gap.SMALL);

        var removeButton = new Button(VaadinIcon.MINUS.create());
        removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
        removeButton.addClickListener(e -> {
            contactsContainer.remove(contactDiv);
            this.contactFields.remove(contactFields);
        });

        var fieldsLayout = new Div(contactFields.nameField(), contactFields.phoneField(), contactFields.emailField());
        fieldsLayout.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Gap.SMALL);

        var removeButtonLayout = new HorizontalLayout();
        removeButtonLayout.setWidthFull();
        removeButtonLayout.setJustifyContentMode(JustifyContentMode.END);
        removeButtonLayout.add(removeButton);

        contactDiv.add(removeButtonLayout, fieldsLayout);
        return contactDiv;
    }

    private Div contactsForm() {
        var contactsForm = new Div();
        contactsForm.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Width.FULL, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.AlignItems.STRETCH, LumoUtility.Gap.MEDIUM);

        contactsContainer.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Gap.SMALL);

        var addButton = new Button("Dodaj Kontakt", VaadinIcon.PLUS.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> {
            contactsContainer.add(createSingleContactFields());
        });

        contactsForm.add(contactsContainer, addButton);
        return contactsForm;
    }

    private Div shippmentDataForm() {
        var shippmentForm = new Div();
        shippmentForm.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Width.FULL, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Gap.SMALL);

        shippmentForm.add(shippmentNameField, shippmentShortNameField, shippmentPostCodeField, shippmentContactField);

        return shippmentForm;
    }

    private HorizontalLayout headersLayout() {
        var headersLayout = new HorizontalLayout();
        var partnerHeader = new HorizontalLayout();
        var contactsHeader = new HorizontalLayout();
        var shippmentHeader = new HorizontalLayout();
        partnerHeader.setWidthFull();
        contactsHeader.setWidthFull();
        shippmentHeader.setWidthFull();
        partnerHeader.setJustifyContentMode(JustifyContentMode.START);
        contactsHeader.setJustifyContentMode(JustifyContentMode.START);
        shippmentHeader.setJustifyContentMode(JustifyContentMode.START);
        partnerHeader.add(new H2("Osnovni podatki"));
        contactsHeader.add(new H2("Kontaktni podatki"));
        shippmentHeader.add(new H2("Dostavni podatki"));
        headersLayout.setWidthFull();
        headersLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        headersLayout.add(partnerHeader, contactsHeader, shippmentHeader);

        return headersLayout;
    }

    private void clearForm() {
        nameField.setValue("");
        shortNameField.setValue("");
        addressField.setValue("");
        postCodeField.setValue("");
        taxNumberField.setValue("");
        shippmentNameField.setValue("");
        shippmentShortNameField.setValue("");
        shippmentPostCodeField.setValue("");
        shippmentContactField.setValue("");
        this.contactFields.clear();
        this.contactsContainer.removeAll();

        partnerBinder.refreshFields();
        contactBinder.refreshFields();
    }

    private HorizontalLayout buttonContainer() {
        var buttonContainer = new HorizontalLayout();
        buttonContainer.setWidthFull();
        buttonContainer.setJustifyContentMode(JustifyContentMode.END);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> {
            if (onSave != null) {
                onSave.accept(createPartnerModel());
                clearForm();
            }
        });

        resetButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        resetButton.addClickListener(e -> {
            clearForm();
        });

        buttonContainer.add(saveButton, resetButton);
        return buttonContainer;
    }

    private final Binder<PartnerModel> partnerBinder = new Binder<>(PartnerModel.class);
    private final Binder<ContactModel> contactBinder = new Binder<>(ContactModel.class);

    private void bindPartnerData() {
        partnerBinder.forField(nameField).asRequired("Naziv je obvezno polje").bind(PartnerModel::getName,
                PartnerModel::setName);

        partnerBinder.forField(shortNameField).asRequired("Kratek naziv je obvezno polje")
                .bind(PartnerModel::getShortName, PartnerModel::setShortName);

        partnerBinder.forField(addressField).asRequired("Naslov je obvezno polje")
                .bind(PartnerModel::getAddress,
                        PartnerModel::setAddress);

        partnerBinder.forField(postCodeField).asRequired("Pošta je obvezno polje")
                .withValidator(new RegexpValidator("Nepravilna poštna številka", "\\d{4}"))
                .bind(PartnerModel::getPostCode,
                        PartnerModel::setPostCode);

        partnerBinder.forField(taxNumberField).asRequired("Davčna številka je obvezno polje")
                .withValidator(new RegexpValidator("Nepravilna davčna številka", "\\d{8}"))
                .bind(PartnerModel::getTaxNumber, PartnerModel::setTaxNumber);

        partnerBinder.forField(shippmentNameField).asRequired("Kontaktna oseba je obvezno polje")
                .bind(PartnerModel::getShippmentContactPerson,
                        PartnerModel::setShippmentContactPerson);

        partnerBinder.forField(shippmentShortNameField).asRequired("Kratek naziv je obvezno polje")
                .bind(PartnerModel::getShippmentShortName,
                        PartnerModel::setShippmentShortName);

        partnerBinder.forField(shippmentPostCodeField).asRequired("Pošta je obvezno polje")
                .withValidator(new RegexpValidator("Nepravilna poštna številka", "\\d{4}"))
                .bind(PartnerModel::getShippmentPostCode,
                        PartnerModel::setShippmentPostCode);

        partnerBinder.forField(shippmentContactField).asRequired("Telefonska številka je obvezno polje")
                .withValidator(new RegexpValidator("Nepravilna telefonska številka", "\\d{9}"))
                .bind(PartnerModel::getShippmentPhone,
                        PartnerModel::setShippmentPhone);
    }

    public PartnerComponent() {
        this.addClassNames(LumoUtility.Height.SCREEN, LumoUtility.Padding.XLARGE);
        var partnerComponent = new Div();
        partnerComponent.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.ROW,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.Gap.XLARGE, LumoUtility.Margin.Vertical.LARGE);

        bindPartnerData();

        partnerComponent.add(this.partnerDataFields(), this.contactsForm(), shippmentDataForm());
        add(headersLayout(), partnerComponent, buttonContainer());
    }
}

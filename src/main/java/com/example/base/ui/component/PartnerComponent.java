package com.example.base.ui.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import com.example.base.data.models.ContactModel;
import com.example.base.data.models.PartnerModel;
import com.example.base.ui.converters.PhoneNumberConverter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class PartnerComponent extends Div {
    // Configuration
    private boolean requiredFields = false;

    // Callback
    private Consumer<PartnerModel> onSave;

    // Binders
    private final Binder<PartnerModel> partnerBinder = new Binder<>(PartnerModel.class);

    // Input fields
    private final TextField nameField = new TextField("Naziv");
    private final TextField shortNameField = new TextField("Kratek Naziv");
    private final TextField addressField = new TextField("Naslov");
    private final TextField postCodeField = new TextField("Pošta");
    private final TextField taxNumberField = new TextField("Davčna številka");
    private final TextField shippmentNameField = new TextField("Naziv dostavne točke");
    private final TextField shippmentShortNameField = new TextField("Kratek Naziv");
    private final TextField shippmentPostCodeField = new TextField("Pošta");
    private final TextField shippmentContactField = new TextField("Telefonska številka");

    // Contact management
    private final Div contactsContainer = new Div();
    private final List<ContactFieldSet> contactFieldSets = new ArrayList<>();

    private class ContactFieldSet {
        private final TextField nameField;
        private final TextField phoneField;
        private final EmailField emailField;
        private final Binder<ContactModel> binder; // ✅ Each contact has own binder!
        private final Div container;

        public ContactFieldSet() {
            this.binder = new Binder<>(ContactModel.class);

            // Create fields
            this.nameField = new TextField("Kontaktna oseba");
            this.phoneField = new TextField("Telefon");
            this.emailField = new EmailField("Email");

            // Configure binder for this contact
            configureBinder();

            // Build UI
            this.container = buildContainer();
        }

        private void configureBinder() {
            binder.forField(nameField)
                    .asRequired("Ime je obvezno")
                    .bind(ContactModel::getName, ContactModel::setName);

            binder.forField(phoneField)
                    .asRequired("Telefon je obvezen")
                    .withConverter(new PhoneNumberConverter("SI"))
                    .bind(ContactModel::getPhone, ContactModel::setPhone);

            binder.forField(emailField)
                    .asRequired("Email je obvezen")
                    .withValidator(new EmailValidator("Neveljaven email naslov"))
                    .bind(ContactModel::getEmail, ContactModel::setEmail);
        }

        private Div buildContainer() {
            var container = new Div();
            container.addClassNames(
                    LumoUtility.Display.FLEX,
                    LumoUtility.FlexDirection.COLUMN,
                    LumoUtility.Gap.SMALL,
                    LumoUtility.Padding.SMALL,
                    LumoUtility.Border.ALL,
                    LumoUtility.BorderColor.CONTRAST_10,
                    LumoUtility.BorderRadius.MEDIUM);

            // Remove button
            var removeButton = new Button(VaadinIcon.MINUS.create());
            removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            removeButton.addClickListener(e -> removeContact(this));

            var removeButtonLayout = new HorizontalLayout(removeButton);
            removeButtonLayout.setWidthFull();
            removeButtonLayout.setJustifyContentMode(JustifyContentMode.END);

            // Fields layout
            var fieldsLayout = new Div(nameField, phoneField, emailField);
            fieldsLayout.addClassNames(
                    LumoUtility.Display.FLEX,
                    LumoUtility.FlexDirection.COLUMN,
                    LumoUtility.Gap.SMALL);

            container.add(removeButtonLayout, fieldsLayout);
            return container;
        }

        public ContactModel toContactModel() throws ValidationException {
            var contact = new ContactModel();
            binder.writeBean(contact);
            return contact;
        }

        public boolean isValid() {
            return binder.validate().isOk();
        }

        public Div getContainer() {
            return container;
        }
    }

    // Buttons
    private final Button saveButton = new Button("Shrani", VaadinIcon.CHECK.create());
    private final Button resetButton = new Button("Ponastavi", VaadinIcon.REFRESH.create());

    /**
     * Constructor for the PartnerComponent.
     */
    public PartnerComponent(final boolean requiredFields) {
        this.requiredFields = requiredFields;
        addClassNames(LumoUtility.Height.SCREEN, LumoUtility.Padding.XLARGE);

        configurePartnerBinder();
        configureButtons();

        var mainLayout = new Div();
        mainLayout.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.ROW,
                LumoUtility.AlignItems.START,
                LumoUtility.Gap.XLARGE,
                LumoUtility.Margin.Vertical.LARGE);

        mainLayout.add(createPartnerSection());
        if (requiredFields) {
            mainLayout.add(createContactsSection());
        }
        mainLayout.add(createShippingSection());
        add(createHeadersLayout(), mainLayout, createButtonsLayout());
    }

    public PartnerComponent(PartnerModel partner) {
        this(true);
        partnerBinder.readBean(partner);
    }

    public PartnerComponent(PartnerModel partner, Collection<ContactModel> contacts) {
        this(partner);
        for (ContactModel contact : contacts) {
            var fieldSet = new ContactFieldSet();
            fieldSet.binder.readBean(contact);
            contactFieldSets.add(fieldSet);
            contactsContainer.add(fieldSet.getContainer());
        }
    }

    private <T> Binder.BindingBuilder<T, String> configureFieldBinding(Binder<T> binder, TextField field,
            String errorMessage) {
        var binding = binder.forField(field);
        if (requiredFields)
            binding.asRequired(errorMessage);
        return binding;
    }

    /**
     * Configures the partner binder.
     */
    private void configurePartnerBinder() {
        configureFieldBinding(partnerBinder, nameField, "Naziv je obvezno polje").bind(PartnerModel::getName,
                PartnerModel::setName);
        configureFieldBinding(partnerBinder, shortNameField, "Kratek naziv je obvezno polje").bind(
                PartnerModel::getShortName,
                PartnerModel::setShortName);
        configureFieldBinding(partnerBinder, addressField, "Naslov je obvezno polje").bind(PartnerModel::getAddress,
                PartnerModel::setAddress);
        configureFieldBinding(partnerBinder, postCodeField, "Pošta je obvezno polje").bind(PartnerModel::getPostCode,
                PartnerModel::setPostCode);
        configureFieldBinding(partnerBinder, taxNumberField, "Davčna številka je obvezno polje").bind(
                PartnerModel::getTaxNumber,
                PartnerModel::setTaxNumber);
        configureFieldBinding(partnerBinder, shippmentNameField, "Kontaktna oseba je obvezno polje").bind(
                PartnerModel::getShippmentContactPerson,
                PartnerModel::setShippmentContactPerson);
        configureFieldBinding(partnerBinder, shippmentShortNameField, "Kratek naziv je obvezno polje").bind(
                PartnerModel::getShippmentShortName,
                PartnerModel::setShippmentShortName);
        configureFieldBinding(partnerBinder, shippmentPostCodeField, "Pošta je obvezno polje").bind(
                PartnerModel::getShippmentPostCode,
                PartnerModel::setShippmentPostCode);
        configureFieldBinding(partnerBinder, shippmentContactField, "Telefonska številka je obvezno polje").bind(
                PartnerModel::getShippmentPhone,
                PartnerModel::setShippmentPhone);
    }

    /**
     * Configures save and reset buttons.
     */
    private void configureButtons() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> handleSave());

        resetButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        resetButton.addClickListener(e -> handleReset());
    }

    /**
     * Creates the headers layout with appropriate titles.
     */
    private HorizontalLayout createHeadersLayout() {
        var headers = new HorizontalLayout();
        headers.setWidthFull();
        headers.addClassNames(LumoUtility.JustifyContent.BETWEEN, LumoUtility.Gap.XLARGE);

        var partnerHeader = new Div(new H2("Osnovni podatki"));
        var shippingHeader = new Div(new H2("Dostavni podatki"));

        partnerHeader.setWidthFull();
        shippingHeader.setWidthFull();

        headers.add(partnerHeader);
        if (requiredFields) {
            var contactsHeader = new Div(new H2("Kontaktni podatki"));
            contactsHeader.setWidthFull();
            headers.add(contactsHeader);
        }

        headers.add(shippingHeader);
        return headers;
    }

    /**
     * Partner data, name, address, etc.
     * 
     * @return Div
     */
    private Div createPartnerSection() {
        var section = new Div();
        section.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.Width.FULL,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Gap.SMALL);

        section.add(nameField, shortNameField, addressField, postCodeField, taxNumberField);
        return section;
    }

    /**
     * Expandable contacts form.
     * 
     * @return Div
     */
    private Div createContactsSection() {
        var section = new Div();
        section.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.Width.FULL,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.AlignItems.STRETCH,
                LumoUtility.Gap.MEDIUM);

        contactsContainer.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Gap.SMALL);

        var addButton = new Button("Dodaj Kontakt", VaadinIcon.PLUS.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> addContact());

        section.add(contactsContainer, addButton);
        return section;
    }

    /**
     * Shipping data, name, address, etc.
     * 
     * @return Div
     */
    private Div createShippingSection() {
        var section = new Div();
        section.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.Width.FULL,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Gap.SMALL);

        section.add(shippmentNameField, shippmentShortNameField,
                shippmentPostCodeField, shippmentContactField);
        return section;
    }

    /**
     * Buttons layout for save and reset buttons pushed to the right.
     * 
     * @return HorizontalLayout
     */
    private HorizontalLayout createButtonsLayout() {
        var layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setJustifyContentMode(JustifyContentMode.END);
        layout.add(saveButton, resetButton);
        return layout;
    }

    /**
     * Adds a new contact field set to the contacts container.
     */
    private void addContact() {
        var fieldSet = new ContactFieldSet();
        contactFieldSets.add(fieldSet);
        contactsContainer.add(fieldSet.getContainer());
    }

    /**
     * Removes a contact field set from the contacts container.
     * 
     * @param fieldSet ContactFieldSet to remove
     */
    private void removeContact(ContactFieldSet fieldSet) {
        contactsContainer.remove(fieldSet.getContainer());
        contactFieldSets.remove(fieldSet);
    }

    /**
     * Handles the save action.
     */
    private void handleSave() {
        if (!validateAll()) {
            return;
        }

        try {
            var partner = createPartner();

            if (onSave != null) {
                onSave.accept(partner);
                handleReset();
            }
        } catch (ValidationException e) {
            Notification.show("Napaka pri shranjevanju: " + e.getMessage(), 3000,
                    Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Validates all the data in the form.
     * 
     * @return boolean
     */
    private boolean validateAll() {
        if (!partnerBinder.validate().isOk()) {
            Notification.show("Prosim preverite osnovne podatke", 3000,
                    Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }

        for (ContactFieldSet fieldSet : contactFieldSets) {
            if (!fieldSet.isValid()) {
                Notification.show("Prosim preverite kontaktne podatke", 3000,
                        Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return false;
            }
        }

        return true;
    }

    /**
     * Creates a new Partner from the form data.
     * 
     * @return PartnerModel
     * @throws ValidationException
     */
    private PartnerModel createPartner() throws ValidationException {
        var partner = new PartnerModel();

        // populates partner variable with the data from the form, or throws an error if
        // validation fails
        partnerBinder.writeBean(partner);

        var contacts = new ArrayList<ContactModel>();
        for (ContactFieldSet fieldSet : contactFieldSets) {
            var contact = fieldSet.toContactModel();
            contact.setPartner(partner);
            contacts.add(contact);
        }
        partner.setContacts(contacts);

        return partner;
    }

    /**
     * Resets the form
     */
    private void handleReset() {
        partnerBinder.readBean(new PartnerModel());

        contactsContainer.removeAll();
        contactFieldSets.clear();
    }

    /**
     * Sets the onSave callback.
     * 
     * @param onSave
     */
    public void setOnSave(Consumer<PartnerModel> onSave) {
        this.onSave = onSave;
    }
}

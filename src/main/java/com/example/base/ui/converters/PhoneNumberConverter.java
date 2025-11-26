package com.example.base.ui.converters;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class PhoneNumberConverter implements Converter<String, String> {

    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    private final String defaultRegion;

    public PhoneNumberConverter(String defaultRegion) {
        this.defaultRegion = defaultRegion;
    }

    @Override
    public Result<String> convertToModel(String displayValue, ValueContext context) {
        if (displayValue == null || displayValue.trim().isEmpty()) {
            return Result.ok("");
        }

        try {
            PhoneNumber number = phoneUtil.parse(displayValue, defaultRegion);

            if (!phoneUtil.isValidNumber(number)) {
                return Result.error("Neveljaven telefon");
            }

            // Format in E164 format: +38631123456
            String normalized = phoneUtil.format(number,
                    PhoneNumberUtil.PhoneNumberFormat.E164);
            return Result.ok(normalized);

        } catch (NumberParseException e) {
            return Result.error("Nepravilna telefonska številka");
        }
    }

    @Override
    public String convertToPresentation(String storedValue, ValueContext context) {
        if (storedValue == null || storedValue.trim().isEmpty()) {
            return "";
        }

        try {
            PhoneNumber number = phoneUtil.parse(storedValue, defaultRegion);
            // Format for display: +386 31 123 456
            return phoneUtil.format(number,
                    PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        } catch (NumberParseException e) {
            return storedValue; // Return as-is if parsing fails
        }
    }
}

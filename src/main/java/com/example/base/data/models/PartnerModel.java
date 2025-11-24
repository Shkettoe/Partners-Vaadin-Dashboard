package com.example.base.data.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Partners")
public class PartnerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String shortName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String postCode;

    @Column(nullable = false)
    private String taxNumber;

    // Shippment info

    @Column(nullable = false)
    private String shippmentContactPerson;

    @Column(nullable = false)
    private String shippmentShortName;

    @Column(nullable = false)
    private String shippmentPostCode;

    @Column(nullable = false)
    private String shippmentPhone;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ContactModel> contacts;

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getAddress() {
        return address;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public String getShippmentContactPerson() {
        return shippmentContactPerson;
    }

    public String getShippmentShortName() {
        return shippmentShortName;
    }

    public String getShippmentPostCode() {
        return shippmentPostCode;
    }

    public String getShippmentPhone() {
        return shippmentPhone;
    }

    public List<ContactModel> getContacts() {
        return contacts;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public void setShippmentContactPerson(String contactPerson) {
        this.shippmentContactPerson = contactPerson;
    }

    public void setShippmentShortName(String contactPersonNick) {
        this.shippmentShortName = contactPersonNick;
    }

    public void setShippmentPostCode(String contactPersonPostCode) {
        this.shippmentPostCode = contactPersonPostCode;
    }

    public void setShippmentPhone(String contactPhone) {
        this.shippmentPhone = contactPhone;
    }

    public void setContacts(List<ContactModel> contacts) {
        this.contacts = contacts;
    }
}

package com.ecommerce.ecommerce.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class OrderAddress {

    @Column(name = "ADDRESS_LINE", nullable = false)
    private String addressLine;

    @Column(name = "CITY", nullable = false)
    private String city;

    @Column(name = "STATE", nullable = false)
    private String state;

    @Column(name = "ZIP_CODE", nullable = false)
    private String zipCode;

    @Column(name = "COUNTRY", nullable = false)
    private String country;

    @Column(name = "LABEL")
    private String label;
}


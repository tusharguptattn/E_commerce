package com.ecommerce.ecommerce.enums;

public enum Roles {
    USER,
    ADMIN,
    SELLER;

    public String authority() {
        return "ROLE_" + this.name();
    }
}

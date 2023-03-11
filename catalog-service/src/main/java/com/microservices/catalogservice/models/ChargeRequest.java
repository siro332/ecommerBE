package com.microservices.catalogservice.models;

import lombok.Data;

@Data
public class ChargeRequest {

    public enum Currency {
        VND, USD;
    }
    private String description;
    private int amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;
}
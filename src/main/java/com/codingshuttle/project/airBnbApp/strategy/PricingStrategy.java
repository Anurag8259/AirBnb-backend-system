package com.codingshuttle.project.airBnbApp.strategy;

import com.codingshuttle.project.airBnbApp.entity.Inventory;

import java.math.BigDecimal;


public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}

package com.codingshuttle.project.airBnbApp.strategy;

import com.codingshuttle.project.airBnbApp.entity.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public class HolidayPricingStrategy implements PricingStrategy{

    private final PricingStrategy wrapped;

    public HolidayPricingStrategy(PricingStrategy wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price=wrapped.calculatePrice(inventory);
        boolean isTodayHoliday=true; //call an API or check with local data
        if(isTodayHoliday){
            price=price.multiply(BigDecimal.valueOf(1.25));
        }
        return price;
    }
}

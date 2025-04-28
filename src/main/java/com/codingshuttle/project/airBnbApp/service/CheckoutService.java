package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.entity.Booking;

public interface CheckoutService {

    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);

}

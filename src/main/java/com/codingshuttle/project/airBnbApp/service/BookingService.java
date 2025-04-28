package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.BookingDTO;
import com.codingshuttle.project.airBnbApp.dto.BookingRequest;
import com.codingshuttle.project.airBnbApp.dto.GuestDTO;

import java.util.List;

public interface BookingService {

    BookingDTO initializeBooking(BookingRequest bookingRequest);

    BookingDTO addGuests(Long bookingId, List<GuestDTO> guestDtoList);

    String initiatePayment(Long bookingId);
}

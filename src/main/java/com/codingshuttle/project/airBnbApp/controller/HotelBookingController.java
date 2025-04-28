package com.codingshuttle.project.airBnbApp.controller;

import com.codingshuttle.project.airBnbApp.dto.BookingDTO;
import com.codingshuttle.project.airBnbApp.dto.BookingRequest;
import com.codingshuttle.project.airBnbApp.dto.GuestDTO;
import com.codingshuttle.project.airBnbApp.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class HotelBookingController {

    private final BookingService bookingService;

    public HotelBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/init")
    public ResponseEntity<BookingDTO> initializeBooking(@RequestBody BookingRequest bookingRequest){
        return ResponseEntity.ok(bookingService.initializeBooking(bookingRequest));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDTO> addGuests(@PathVariable Long bookingId, @RequestBody List<GuestDTO> guestDtoList){
        return ResponseEntity.ok(bookingService.addGuests(bookingId,guestDtoList));
    }

    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<Map<String,String>> initiatePayment(@PathVariable Long bookingId){
        String sessionUrl=bookingService.initiatePayment(bookingId);
        return ResponseEntity.ok(Map.of("sessionUrl",sessionUrl));
    }
}

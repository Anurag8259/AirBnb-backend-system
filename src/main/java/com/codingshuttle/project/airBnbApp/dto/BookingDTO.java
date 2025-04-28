package com.codingshuttle.project.airBnbApp.dto;

import com.codingshuttle.project.airBnbApp.entity.Guest;
import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.entity.Room;
import com.codingshuttle.project.airBnbApp.entity.User;
import com.codingshuttle.project.airBnbApp.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDTO {


    private Long id;

    private Integer roomCount;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    private BookingStatus bookingStatus;

    private Set<Guest> guests;
}

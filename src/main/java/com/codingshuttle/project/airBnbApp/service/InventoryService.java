package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.HotelDTO;
import com.codingshuttle.project.airBnbApp.dto.HotelPriceDTO;
import com.codingshuttle.project.airBnbApp.dto.HotelSearchRequest;
import com.codingshuttle.project.airBnbApp.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelPriceDTO> searchHotels(HotelSearchRequest hotelSearchRequest);
}

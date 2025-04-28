package com.codingshuttle.project.airBnbApp.service;


import com.codingshuttle.project.airBnbApp.dto.HotelDTO;
import com.codingshuttle.project.airBnbApp.dto.HotelInfoDTO;

import java.util.List;

public interface HotelService {
    HotelDTO createNewHotel(HotelDTO hotelDTO);
    HotelDTO getHotelById(Long id);
    HotelDTO updateHotelById(Long id,HotelDTO hotelDTO);
    void deleteHotelById(Long id);
    void activateHotel(Long hotelId);

    List<HotelDTO> getAllHotels();

    HotelInfoDTO getHotelInfoById(Long hotelId);
}

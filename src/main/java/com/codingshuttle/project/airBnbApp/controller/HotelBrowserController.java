package com.codingshuttle.project.airBnbApp.controller;

import com.codingshuttle.project.airBnbApp.dto.HotelDTO;
import com.codingshuttle.project.airBnbApp.dto.HotelInfoDTO;
import com.codingshuttle.project.airBnbApp.dto.HotelPriceDTO;
import com.codingshuttle.project.airBnbApp.dto.HotelSearchRequest;
import com.codingshuttle.project.airBnbApp.service.HotelService;
import com.codingshuttle.project.airBnbApp.service.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/hotels")
public class HotelBrowserController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    public HotelBrowserController(HotelService hotelService,InventoryService inventoryService){
        this.inventoryService=inventoryService;
        this.hotelService=hotelService;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDTO>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest){
        var page =inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDTO> getHotelInfo(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }

}

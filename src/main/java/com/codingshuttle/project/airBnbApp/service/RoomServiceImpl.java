package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.RoomDTO;
import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.entity.Inventory;
import com.codingshuttle.project.airBnbApp.entity.Room;
import com.codingshuttle.project.airBnbApp.entity.User;
import com.codingshuttle.project.airBnbApp.exception.ResourceNotFoundException;
import com.codingshuttle.project.airBnbApp.exception.UnauthorizedException;
import com.codingshuttle.project.airBnbApp.repository.HotelRepository;
import com.codingshuttle.project.airBnbApp.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper mapper;
    private final InventoryService inventoryService;

    public RoomServiceImpl(RoomRepository roomRepository,ModelMapper mapper,HotelRepository hotelRepository,InventoryService inventoryService){
        this.roomRepository=roomRepository;
        this.mapper=mapper;
        this.inventoryService=inventoryService;
        this.hotelRepository=hotelRepository;
    }

    @Override
    public RoomDTO createNewRoom(Long hotelId,RoomDTO roomDTO) {
        log.info("Creating a new room in hotel with ID: {}", hotelId);
        Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id: "+hotelId));


        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnauthorizedException("This user does not own this hotel with id:"+hotelId);
        }
        Room room=mapper.map(roomDTO,Room.class);
        room.setHotel(hotel);
        room=roomRepository.save(room);

        if(hotel.getActive()){
            inventoryService.initializeRoomForAYear(room);
        }
        return mapper.map(room,RoomDTO.class);
    }

    @Override
    public List<RoomDTO> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting all the rooms in hotel with ID: {}", hotelId);
        Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id: "+hotelId));


        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnauthorizedException("This user does not own this hotel with id:"+hotelId);
        }
        return hotel.getRooms()
                .stream()
                .map((element)->mapper.map(element,RoomDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDTO getRoomById(Long roomId) {
        log.info("Getting the room with ID: {}", roomId);
        Room room=roomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("Room not found with id: "+roomId));


        return mapper.map(room,RoomDTO.class);
    }

    @Override
    @Transactional
    public void deleteRoomById(Long roomId) {
        log.info("Deleting the room with ID: {}",roomId);
        Room room=roomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("Room not found with id: "+roomId));

        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(room.getHotel().getOwner())){
            throw new UnauthorizedException("This user does not own this room with id:"+roomId);
        }
        inventoryService.deleteAllInventories(room);
        roomRepository.deleteById(roomId);

        //delete all future inventory for this room


    }
}

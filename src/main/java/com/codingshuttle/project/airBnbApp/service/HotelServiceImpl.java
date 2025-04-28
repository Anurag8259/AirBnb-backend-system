package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.HotelDTO;
import com.codingshuttle.project.airBnbApp.dto.HotelInfoDTO;
import com.codingshuttle.project.airBnbApp.dto.RoomDTO;
import com.codingshuttle.project.airBnbApp.entity.Hotel;
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
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final ModelMapper mapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;

    public HotelServiceImpl(RoomRepository roomRepository,HotelRepository hotelRepository,ModelMapper mapper,InventoryService inventoryService){
        this.hotelRepository=hotelRepository;
        this.mapper=mapper;
        this.inventoryService=inventoryService;
        this.roomRepository=roomRepository;
    }

    @Override
    public HotelDTO createNewHotel(HotelDTO hotelDTO) {
        log.info("Creating new hotel with name :{}",hotelDTO.getName());
        Hotel hotel=mapper.map(hotelDTO,Hotel.class);
        hotel.setActive(false);

        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);


        hotel=hotelRepository.save(hotel);
        log.info("Created a new hotel with id :{}",hotelDTO.getId());
        return mapper.map(hotel,HotelDTO.class);
    }

    @Override
    public HotelDTO getHotelById(Long id) {
        log.info("Getting the hotel with id :{}",id);
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id: "+id));

        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnauthorizedException("This user does not own this hotel with id:"+id);
        }

        return mapper.map(hotel,HotelDTO.class);
    }

    @Override
    public HotelDTO updateHotelById(Long id, HotelDTO hotelDTO) {
        log.info("Updating the hotel with id :{}",id);
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id: "+id));

        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnauthorizedException("This user does not own this hotel with id:"+id);
        }

        mapper.map(hotelDTO,hotel);
        hotel.setId(id);
        hotelRepository.save(hotel);
        return mapper.map(hotel,HotelDTO.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id: "+id));

        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnauthorizedException("This user does not own this hotel with id:"+id);
        }

        //delete the future inventories
        for(Room room : hotel.getRooms()){
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());
        }
        hotelRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Activating the hotel with id :{}",hotelId);
        Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id: "+hotelId));
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnauthorizedException("This user does not own this hotel with id:"+hotelId);
        }

        hotel.setActive(true);

        //assuming first time
        for(Room room : hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }
    }

    @Override
    public List<HotelDTO> getAllHotels() {

        return hotelRepository.findAll()
                .stream()
                .map((element)->mapper.map(element,HotelDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public HotelInfoDTO getHotelInfoById(Long hotelId) {
        Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id: "+hotelId));

        List<RoomDTO> rooms=hotel.getRooms()
                .stream()
                .map((element)->mapper.map(element,RoomDTO.class))
                .toList();

        return new HotelInfoDTO(mapper.map(hotel,HotelDTO.class),rooms);
    }


}

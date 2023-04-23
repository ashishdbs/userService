package com.user.service.userservice;


import com.user.service.userservice.entities.Hotel;
import com.user.service.userservice.externalService.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HotelServiceTest {

    @Autowired
    HotelService hotelService;

    @Test
    public void createHotelTest(){
        Hotel hotel = Hotel.builder()
                .name("Raj Mahal")
                .location("Lucknow")
                .about("Good Place")
                .build();

        hotelService.create(hotel);
    }
}

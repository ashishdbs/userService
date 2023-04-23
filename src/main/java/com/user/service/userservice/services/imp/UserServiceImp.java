package com.user.service.userservice.services.imp;

import com.user.service.userservice.entities.Hotel;
import com.user.service.userservice.entities.Rating;
import com.user.service.userservice.entities.User;
import com.user.service.userservice.exception.ResourceNotFoundException;
import com.user.service.userservice.externalService.HotelService;
import com.user.service.userservice.repository.UserRepository;
import com.user.service.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    @Override
    public User saveUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("user with given id is not found on server !! : " + userId));

        Rating[] rattingOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+userId, Rating[].class);
        List<Rating> ratings = Arrays.stream(rattingOfUser).collect(Collectors.toList());

        ratings.stream().map(rating -> {
       //  ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/" + rating.getHotelId(), Hotel.class);
            Hotel forEntity = hotelService.getHotel(rating.getHotelId());
         rating.setHotel(forEntity);
         return forEntity;
        }).collect(Collectors.toList());
/*
        ratings.stream().map(rating -> restTemplate.getForEntity("http://localhost:8082/hotels/" + rating.getRatingId(), Hotel.class)).collect(Collectors.toList());
*/


        user.setRatings(ratings);
        return user;
    }
}

package com.user.service.userservice.controller;

import com.user.service.userservice.entities.User;
import com.user.service.userservice.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
     User user1 = userService.saveUser(user);
     return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    int retryCount = 1;

    @GetMapping("/{userId}")
   // @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallBack")
    @Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallBack")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId){
        log.info("Retry count:" + retryCount);
        retryCount++;
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
       // return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<User> ratingHotelFallBack(String userId, Exception ex){
        log.info("Fallback is executed because service is down", ex.getMessage());
        User user = User.builder().email("dummy@gamil.com").name("Dummy").about("This user is created because some service is down").userId("1342").build();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/getAllUser")
    private ResponseEntity<List<User>> getAllUser(){
        List<User> allUser = userService.getAllUser();
        return ResponseEntity.ok(allUser);

    }
}

// This provides the user with access to backend. The user will be interacting with this layer
// Controller is solely responsible for receiving a request and routing it to the appropriate service for processing.

package com.infodat.library_borrow_springboot.api.controller;

import com.infodat.library_borrow_springboot.api.entity.UsersEntity;
import com.infodat.library_borrow_springboot.api.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
public class UsersController {

    // Variable
    private final UsersService usersService;

    // Constructor
    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    // Function
    @GetMapping
    public List<UsersEntity> getUsers() {
        return usersService.getUsers();
    }

    @PostMapping("/signup")
    public void registerNewUser(@RequestBody UsersEntity newUser) {
        usersService.addNewUser(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UsersEntity loginRequest) {
        return usersService.loginUser(loginRequest);
    }
}

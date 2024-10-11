// This file interacts directly with the DB
// A service has business logic etc. to handle the specifics of the requests.
// The service may need to fetch or update data in a database. The raw querying/updating of the data is the responsibility of the repository.

package com.infodat.library_borrow_springboot.api.service;

import com.infodat.library_borrow_springboot.api.entity.UsersEntity;
import com.infodat.library_borrow_springboot.api.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<UsersEntity> getUsers() {
        return usersRepository.findAll();
    }

    public void addNewUser(UsersEntity newUser) {
        String test = newUser.getUsername();
        Optional<UsersEntity> usersEntityOptional = usersRepository
                .findUsersEntitiesByUsername(newUser.getUsername());
        if (usersEntityOptional.isPresent()) {
            return;
        }
        else
            usersRepository.save(newUser);
    }

    public ResponseEntity<String> loginUser(UsersEntity newUser) {

        try {
            Optional<UsersEntity> usersEntityOptional = usersRepository
                    .findUsersEntitiesByUsernameAndPassword(newUser.getUsername(), newUser.getPassword());
            if (usersEntityOptional.isEmpty()) {
                // Login failed
                throw new IllegalStateException("Incorrect username or password");
            } else {
                return ResponseEntity.ok("Logged In successfully");
            }
        } catch (IllegalStateException e) {
            // Handle login failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
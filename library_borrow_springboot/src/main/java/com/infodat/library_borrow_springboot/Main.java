package com.infodat.library_borrow_springboot;

import com.infodat.library_borrow_springboot.api.entity.BooksEntity;
import com.infodat.library_borrow_springboot.api.entity.UsersEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackageClasses = { BooksEntity.class, UsersEntity.class })
@SpringBootApplication

public class Main {

    // Main function to start the commandline + server
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

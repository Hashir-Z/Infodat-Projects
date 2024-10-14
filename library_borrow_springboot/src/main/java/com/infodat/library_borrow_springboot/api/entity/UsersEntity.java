// This file initializes the table and the values

package com.infodat.library_borrow_springboot.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "UserEntity")
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UsersEntity {
    @Id
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "isadmin")
    private boolean isAdmin;
}

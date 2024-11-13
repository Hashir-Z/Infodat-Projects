// Repo = DB Functions

package com.infodat.library_borrow_springboot.api.repository;

import com.infodat.library_borrow_springboot.api.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends
        JpaRepository<UsersEntity, String> {
    Optional<UsersEntity> findUsersEntitiesByUsername(String username);
    Optional<UsersEntity> findUsersEntitiesByUsernameAndPassword(String username, String password);
}
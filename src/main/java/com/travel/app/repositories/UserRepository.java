package com.travel.app.repositories;

import com.travel.app.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // Devuelve solo los usuarios que no han sido "borrados"
    List<UserEntity> findByActive(Integer active);
}

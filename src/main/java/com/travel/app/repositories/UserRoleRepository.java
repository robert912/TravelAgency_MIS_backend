package com.travel.app.repositories;

import com.travel.app.entities.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity,Long> {
    List<UserRoleEntity> findByActive(Integer active);
}

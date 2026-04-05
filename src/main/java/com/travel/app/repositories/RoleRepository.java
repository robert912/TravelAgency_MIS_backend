package com.travel.app.repositories;

import com.travel.app.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
    List<RoleEntity> findByActive(Integer active);
}

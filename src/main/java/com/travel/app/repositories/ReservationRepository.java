package com.travel.app.repositories;

import com.travel.app.entities.ReservationEntity;
import com.travel.app.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity,Long> {
    List<ReservationEntity> findByActive(Integer active);
}

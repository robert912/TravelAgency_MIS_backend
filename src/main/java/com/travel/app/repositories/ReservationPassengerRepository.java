package com.travel.app.repositories;

import com.travel.app.entities.ReservationPassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationPassengerRepository extends JpaRepository<ReservationPassengerEntity,Long> {
    List<ReservationPassengerEntity> findByActive(Integer active);
}

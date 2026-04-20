package com.travel.app.repositories;

import com.travel.app.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findByActive(Integer active);

    List<ReservationEntity> findByPersonIdAndActive(Long personId, Integer active);

    List<ReservationEntity> findByTourPackageIdAndActive(Long tourPackageId, Integer active);

    List<ReservationEntity> findByStatusAndActive(String status, Integer active);
}
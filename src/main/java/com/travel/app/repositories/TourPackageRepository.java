package com.travel.app.repositories;

import com.travel.app.entities.TourPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourPackageRepository extends JpaRepository<TourPackageEntity,Long> {
    List<TourPackageEntity> findByActive(Integer active);
}

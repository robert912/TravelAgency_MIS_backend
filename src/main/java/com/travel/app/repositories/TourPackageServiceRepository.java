package com.travel.app.repositories;

import com.travel.app.entities.TourPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourPackageServiceRepository extends JpaRepository<TourPackageEntity,Integer> {
}

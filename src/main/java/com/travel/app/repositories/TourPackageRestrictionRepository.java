package com.travel.app.repositories;

import com.travel.app.entities.TourPackageRestrictionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourPackageRestrictionRepository extends JpaRepository<TourPackageRestrictionEntity,Integer> {
}

package com.travel.app.repositories;

import com.travel.app.entities.TravelTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelTypeRepository extends JpaRepository<TravelTypeEntity,Integer> {
}

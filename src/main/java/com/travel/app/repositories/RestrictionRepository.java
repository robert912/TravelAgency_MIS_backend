package com.travel.app.repositories;

import com.travel.app.entities.RestrictionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestrictionRepository extends JpaRepository<RestrictionEntity,Integer> {
}

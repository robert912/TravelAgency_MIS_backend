package com.travel.app.repositories;

import com.travel.app.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity,Long> {
    // Devuelve solo las personas que no han sido "borradas"
    List<PersonEntity> findByActive(Integer active);

    // devuelve persona por identificacion (rut u otro)
    PersonEntity findByIdentificationAndActive(String identification, Integer active);
}

package com.travel.app.services;

import com.travel.app.entities.PersonEntity;
import com.travel.app.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    // Obtener todas las personas
    public List<PersonEntity> getPersons() {
        return (List<PersonEntity>) personRepository.findAll();
    }

    // Guardar una persona
    public PersonEntity savePerson(PersonEntity person) {
        return personRepository.save(person);
    }

    // Obtener persona por ID
    public PersonEntity getPersonById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    // Actualizar persona
    public PersonEntity updatePerson(PersonEntity person) {
        return personRepository.save(person);
    }

    // Eliminado logico de persona
    public boolean deletePerson(Long id) throws Exception {
        try {
            // 1. Buscamos a la persona
            PersonEntity person = personRepository.findById(id).orElse(null);

            if (person != null) {
                // 2. Cambiamos el estado a 0 (desactivado)
                person.setActive(0);
                // 3. Guardamos los cambios (esto hace un UPDATE en la BD)
                personRepository.save(person);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("Error al desactivar la persona: " + e.getMessage());
        }
    }
}
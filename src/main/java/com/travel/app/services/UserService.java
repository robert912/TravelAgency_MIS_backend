package com.travel.app.services;

import com.travel.app.entities.UserEntity;
import com.travel.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    // Obtener todos los usuarios
    public ArrayList<UserEntity> getUsers(){
        return (ArrayList<UserEntity>) userRepository.findAll();
    }

    // Obtener todos los usuarios activo (1)
    public List<UserEntity> getUsersActive() {
        return userRepository.findByActive(1);
    }

    // Guardar un nuevo usuario
    public UserEntity saveUser(UserEntity user){
        return userRepository.save(user);
    }

    // Obtener usuario por ID
    public UserEntity getUserById(Long id){
        // Usamos .orElse(null) para evitar errores si el ID no existe
        return userRepository.findById(id).orElse(null);
    }

    // Actualizar usuario
    public UserEntity updateUser(UserEntity user) {
        return userRepository.save(user);
    }

    // Eliminar usuario
    public boolean deleteUser(Long id) throws Exception {
        try {
            UserEntity user = userRepository.findById(id).orElse(null);
            if (user != null) {
                user.setActive(0); // Borrado lógico
                userRepository.save(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("Error al desactivar el usuario: " + e.getMessage());
        }
    }
}

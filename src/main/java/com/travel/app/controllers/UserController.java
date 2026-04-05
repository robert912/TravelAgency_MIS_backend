package com.travel.app.controllers;

import com.travel.app.entities.UserEntity;
import com.travel.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    UserService userService;

    // Obtener todos los usuarios
    @GetMapping("/")
    public ResponseEntity<List<UserEntity>> listUsers() {
        List<UserEntity> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    // Obtener un usuario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Crear un nuevo usuario
    @PostMapping("/")
    public ResponseEntity<UserEntity> saveUser(@RequestBody UserEntity user) {
        UserEntity userNew = userService.saveUser(user);
        return ResponseEntity.ok(userNew);
    }

    // Actualizar un usuario existente
    @PutMapping("/")
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity user) {
        UserEntity userUpdated = userService.updateUser(user);
        return ResponseEntity.ok(userUpdated);
    }

    // Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) throws Exception {
        boolean isDeactivated = userService.deleteUser(id);

        if (isDeactivated) {
            return ResponseEntity.ok("Usuario desactivado correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

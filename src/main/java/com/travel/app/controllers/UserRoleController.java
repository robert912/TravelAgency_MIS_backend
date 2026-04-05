package com.travel.app.controllers;

import com.travel.app.entities.UserRoleEntity;
import com.travel.app.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-roles")
@CrossOrigin("*")
public class UserRoleController {

    @Autowired
    UserRoleService userRoleService;

    @GetMapping("/")
    public ResponseEntity<List<UserRoleEntity>> listAll() {
        List<UserRoleEntity> list = userRoleService.getUserRoles();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRoleEntity> getById(@PathVariable Long id) {
        UserRoleEntity userRole = userRoleService.getUserRoleById(id);
        return userRole != null ? ResponseEntity.ok(userRole) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<UserRoleEntity> save(@RequestBody UserRoleEntity userRole) {
        UserRoleEntity newUserRole = userRoleService.saveUserRole(userRole);
        return ResponseEntity.ok(newUserRole);
    }

    @PutMapping("/")
    public ResponseEntity<UserRoleEntity> update(@RequestBody UserRoleEntity userRole) {
        UserRoleEntity updatedUserRole = userRoleService.updateUserRole(userRole);
        return ResponseEntity.ok(updatedUserRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        boolean isDeactivated = userRoleService.deleteUserRole(id);
        if (isDeactivated) {
            return ResponseEntity.ok("Relación Usuario-Rol con ID " + id + " desactivada correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
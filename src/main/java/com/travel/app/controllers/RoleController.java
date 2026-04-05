package com.travel.app.controllers;

import com.travel.app.entities.RoleEntity;
import com.travel.app.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@CrossOrigin("*")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping("/")
    public ResponseEntity<List<RoleEntity>> listRoles() {
        List<RoleEntity> roles = roleService.getRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleEntity> getRoleById(@PathVariable Long id) {
        RoleEntity role = roleService.getRoleById(id);
        return role != null ? ResponseEntity.ok(role) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<RoleEntity> saveRole(@RequestBody RoleEntity role) {
        RoleEntity newRole = roleService.saveRole(role);
        return ResponseEntity.ok(newRole);
    }

    @PutMapping("/")
    public ResponseEntity<RoleEntity> updateRole(@RequestBody RoleEntity role) {
        RoleEntity updatedRole = roleService.updateRole(role);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoleById(@PathVariable Long id) throws Exception {
        boolean isDeactivated = roleService.deleteRole(id);
        if (isDeactivated) {
            return ResponseEntity.ok("Rol con ID " + id + " desactivado correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
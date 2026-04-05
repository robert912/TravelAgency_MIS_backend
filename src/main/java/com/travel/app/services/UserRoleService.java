package com.travel.app.services;

import com.travel.app.entities.UserRoleEntity;
import com.travel.app.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {

    @Autowired
    UserRoleRepository userRoleRepository;

    // Listar solo las asignaciones de roles activas
    public List<UserRoleEntity> getUserRoles() {
        return userRoleRepository.findByActive(1);
    }

    // Asignar un rol a un usuario
    public UserRoleEntity saveUserRole(UserRoleEntity userRole) {
        return userRoleRepository.save(userRole);
    }

    // Buscar por ID (solo si está activo)
    public UserRoleEntity getUserRoleById(Long id) {
        UserRoleEntity userRole = userRoleRepository.findById(id).orElse(null);
        if (userRole != null && userRole.getActive() == 1) {
            return userRole;
        }
        return null;
    }

    // Actualizar asignación
    public UserRoleEntity updateUserRole(UserRoleEntity userRole) {
        return userRoleRepository.save(userRole);
    }

    // Borrado lógico (Desactivar)
    public boolean deleteUserRole(Long id) throws Exception {
        try {
            UserRoleEntity userRole = userRoleRepository.findById(id).orElse(null);
            if (userRole != null) {
                userRole.setActive(0);
                userRoleRepository.save(userRole);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("Error al desactivar la relación usuario-rol: " + e.getMessage());
        }
    }
}
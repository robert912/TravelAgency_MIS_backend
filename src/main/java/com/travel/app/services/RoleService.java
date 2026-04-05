package com.travel.app.services;

import com.travel.app.entities.RoleEntity;
import com.travel.app.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    // Listar solo roles activos
    public List<RoleEntity> getRoles() {
        return roleRepository.findByActive(1);
    }

    // Guardar nuevo rol
    public RoleEntity saveRole(RoleEntity role) {
        return roleRepository.save(role);
    }

    // Buscar por ID (solo si está activo)
    public RoleEntity getRoleById(Long id) {
        RoleEntity role = roleRepository.findById(id).orElse(null);
        if (role != null && role.getActive() == 1) {
            return role;
        }
        return null;
    }

    // Actualizar rol
    public RoleEntity updateRole(RoleEntity role) {
        return roleRepository.save(role);
    }

    // Borrado lógico (Desactivar)
    public boolean deleteRole(Long id) throws Exception {
        try {
            RoleEntity role = roleRepository.findById(id).orElse(null);
            if (role != null) {
                role.setActive(0);
                roleRepository.save(role);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("Error al desactivar el rol: " + e.getMessage());
        }
    }
}
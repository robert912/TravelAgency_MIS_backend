package com.travel.app.services;

import com.travel.app.entities.TourPackageRestrictionEntity;
import com.travel.app.repositories.TourPackageRestrictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourPackageRestrictionService {

    @Autowired
    TourPackageRestrictionRepository tourPackageRestrictionRepository;

    // Listar solo restricciones de paquetes activas
    public List<TourPackageRestrictionEntity> getTourPackageRestrictions() {
        return tourPackageRestrictionRepository.findByActive(1);
    }

    // Guardar una nueva restricción de paquete
    public TourPackageRestrictionEntity saveTourPackageRestriction(TourPackageRestrictionEntity tpr) {
        return tourPackageRestrictionRepository.save(tpr);
    }

    // Buscar por ID (solo si está activo)
    public TourPackageRestrictionEntity getTourPackageRestrictionById(Long id) {
        TourPackageRestrictionEntity tpr = tourPackageRestrictionRepository.findById(id).orElse(null);
        if (tpr != null && tpr.getActive() == 1) {
            return tpr;
        }
        return null;
    }

    // Actualizar restricción de paquete
    public TourPackageRestrictionEntity updateTourPackageRestriction(TourPackageRestrictionEntity tpr) {
        return tourPackageRestrictionRepository.save(tpr);
    }

    // Borrado lógico (Desactivar)
    public boolean deleteTourPackageRestriction(Long id) throws Exception {
        try {
            TourPackageRestrictionEntity tpr = tourPackageRestrictionRepository.findById(id).orElse(null);
            if (tpr != null) {
                tpr.setActive(0);
                tourPackageRestrictionRepository.save(tpr);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("Error al desactivar la restricción del paquete: " + e.getMessage());
        }
    }
}
package com.travel.app.services;

import com.travel.app.entities.TourPackageConditionEntity;
import com.travel.app.repositories.TourPackageConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourPackageConditionService {

    @Autowired
    TourPackageConditionRepository tourPackageConditionRepository;

    // Listar solo condiciones de paquetes activas
    public List<TourPackageConditionEntity> getTourPackageConditions() {
        return tourPackageConditionRepository.findByActive(1);
    }

    // Guardar una nueva condición de paquete
    public TourPackageConditionEntity saveTourPackageCondition(TourPackageConditionEntity tpc) {
        return tourPackageConditionRepository.save(tpc);
    }

    // Buscar por ID (solo si está activo)
    public TourPackageConditionEntity getTourPackageConditionById(Long id) {
        TourPackageConditionEntity tpc = tourPackageConditionRepository.findById(id).orElse(null);
        if (tpc != null && tpc.getActive() == 1) {
            return tpc;
        }
        return null;
    }

    // Actualizar condición de paquete
    public TourPackageConditionEntity updateTourPackageCondition(TourPackageConditionEntity tpc) {
        return tourPackageConditionRepository.save(tpc);
    }

    // Borrado lógico (Desactivar)
    public boolean deleteTourPackageCondition(Long id) throws Exception {
        try {
            TourPackageConditionEntity tpc = tourPackageConditionRepository.findById(id).orElse(null);
            if (tpc != null) {
                tpc.setActive(0);
                tourPackageConditionRepository.save(tpc);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("Error al desactivar la condición del paquete: " + e.getMessage());
        }
    }
}
package com.travel.app.services;

import com.travel.app.entities.TourPackageServiceEntity;
import com.travel.app.repositories.TourPackageServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourPackageServiceService {

    @Autowired
    TourPackageServiceRepository tourPackageServiceRepository;

    // Listar solo servicios de paquetes activos
    public List<TourPackageServiceEntity> getTourPackageServices() {
        return tourPackageServiceRepository.findByActive(1);
    }

    // Guardar una nueva relación de servicio en un paquete
    public TourPackageServiceEntity saveTourPackageService(TourPackageServiceEntity tps) {
        return tourPackageServiceRepository.save(tps);
    }

    // Buscar por ID (solo si está activo)
    public TourPackageServiceEntity getTourPackageServiceById(Long id) {
        TourPackageServiceEntity tps = tourPackageServiceRepository.findById(id).orElse(null);
        if (tps != null && tps.getActive() == 1) {
            return tps;
        }
        return null;
    }

    // Actualizar relación de servicio de paquete
    public TourPackageServiceEntity updateTourPackageService(TourPackageServiceEntity tps) {
        return tourPackageServiceRepository.save(tps);
    }

    // Borrado lógico (Desactivar)
    public boolean deleteTourPackageService(Long id) throws Exception {
        try {
            TourPackageServiceEntity tps = tourPackageServiceRepository.findById(id).orElse(null);
            if (tps != null) {
                tps.setActive(0);
                tourPackageServiceRepository.save(tps);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("Error al desactivar el servicio del paquete: " + e.getMessage());
        }
    }
}
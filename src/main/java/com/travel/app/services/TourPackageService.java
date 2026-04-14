package com.travel.app.services;

import com.travel.app.entities.TourPackageEntity;
import com.travel.app.repositories.TourPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TourPackageService {

    @Autowired
    TourPackageRepository tourPackageRepository;

    // Listar solo paquetes turísticos activos
    public List<TourPackageEntity> getTourPackages() {
        return tourPackageRepository.findByActive(1);
    }

    // Guardar nuevo paquete turístico
    public TourPackageEntity saveTourPackage(TourPackageEntity tourPackage) {
        return tourPackageRepository.save(tourPackage);
    }

    // Buscar por ID (solo si está activo)
    public TourPackageEntity getTourPackageById(Long id) {
        TourPackageEntity tourPackage = tourPackageRepository.findById(id).orElse(null);
        if (tourPackage != null && tourPackage.getActive() == 1) {
            return tourPackage;
        }
        return null;
    }

    // Actualizar paquete turístico
    public TourPackageEntity updateTourPackage(TourPackageEntity tourPackage) {
        return tourPackageRepository.save(tourPackage);
    }

    // Borrado lógico (Desactivar)
    public boolean deleteTourPackage(Long id) throws Exception {
        try {
            TourPackageEntity tourPackage = tourPackageRepository.findById(id).orElse(null);
            if (tourPackage != null) {
                tourPackage.setActive(0);
                tourPackageRepository.save(tourPackage);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("Error al desactivar el paquete turístico: " + e.getMessage());
        }
    }

    // Buscar Paquetes filtrados por destino, fechas de viaje, rango de precios, duración o tipo de experiencia.
    public List<TourPackageEntity> filterTourPackages(String destination, BigDecimal minPrice, BigDecimal maxPrice, LocalDate startDate, LocalDate endDate, Long travelTypeId) {
        return tourPackageRepository.findByFilters(destination, minPrice, maxPrice, startDate, endDate, travelTypeId);
    }
}
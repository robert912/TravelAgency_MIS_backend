package com.travel.app.controllers;

import com.travel.app.entities.TourPackageEntity;
import com.travel.app.services.ReservationService;
import com.travel.app.services.TourPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tour-packages")
@CrossOrigin("*")
public class TourPackageController {

    @Autowired
    private TourPackageService tourPackageService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/")
    public ResponseEntity<List<TourPackageEntity>> listTourPackages() {
        List<TourPackageEntity> tourPackages = tourPackageService.getTourPackages();
        return ResponseEntity.ok(tourPackages);
    }

    @GetMapping("/active")
    public ResponseEntity<List<TourPackageEntity>> listTourPackagesActive() {
        List<TourPackageEntity> tourPackages = tourPackageService.getTourPackagesActive();
        return ResponseEntity.ok(tourPackages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourPackageEntity> getTourPackageById(@PathVariable Long id) {
        TourPackageEntity tourPackage = tourPackageService.getTourPackageById(id);
        return tourPackage != null ? ResponseEntity.ok(tourPackage) : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<TourPackageEntity>> searchPackages(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Long travelTypeId
    ) {
        List<TourPackageEntity> filteredPackages = tourPackageService.filterTourPackages(
                destination, minPrice, maxPrice, startDate, endDate, travelTypeId);

        return ResponseEntity.ok(filteredPackages);
    }

    @PostMapping("/")
    public ResponseEntity<TourPackageEntity> saveTourPackage(@RequestBody TourPackageEntity tourPackage) {
        TourPackageEntity newTourPackage = tourPackageService.saveTourPackage(tourPackage);
        return ResponseEntity.ok(newTourPackage);
    }

    @PutMapping("/")
    public ResponseEntity<TourPackageEntity> updateTourPackage(@RequestBody TourPackageEntity tourPackage) {
        TourPackageEntity updatedTourPackage = tourPackageService.updateTourPackage(tourPackage);
        return ResponseEntity.ok(updatedTourPackage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTourPackageById(@PathVariable Long id) throws Exception {
        boolean isDeactivated = tourPackageService.deleteTourPackage(id);
        if (isDeactivated) {
            return ResponseEntity.ok("Paquete turístico con ID " + id + " desactivado correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para verificar si se puede reservar una cantidad específica
    @GetMapping("/{packageId}/availability/check")
    public ResponseEntity<Map<String, Object>> checkAvailabilityForQuantity(
            @PathVariable Long packageId,
            @RequestParam int quantity) {

        TourPackageEntity tourPackage = tourPackageService.getTourPackageById(packageId);
        if (tourPackage == null) {
            return ResponseEntity.notFound().build();
        }

        int totalSlots = tourPackage.getTotalSlots() != null ? tourPackage.getTotalSlots() : 0;
        int reservedSlots = reservationService.countConfirmedPassengersByPackageId(packageId);
        int availableSlots = totalSlots - reservedSlots;
// 🔥 LOGS PARA DEPURAR
        System.out.println("=== DEBUG DISPONIBILIDAD ===");
        System.out.println("Package ID: " + packageId);
        System.out.println("Total Slots: " + totalSlots);
        System.out.println("Reserved Slots: " + reservedSlots);
        System.out.println("Available Slots: " + availableSlots);
        System.out.println("Requested Quantity: " + quantity);
        System.out.println("===========================");

        Map<String, Object> response = new HashMap<>();
        response.put("totalSlots", totalSlots);
        response.put("reservedSlots", reservedSlots);
        response.put("availableSlots", availableSlots);
        response.put("requestedQuantity", quantity);
        response.put("isAvailable", availableSlots >= quantity);
        response.put("message", availableSlots >= quantity ?
                "Hay suficientes cupos disponibles" :
                String.format("Solo hay %d cupos disponibles de %d solicitados", availableSlots, quantity));

        return ResponseEntity.ok(response);
    }
}
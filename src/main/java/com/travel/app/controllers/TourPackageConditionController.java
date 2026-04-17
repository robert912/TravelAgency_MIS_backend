package com.travel.app.controllers;

import com.travel.app.entities.TourPackageConditionEntity;
import com.travel.app.services.TourPackageConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tour-package-conditions")
@CrossOrigin("*")
public class TourPackageConditionController {

    @Autowired
    private TourPackageConditionService tourPackageConditionService;

    // Obtener condiciones activas de un paquete específico
    @GetMapping("/package/{packageId}/active")
    public ResponseEntity<List<TourPackageConditionEntity>> getActiveByPackageId(@PathVariable Long packageId) {
        return ResponseEntity.ok(tourPackageConditionService.getActiveConditionsByPackageId(packageId));
    }

    // Obtener todas las condiciones de un paquete
    @GetMapping("/package/{packageId}/all")
    public ResponseEntity<List<TourPackageConditionEntity>> getAllByPackageId(@PathVariable Long packageId) {
        return ResponseEntity.ok(tourPackageConditionService.getAllConditionsByPackageId(packageId));
    }

    // Endpoint para sincronizar condiciones
    @PutMapping("/package/{packageId}/sync")
    public ResponseEntity<Void> syncConditions(
            @PathVariable Long packageId,
            @RequestBody Map<String, List<Long>> request,
            @RequestParam(defaultValue = "1") Long userId) {

        List<Long> conditionIds = request.get("conditionIds");
        if (conditionIds == null) {
            return ResponseEntity.badRequest().build();
        }

        tourPackageConditionService.syncPackageConditions(packageId, conditionIds, userId);
        return ResponseEntity.ok().build();
    }
}
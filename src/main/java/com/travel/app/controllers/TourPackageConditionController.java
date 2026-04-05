package com.travel.app.controllers;

import com.travel.app.entities.TourPackageConditionEntity;
import com.travel.app.services.TourPackageConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tour-package-conditions")
@CrossOrigin("*")
public class TourPackageConditionController {

    @Autowired
    TourPackageConditionService tourPackageConditionService;

    @GetMapping("/")
    public ResponseEntity<List<TourPackageConditionEntity>> listAll() {
        List<TourPackageConditionEntity> list = tourPackageConditionService.getTourPackageConditions();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourPackageConditionEntity> getById(@PathVariable Long id) {
        TourPackageConditionEntity tpc = tourPackageConditionService.getTourPackageConditionById(id);
        return tpc != null ? ResponseEntity.ok(tpc) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<TourPackageConditionEntity> save(@RequestBody TourPackageConditionEntity tpc) {
        TourPackageConditionEntity newTpc = tourPackageConditionService.saveTourPackageCondition(tpc);
        return ResponseEntity.ok(newTpc);
    }

    @PutMapping("/")
    public ResponseEntity<TourPackageConditionEntity> update(@RequestBody TourPackageConditionEntity tpc) {
        TourPackageConditionEntity updatedTpc = tourPackageConditionService.updateTourPackageCondition(tpc);
        return ResponseEntity.ok(updatedTpc);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        boolean isDeactivated = tourPackageConditionService.deleteTourPackageCondition(id);
        if (isDeactivated) {
            return ResponseEntity.ok("Condición de paquete con ID " + id + " desactivada correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
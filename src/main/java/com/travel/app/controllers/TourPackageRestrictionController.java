package com.travel.app.controllers;

import com.travel.app.entities.TourPackageRestrictionEntity;
import com.travel.app.services.TourPackageRestrictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tour-package-restrictions")
@CrossOrigin("*")
public class TourPackageRestrictionController {

    @Autowired
    TourPackageRestrictionService tourPackageRestrictionService;

    @GetMapping("/")
    public ResponseEntity<List<TourPackageRestrictionEntity>> listAll() {
        List<TourPackageRestrictionEntity> list = tourPackageRestrictionService.getTourPackageRestrictions();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourPackageRestrictionEntity> getById(@PathVariable Long id) {
        TourPackageRestrictionEntity tpr = tourPackageRestrictionService.getTourPackageRestrictionById(id);
        return tpr != null ? ResponseEntity.ok(tpr) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<TourPackageRestrictionEntity> save(@RequestBody TourPackageRestrictionEntity tpr) {
        TourPackageRestrictionEntity newTpr = tourPackageRestrictionService.saveTourPackageRestriction(tpr);
        return ResponseEntity.ok(newTpr);
    }

    @PutMapping("/")
    public ResponseEntity<TourPackageRestrictionEntity> update(@RequestBody TourPackageRestrictionEntity tpr) {
        TourPackageRestrictionEntity updatedTpr = tourPackageRestrictionService.updateTourPackageRestriction(tpr);
        return ResponseEntity.ok(updatedTpr);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        boolean isDeactivated = tourPackageRestrictionService.deleteTourPackageRestriction(id);
        if (isDeactivated) {
            return ResponseEntity.ok("Restricción de paquete con ID " + id + " desactivada correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
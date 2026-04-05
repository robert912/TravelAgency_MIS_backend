package com.travel.app.controllers;

import com.travel.app.entities.TourPackageServiceEntity;
import com.travel.app.services.TourPackageServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tour-package-services")
@CrossOrigin("*")
public class TourPackageServiceController {

    @Autowired
    TourPackageServiceService tourPackageServiceService;

    @GetMapping("/")
    public ResponseEntity<List<TourPackageServiceEntity>> listAll() {
        List<TourPackageServiceEntity> list = tourPackageServiceService.getTourPackageServices();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourPackageServiceEntity> getById(@PathVariable Long id) {
        TourPackageServiceEntity tps = tourPackageServiceService.getTourPackageServiceById(id);
        return tps != null ? ResponseEntity.ok(tps) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<TourPackageServiceEntity> save(@RequestBody TourPackageServiceEntity tps) {
        TourPackageServiceEntity newTps = tourPackageServiceService.saveTourPackageService(tps);
        return ResponseEntity.ok(newTps);
    }

    @PutMapping("/")
    public ResponseEntity<TourPackageServiceEntity> update(@RequestBody TourPackageServiceEntity tps) {
        TourPackageServiceEntity updatedTps = tourPackageServiceService.updateTourPackageService(tps);
        return ResponseEntity.ok(updatedTps);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        boolean isDeactivated = tourPackageServiceService.deleteTourPackageService(id);
        if (isDeactivated) {
            return ResponseEntity.ok("Servicio de paquete con ID " + id + " desactivado correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
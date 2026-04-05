package com.travel.app.controllers;

import com.travel.app.entities.ReservationEntity;
import com.travel.app.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@CrossOrigin("*")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @GetMapping("/")
    public ResponseEntity<List<ReservationEntity>> listReservations() {
        List<ReservationEntity> reservations = reservationService.getReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationEntity> getReservationById(@PathVariable Long id) {
        ReservationEntity reservation = reservationService.getReservationById(id);
        return reservation != null ? ResponseEntity.ok(reservation) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<ReservationEntity> saveReservation(@RequestBody ReservationEntity reservation) {
        ReservationEntity newReservation = reservationService.saveReservation(reservation);
        return ResponseEntity.ok(newReservation);
    }

    @PutMapping("/")
    public ResponseEntity<ReservationEntity> updateReservation(@RequestBody ReservationEntity reservation) {
        ReservationEntity updatedReservation = reservationService.updateReservation(reservation);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservationById(@PathVariable Long id) throws Exception {
        boolean isDeactivated = reservationService.deleteReservation(id);
        if (isDeactivated) {
            return ResponseEntity.ok("Reserva con ID " + id + " desactivada correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
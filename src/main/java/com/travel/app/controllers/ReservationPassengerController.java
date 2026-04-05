package com.travel.app.controllers;

import com.travel.app.entities.ReservationPassengerEntity;
import com.travel.app.services.ReservationPassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation-passengers")
@CrossOrigin("*")
public class ReservationPassengerController {

    @Autowired
    ReservationPassengerService reservationPassengerService;

    @GetMapping("/")
    public ResponseEntity<List<ReservationPassengerEntity>> listReservationPassengers() {
        List<ReservationPassengerEntity> list = reservationPassengerService.getReservationPassengers();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationPassengerEntity> getById(@PathVariable Long id) {
        ReservationPassengerEntity rp = reservationPassengerService.getReservationPassengerById(id);
        return rp != null ? ResponseEntity.ok(rp) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<ReservationPassengerEntity> save(@RequestBody ReservationPassengerEntity rp) {
        ReservationPassengerEntity newRp = reservationPassengerService.saveReservationPassenger(rp);
        return ResponseEntity.ok(newRp);
    }

    @PutMapping("/")
    public ResponseEntity<ReservationPassengerEntity> update(@RequestBody ReservationPassengerEntity rp) {
        ReservationPassengerEntity updatedRp = reservationPassengerService.updateReservationPassenger(rp);
        return ResponseEntity.ok(updatedRp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        boolean isDeactivated = reservationPassengerService.deleteReservationPassenger(id);
        if (isDeactivated) {
            return ResponseEntity.ok("Pasajero de reserva con ID " + id + " desactivado correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
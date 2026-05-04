package com.travel.app.controllers;

import com.travel.app.dtos.ReservationRequestDTO;
import com.travel.app.entities.ReservationEntity;
import com.travel.app.enums.ReservationStatus;
import com.travel.app.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin("*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/")
    public ResponseEntity<List<ReservationEntity>> listReservations() {
        List<ReservationEntity> reservations = reservationService.getReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ReservationEntity>> listActiveReservations() {
        List<ReservationEntity> reservations = reservationService.getActiveReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationEntity> getReservationById(@PathVariable Long id) {
        ReservationEntity reservation = reservationService.getReservationById(id);
        return reservation != null ? ResponseEntity.ok(reservation) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/passengers")
    public ResponseEntity<?> getPassengersByReservationId(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getPassengersByReservationId(id));
    }

    // Nuevo endpoint para crear reserva con DTO
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createReservation(
            @RequestBody ReservationRequestDTO request,
            @RequestParam(defaultValue = "1") Long userId) {

        try {
            ReservationEntity reservation = reservationService.createReservation(request, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Reserva creada exitosamente");
            response.put("data", reservation);
            response.put("reservationId", reservation.getId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Endpoint original para compatibilidad
    @PostMapping("/")
    public ResponseEntity<ReservationEntity> saveReservation(@RequestBody ReservationEntity reservation) {
        if (reservation.getReservationDate() == null) {
            reservation.setReservationDate(java.time.LocalDateTime.now());
        }
        if (reservation.getExpirationDate() == null) {
            reservation.setExpirationDate(java.time.LocalDateTime.now().plusDays(3));
        }
        if (reservation.getActive() == null) {
            reservation.setActive(1);
        }
        if (reservation.getStatus() == null) {
            reservation.setStatus(ReservationStatus.PENDIENTE);
        }

        ReservationEntity newReservation = reservationService.saveReservation(reservation);
        return ResponseEntity.ok(newReservation);
    }

    @PutMapping("/")
    public ResponseEntity<ReservationEntity> updateReservation(@RequestBody ReservationEntity reservation) {
        if (reservation.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
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

    @PutMapping("/{id}/status")
    public ResponseEntity<ReservationEntity> changeStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(defaultValue = "1") Long userId) {
        ReservationEntity updated = reservationService.changeStatus(id, status, userId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<List<ReservationEntity>> getByPersonId(@PathVariable Long personId) {
        List<ReservationEntity> reservations = reservationService.getByPersonId(personId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/package/{packageId}")
    public ResponseEntity<List<ReservationEntity>> getByPackageId(@PathVariable Long packageId) {
        List<ReservationEntity> reservations = reservationService.getByPackageId(packageId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReservationEntity>> getByStatus(@PathVariable String status) {
        List<ReservationEntity> reservations = reservationService.getByStatus(status);
        return ResponseEntity.ok(reservations);
    }
}
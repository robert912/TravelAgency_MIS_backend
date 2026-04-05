package com.travel.app.services;

import com.travel.app.entities.ReservationEntity;
import com.travel.app.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    // Listar solo reservas activas
    public List<ReservationEntity> getReservations() {
        return reservationRepository.findByActive(1);
    }

    // Guardar reserva
    public ReservationEntity saveReservation(ReservationEntity reservation) {
        return reservationRepository.save(reservation);
    }

    // Buscar por ID (solo si está activa)
    public ReservationEntity getReservationById(Long id) {
        ReservationEntity reservation = reservationRepository.findById(id).orElse(null);
        if (reservation != null && reservation.getActive() == 1) {
            return reservation;
        }
        return null;
    }

    // Actualizar reserva
    public ReservationEntity updateReservation(ReservationEntity reservation) {
        return reservationRepository.save(reservation);
    }

    // Borrado lógico (Desactivar)
    public boolean deleteReservation(Long id) throws Exception {
        try {
            ReservationEntity reservation = reservationRepository.findById(id).orElse(null);
            if (reservation != null) {
                reservation.setActive(0);
                reservationRepository.save(reservation);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("Error al desactivar la reserva: " + e.getMessage());
        }
    }
}
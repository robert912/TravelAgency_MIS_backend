package com.travel.app.services;

import com.travel.app.dtos.ReservationRequestDTO;
import com.travel.app.entities.*;
import com.travel.app.enums.ReservationStatus;
import com.travel.app.repositories.ReservationPassengerRepository;
import com.travel.app.repositories.ReservationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationPassengerRepository reservationPassengerRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private TourPackageService tourPackageService;

    // Obtener todas las reservas
    public List<ReservationEntity> getReservations() {
        return reservationRepository.findAll();
    }

    // Obtener reservas activas
    public List<ReservationEntity> getActiveReservations() {
        return reservationRepository.findByActive(1);
    }

    // Obtener reserva por ID
    public ReservationEntity getReservationById(Long id) {
        ReservationEntity reservation = reservationRepository.findById(id).orElse(null);
        if (reservation != null && reservation.getActive() == 1) {
            return reservation;
        }
        return null;
    }

    // Cambiar estado de la reserva
    @Transactional
    public ReservationEntity changeStatus(Long id, String status, Long userId) {
        ReservationEntity reservation = getReservationById(id);
        if (reservation == null) {
            throw new RuntimeException("Reserva no encontrada con ID: " + id);
        }

        try {
            ReservationStatus newStatus = ReservationStatus.valueOf(status);
            reservation.setStatus(newStatus);
            reservation.setModifiedByUserId(userId);
            reservation.setUpdatedAt(LocalDateTime.now());
            return reservationRepository.save(reservation);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido: " + status);
        }
    }

    // Guardar reserva con pasajeros (Método principal)
    @Transactional
    public ReservationEntity createReservation(ReservationRequestDTO request, Long userId) {
        // Validar que el paquete existe
        TourPackageEntity tourPackage = tourPackageService.getTourPackageById(request.getTourPackageId());
        if (tourPackage == null) {
            throw new RuntimeException("Paquete turístico no encontrado");
        }

        // Validar cupos disponibles
        if (tourPackage.getTotalSlots() < request.getPassengers()) {
            throw new RuntimeException("No hay suficientes cupos disponibles");
        }

        // Obtener o crear la persona principal
        PersonEntity mainPerson;
        if (request.getPersonId() != null) {
            mainPerson = personService.getPersonById(request.getPersonId());
            if (mainPerson == null) {
                throw new RuntimeException("Persona no encontrada con ID: " + request.getPersonId());
            }
        } else if (request.getIdentification() != null && !request.getIdentification().isEmpty()) {
            mainPerson = personService.findByIdentification(request.getIdentification());
            if (mainPerson == null) {
                mainPerson = new PersonEntity();
                mainPerson.setFullName(request.getFullName());
                mainPerson.setIdentification(request.getIdentification());
                mainPerson.setEmail(request.getEmail());
                mainPerson.setPhone(request.getPhone() != null ? request.getPhone() : "");
                mainPerson.setNationality(request.getNationality() != null ? request.getNationality() : "");
                mainPerson.setFailedAttempts(3);
                mainPerson.setActive(1);
                mainPerson.setCreatedByUserId(userId);
                mainPerson.setUpdatedByUserId(userId);
                mainPerson = personService.savePerson(mainPerson);
            }
        } else {
            throw new RuntimeException("Se requiere la identificación de la persona principal");
        }

        // Crear la reserva con los datos de precios y descuentos
        ReservationEntity reservation = new ReservationEntity();
        reservation.setPerson(mainPerson);
        reservation.setTourPackage(tourPackage);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setExpirationDate(LocalDateTime.now().plusDays(3));
        reservation.setStatus(ReservationStatus.PENDIENTE);
        reservation.setActive(1);
        reservation.setCreatedByUserId(userId);
        reservation.setModifiedByUserId(userId);

        // Guardar solicitudes especiales
        if (request.getSpecialRequests() != null) {
            reservation.setSolicitudes(request.getSpecialRequests());
        }

        // Guardar precios y descuentos
        reservation.setPassengersCount(request.getPassengers());
        reservation.setSubtotal(request.getSubtotal());
        reservation.setTotalAmount(request.getTotalAmount());
        reservation.setDiscountAmount(request.getDiscountAmount());

        // Guardar detalles de descuentos como JSON
        if (request.getDiscountsDetail() != null && !request.getDiscountsDetail().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String discountDetailsJson = mapper.writeValueAsString(request.getDiscountsDetail());
                reservation.setDiscountDetails(discountDetailsJson);
            } catch (Exception e) {
                System.err.println("Error guardando detalles de descuentos: " + e.getMessage());
            }
        }

        ReservationEntity savedReservation = reservationRepository.save(reservation);

        // Procesar pasajeros
        boolean mainPassengerIncluded = false;
        for (ReservationRequestDTO.PassengerDataDTO passengerData : request.getPassengersData()) {
            PersonEntity person;

            if (passengerData.getPersonId() != null) {
                person = personService.getPersonById(passengerData.getPersonId());
                if (person == null) {
                    throw new RuntimeException("Persona no encontrada con ID: " + passengerData.getPersonId());
                }
            } else {
                person = personService.findByIdentification(passengerData.getIdentification());
                if (person == null) {
                    person = new PersonEntity();
                    person.setFullName(passengerData.getFullName());
                    person.setIdentification(passengerData.getIdentification());
                    person.setEmail(passengerData.getEmail());
                    person.setPhone(passengerData.getPhone() != null ? passengerData.getPhone() : "");
                    person.setNationality(passengerData.getNationality() != null ? passengerData.getNationality() : "");
                    person.setFailedAttempts(3);
                    person.setActive(1);
                    person.setCreatedByUserId(userId);
                    person.setUpdatedByUserId(userId);
                    person = personService.savePerson(person);
                }
            }

            if (person.getId().equals(mainPerson.getId())) {
                mainPassengerIncluded = true;
            }

            ReservationPassengerEntity reservationPassenger = new ReservationPassengerEntity();
            reservationPassenger.setReservation(savedReservation);
            reservationPassenger.setPerson(person);
            reservationPassenger.setActive(1);
            reservationPassenger.setCreatedByUserId(userId);
            reservationPassenger.setModifiedByUserId(userId);
            reservationPassengerRepository.save(reservationPassenger);
        }

        if (!mainPassengerIncluded) {
            ReservationPassengerEntity mainReservationPassenger = new ReservationPassengerEntity();
            mainReservationPassenger.setReservation(savedReservation);
            mainReservationPassenger.setPerson(mainPerson);
            mainReservationPassenger.setActive(1);
            mainReservationPassenger.setCreatedByUserId(userId);
            mainReservationPassenger.setModifiedByUserId(userId);
            reservationPassengerRepository.save(mainReservationPassenger);
        }

        // Descontar cupos
        tourPackage.setTotalSlots(tourPackage.getTotalSlots() - request.getPassengers());
        tourPackageService.updateTourPackage(tourPackage);

        return savedReservation;
    }

    // Método para guardar reserva (compatible con el controller existente)
    @Transactional
    public ReservationEntity saveReservation(ReservationEntity reservation) {
        return reservationRepository.save(reservation);
    }

    // Método para guardar reserva con pasajeros desde el controller
    @Transactional
    public ReservationEntity saveReservationWithPassengers(ReservationEntity reservation, List<PersonEntity> passengers, Long userId) {
        ReservationEntity savedReservation = reservationRepository.save(reservation);

        for (PersonEntity passenger : passengers) {
            ReservationPassengerEntity reservationPassenger = new ReservationPassengerEntity();
            reservationPassenger.setReservation(savedReservation);
            reservationPassenger.setPerson(passenger);
            reservationPassenger.setActive(1);
            reservationPassenger.setCreatedByUserId(userId);
            reservationPassenger.setModifiedByUserId(userId);
            reservationPassengerRepository.save(reservationPassenger);
        }

        return savedReservation;
    }

    // Actualizar reserva
    @Transactional
    public ReservationEntity updateReservation(ReservationEntity reservation) {
        ReservationEntity existingReservation = reservationRepository.findById(reservation.getId()).orElse(null);
        if (existingReservation == null) {
            throw new RuntimeException("Reserva no encontrada");
        }

        if (reservation.getStatus() != null) {
            existingReservation.setStatus(reservation.getStatus());
        }
        if (reservation.getActive() != null) {
            existingReservation.setActive(reservation.getActive());
        }
        existingReservation.setModifiedByUserId(reservation.getModifiedByUserId());
        existingReservation.setUpdatedAt(LocalDateTime.now());

        return reservationRepository.save(existingReservation);
    }

    // Borrado lógico
    @Transactional
    public boolean deleteReservation(Long id) throws Exception {
        try {
            ReservationEntity reservation = reservationRepository.findById(id).orElse(null);
            if (reservation != null) {
                reservation.setActive(0);
                reservation.setUpdatedAt(LocalDateTime.now());
                reservationRepository.save(reservation);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("Error al desactivar la reserva: " + e.getMessage());
        }
    }

    // Obtener reservas por persona
    public List<ReservationEntity> getByPersonId(Long personId) {
        return reservationRepository.findByPersonIdAndActive(personId, 1);
    }

    // Obtener reservas por paquete
    public List<ReservationEntity> getByPackageId(Long packageId) {
        return reservationRepository.findByTourPackageIdAndActive(packageId, 1);
    }

    // Obtener reservas por estado
    public List<ReservationEntity> getByStatus(String status) {
        return reservationRepository.findByStatusAndActive(status, 1);
    }

    // Obtener pasajeros de una reserva
    public List<ReservationPassengerEntity> getPassengersByReservationId(Long reservationId) {
        return reservationPassengerRepository.findByReservationIdAndActive(reservationId, 1);
    }
}
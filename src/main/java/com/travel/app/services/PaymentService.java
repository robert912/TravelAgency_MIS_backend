package com.travel.app.services;

import com.travel.app.dtos.PaymentRequestDTO;
import com.travel.app.entities.PaymentEntity;
import com.travel.app.entities.ReservationEntity;
import com.travel.app.enums.ReservationStatus;
import com.travel.app.repositories.PaymentRepository;
import com.travel.app.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public List<PaymentEntity> getPayments() {
        return paymentRepository.findAll();
    }

    public PaymentEntity getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public PaymentEntity getPaymentByReservationId(Long reservationId) {
        return paymentRepository.findByReservationId(reservationId).orElse(null);
    }

    public boolean hasPayment(Long reservationId) {
        return paymentRepository.existsByReservationId(reservationId);
    }

    @Transactional
    public PaymentEntity processPayment(PaymentRequestDTO request) {
        // 1. Validar que la reserva existe
        ReservationEntity reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + request.getReservationId()));

        // 2. Validar que la reserva está activa
        if (reservation.getActive() != 1) {
            throw new RuntimeException("La reserva no está activa");
        }

        // 3. Validar que la reserva está pendiente
        if (reservation.getStatus() != ReservationStatus.PENDIENTE) {
            throw new RuntimeException("Solo se pueden pagar reservas en estado PENDIENTE. Estado actual: " + reservation.getStatus());
        }

        // 4. Validar que no tenga un pago previo
        if (hasPayment(request.getReservationId())) {
            throw new RuntimeException("Esta reserva ya tiene un pago registrado");
        }

        // 5. Validar datos de la tarjeta (solo formato, no validación real)
        validateCardData(request);

        // 6. Calcular monto total registrado en la reserva
        BigDecimal amount = reservation.getTotalAmount();
        if (amount == null) {
            int passengers = reservation.getPassengersCount() != null ? reservation.getPassengersCount() : 1;
            amount = reservation.getTourPackage().getPrice().multiply(BigDecimal.valueOf(passengers));
        }

        // 7. Crear el pago
        PaymentEntity payment = new PaymentEntity();
        payment.setReservation(reservation);
        payment.setAmount(amount);
        payment.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "TARJETA_CREDITO");
        payment.setCardNumber(maskCardNumber(request.getCardNumber()));
        payment.setCardExpiration(request.getCardExpiration());
        payment.setCardCvv("***"); // No almacenar CVV real
        payment.setTransactionId(generateTransactionId());
        payment.setCreatedByUserId(request.getUserId() != null ? request.getUserId() : 1);

        // 8. Actualizar estado de la reserva a PAGADA
        reservation.setStatus(ReservationStatus.PAGADA);
        reservation.setModifiedByUserId(request.getUserId() != null ? request.getUserId() : 1);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);

        // 9. Guardar el pago
        return paymentRepository.save(payment);
    }

    private void validateCardData(PaymentRequestDTO request) {
        // Validar número de tarjeta (16 dígitos, solo números)
        String cardNumber = request.getCardNumber().replaceAll("\\s", "");
        if (!cardNumber.matches("\\d{16}")) {
            throw new RuntimeException("Número de tarjeta inválido. Debe tener 16 dígitos.");
        }

        // Validar fecha de expiración (MM/YY o MM/YYYY)
        String expiration = request.getCardExpiration();
        if (!expiration.matches("(0[1-9]|1[0-2])/(\\d{2}|\\d{4})")) {
            throw new RuntimeException("Fecha de expiración inválida. Formato: MM/YY o MM/YYYY");
        }

        // Validar CVV (3 dígitos)
        if (!request.getCardCvv().matches("\\d{3}")) {
            throw new RuntimeException("CVV inválido. Debe tener 3 dígitos.");
        }

        // Validar nombre del titular
        if (request.getCardHolderName() == null || request.getCardHolderName().trim().isEmpty()) {
            throw new RuntimeException("Nombre del titular es requerido");
        }
    }

    private String maskCardNumber(String cardNumber) {
        String clean = cardNumber.replaceAll("\\s", "");
        if (clean.length() >= 4) {
            return "**** **** **** " + clean.substring(clean.length() - 4);
        }
        return "****";
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase() +
                "-" + System.currentTimeMillis();
    }

    public PaymentEntity savePayment(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }

    public PaymentEntity updatePayment(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }
}

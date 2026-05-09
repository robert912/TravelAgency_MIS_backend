package com.travel.app.services;

import com.travel.app.dtos.PaymentRequestDTO;
import com.travel.app.entities.PaymentEntity;
import com.travel.app.entities.ReservationEntity;
import com.travel.app.entities.TourPackageEntity;
import com.travel.app.enums.ReservationStatus;
import com.travel.app.repositories.PaymentRepository;
import com.travel.app.repositories.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void processPaymentUsesReservationTotalAmountAndMarksReservationAsPaid() {
        ReservationEntity reservation = pendingReservation();
        reservation.setTotalAmount(new BigDecimal("250000.00"));

        when(reservationRepository.findById(10L)).thenReturn(Optional.of(reservation));
        when(paymentRepository.existsByReservationId(10L)).thenReturn(false);
        when(paymentRepository.save(any(PaymentEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentEntity payment = paymentService.processPayment(validPaymentRequest());

        assertEquals(new BigDecimal("250000.00"), payment.getAmount());
        assertEquals(ReservationStatus.PAGADA, reservation.getStatus());
        assertEquals(7L, reservation.getModifiedByUserId());
        verify(reservationRepository).save(reservation);
    }

    @Test
    void processPaymentFallsBackToPackagePriceTimesPassengersWhenTotalAmountIsMissing() {
        ReservationEntity reservation = pendingReservation();
        reservation.setTotalAmount(null);
        reservation.setPassengersCount(3);
        reservation.getTourPackage().setPrice(new BigDecimal("120000.00"));

        when(reservationRepository.findById(10L)).thenReturn(Optional.of(reservation));
        when(paymentRepository.existsByReservationId(10L)).thenReturn(false);
        when(paymentRepository.save(any(PaymentEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentEntity payment = paymentService.processPayment(validPaymentRequest());

        assertEquals(new BigDecimal("360000.00"), payment.getAmount());
    }

    @Test
    void processPaymentRejectsReservationThatAlreadyHasPayment() {
        when(reservationRepository.findById(10L)).thenReturn(Optional.of(pendingReservation()));
        when(paymentRepository.existsByReservationId(10L)).thenReturn(true);

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> paymentService.processPayment(validPaymentRequest()));

        assertEquals("Esta reserva ya tiene un pago registrado", error.getMessage());
    }

    @Test
    void processPaymentMasksCardNumberBeforeSaving() {
        when(reservationRepository.findById(10L)).thenReturn(Optional.of(pendingReservation()));
        when(paymentRepository.existsByReservationId(10L)).thenReturn(false);
        when(paymentRepository.save(any(PaymentEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ArgumentCaptor<PaymentEntity> paymentCaptor = ArgumentCaptor.forClass(PaymentEntity.class);

        paymentService.processPayment(validPaymentRequest());

        verify(paymentRepository).save(paymentCaptor.capture());
        assertEquals("**** **** **** 1111", paymentCaptor.getValue().getCardNumber());
        assertEquals("***", paymentCaptor.getValue().getCardCvv());
    }

    private ReservationEntity pendingReservation() {
        TourPackageEntity tourPackage = new TourPackageEntity();
        tourPackage.setPrice(new BigDecimal("100000.00"));

        ReservationEntity reservation = new ReservationEntity();
        reservation.setId(10L);
        reservation.setActive(1);
        reservation.setStatus(ReservationStatus.PENDIENTE);
        reservation.setPassengersCount(2);
        reservation.setTourPackage(tourPackage);
        return reservation;
    }

    private PaymentRequestDTO validPaymentRequest() {
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setReservationId(10L);
        request.setCardNumber("4111111111111111");
        request.setCardHolderName("Cliente Prueba");
        request.setCardExpiration("12/28");
        request.setCardCvv("123");
        request.setPaymentMethod("CREDIT_CARD");
        request.setUserId(7L);
        return request;
    }
}

package com.travel.app.services;

import com.travel.app.entities.PaymentEntity;
import com.travel.app.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    // Obtener todos los pagos
    public List<PaymentEntity> getPayments() {
        return (List<PaymentEntity>) paymentRepository.findAll();
    }

    // Registrar un nuevo pago
    public PaymentEntity savePayment(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }

    // Obtener pago por ID
    public PaymentEntity getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    // Actualizar un pago
    public PaymentEntity updatePayment(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }
}
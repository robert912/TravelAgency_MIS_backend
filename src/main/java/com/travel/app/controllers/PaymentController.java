package com.travel.app.controllers;

import com.travel.app.entities.PaymentEntity;
import com.travel.app.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @GetMapping("/")
    public ResponseEntity<List<PaymentEntity>> listPayments() {
        List<PaymentEntity> payments = paymentService.getPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentEntity> getPaymentById(@PathVariable Long id) {
        PaymentEntity payment = paymentService.getPaymentById(id);
        return payment != null ? ResponseEntity.ok(payment) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<PaymentEntity> savePayment(@RequestBody PaymentEntity payment) {
        PaymentEntity newPayment = paymentService.savePayment(payment);
        return ResponseEntity.ok(newPayment);
    }

    @PutMapping("/")
    public ResponseEntity<PaymentEntity> updatePayment(@RequestBody PaymentEntity payment) {
        PaymentEntity updatedPayment = paymentService.updatePayment(payment);
        return ResponseEntity.ok(updatedPayment);
    }

}
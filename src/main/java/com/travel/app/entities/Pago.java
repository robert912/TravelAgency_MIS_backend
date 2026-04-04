package com.travel.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 FK correcta aquí
    @OneToOne
    @JoinColumn(name = "id_reserva", nullable = false, unique = true)
    private Reserva reserva;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private String medioPago;

    private String numeroTarjeta;
    private String fechaExpiracion;
    private String codigoCvv;

    @Column(nullable = false, unique = true)
    private String transaccion;

    private Long idUsuarioCreador;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
package com.travel.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "reserva_pasajero",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id_reserva", "id_persona"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaPasajero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 Reserva asociada
    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Reserva reserva;

    // 🔥 Persona que viaja
    @ManyToOne
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer activo = 1;

    private Long idUsuarioCreador;
    private Long idUsuarioModificador;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

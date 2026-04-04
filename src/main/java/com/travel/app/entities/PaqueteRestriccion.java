package com.travel.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "paquete_restriccion",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_paquete","id_restriccion"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaqueteRestriccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_paquete", nullable = false)
    private PaqueteTuristico paquete;

    @ManyToOne
    @JoinColumn(name = "id_restriccion", nullable = false)
    private Restriccion restriccion;

    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer activo = 1;

    private Long idUsuarioCreador;
    private Long idUsuarioModificador;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
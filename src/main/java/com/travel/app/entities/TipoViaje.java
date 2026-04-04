package com.travel.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tipo_viaje")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoViaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer activo = 1;

    private Long idUsuarioCreador;
    private Long idUsuarioModificador;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
package com.travel.app.entities;

import com.travel.app.enums.EstadoPaquete;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "paquete_turistico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaqueteTuristico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @NotBlank
    @Column(nullable = false)
    private String destino;

    @ManyToOne
    @JoinColumn(name = "id_temporada", nullable = false)
    private Temporada temporada;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_tipo_viaje", nullable = false)
    private TipoViaje tipoViaje;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaTermino;

    @DecimalMin(value = "0.01")
    @Column(nullable = false)
    private BigDecimal precio;

    @Min(1)
    @Column(nullable = false)
    private Integer cuposTotales;

    @Enumerated(EnumType.STRING)
    private EstadoPaquete estado;

    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer activo = 1;

    private Long idUsuarioCreador;
    private Long idUsuarioModificador;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (fechaTermino != null && fechaInicio != null &&
                fechaTermino.isBefore(fechaInicio)) {
            throw new RuntimeException("La fecha de término debe ser mayor a la fecha de inicio");
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
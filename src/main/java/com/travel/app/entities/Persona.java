package com.travel.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "persona",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "identificacion"),
                @UniqueConstraint(columnNames = "email")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "identificacion", nullable = false, unique = true)
    private String identificacion;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "nacionalidad")
    private String nacionalidad;

    @Column(name = "activo", columnDefinition = "TINYINT DEFAULT 1")
    private Integer activo = 1;

    // LADO INVERSO
    @OneToOne(mappedBy = "persona")
    private Usuario usuario;

    @Column(name = "id_usuario_creador")
    private Long idUsuarioCreador;

    @Column(name = "id_usuario_modificador")
    private Long idUsuarioModificador;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
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
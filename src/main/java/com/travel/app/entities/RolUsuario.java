package com.travel.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "rol_usuario",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id_usuario", "id_rol"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 Relación con Usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    // 🔥 Relación con Rol
    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Rol rol;

    @Column(name = "activo", columnDefinition = "TINYINT DEFAULT 1")
    private Integer activo = 1;

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
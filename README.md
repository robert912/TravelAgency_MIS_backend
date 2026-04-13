# 🚀 Travel API

## 📌 Requisitos

- Tener una base de datos creada llamada:

```sql
CREATE DATABASE dbtravel;
```

## ▶️ Ejecución del proyecto

1. Configurar las credenciales de la base de datos en `application.properties`
2. Ejecutar el proyecto Spring Boot

---

## 📚 Documentación de la API

Una vez levantado el proyecto, puedes ver y probar los endpoints en:

👉 http://localhost:8090/swagger-ui/index.html

---

## 🛠️ Notas

- Asegúrate de que la base de datos esté corriendo antes de iniciar la aplicación
- El puerto configurado es `8090`




## 📌 SQL

```sql
INSERT INTO season (id, name, description, active, created_by_user_id, modified_by_user_id, created_at, updated_at) VALUES
(1, 'Verano', 'Temporada alta con clima cálido', 1, 1, 1, NOW(), NOW()),
(2, 'Invierno', 'Temporada fría ideal para nieve', 1, 1, 1, NOW(), NOW()),
(3, 'Primavera', 'Clima templado y paisajes florales', 1, 1, 1, NOW(), NOW());

INSERT INTO category (id, name, description, active, created_by_user_id, modified_by_user_id, created_at, updated_at) VALUES
(1, 'Aventura', 'Actividades extremas y excursiones', 1, 1, 1, NOW(), NOW()),
(2, 'Relax', 'Descanso y bienestar', 1, 1, 1, NOW(), NOW()),
(3, 'Cultural', 'Turismo cultural e histórico', 1, 1, 1, NOW(), NOW());

INSERT INTO travel_type (id, name, description, active, created_by_user_id, modified_by_user_id, created_at, updated_at) VALUES
(1, 'Familiar', 'Viajes para toda la familia', 1, 1, 1, NOW(), NOW()),
(2, 'Pareja', 'Viajes románticos', 1, 1, 1, NOW(), NOW()),
(3, 'Solo', 'Viajes individuales', 1, 1, 1, NOW(), NOW());

INSERT INTO tour_package ( id, name, destination, season_id, category_id, travel_type_id, description, start_date, end_date, price, total_slots, status, active, created_by_user_id, modified_by_user_id, created_at, updated_at)
VALUES
-- 🌴 Cancún
( 1, 'Viaje a Cancún Todo Incluido', 'México', 1, 2, 2, 'Vuelo + alojamiento + traslados. 8 días / 7 noches.', '2026-12-10', '2026-12-17', 850000, 20, 'DISPONIBLE', 1, 1, 1, NOW(), NOW()),

-- 🏝️ Punta Cana
( 2, 'Escapada Premium a Punta Cana', 'República Dominicana', 1, 2, 2, 'Vuelo + alojamiento todo incluido. 6 días / 5 noches.', '2026-11-05', '2026-11-10', 920000, 15, 'DISPONIBLE', 1, 1, 1, NOW(), NOW()),

-- 🇧🇷 Río de Janeiro
( 3, 'Aventura en Río de Janeiro', 'Brasil', 3,  1,  3, 'Vuelo + hotel con desayuno incluido. 5 días / 4 noches.', '2026-10-01', '2026-10-05', 450000, 25, 'DISPONIBLE', 1, 1, 1, NOW(), NOW());
 ```
 
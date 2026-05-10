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


## Environment variables
DB_HOST=localhost;DB_PORT=3306;DB_NAME=dbtravel;DB_USER=root;DB_PASSWORD=12345678;KEYCLOAK_HOST=localhost

## Iniciar la imagen de docker
```bash
docker build -t robert912/tourpackage-backend:latest .
```
```bash
docker run -d --name tour-backend ^
  -p 8090:8090 ^
  -e DB_HOST=host.docker.internal ^
  -e DB_PORT=3306 ^
  -e DB_NAME=dbtravel ^
  -e DB_USER=root ^
  -e DB_PASSWORD=12345678 ^
  -e KEYCLOAK_HOST=host.docker.internal ^
  robert912/tourpackage-backend
```
# HireReady — API REST

Plataforma web para que estudiantes universitarios practiquen entrevistas laborales por voz y reciban retroalimentación generada con IA. Este repositorio contiene el backend; el frontend está en [hireready-frontend-web-applications](https://github.com/AuroraaPr/hireready-frontend-web-applications).

Proyecto académico del curso Arquitectura de Aplicaciones Web — Universidad Peruana de Ciencias Aplicadas (UPC), 2026-1.

## Funcionalidades

- Registro e inicio de sesión con JWT para tres roles: administrador, empresa y postulante.
- Las empresas crean bancos de preguntas asociados a carreras.
- Los postulantes inician, continúan, abandonan y finalizan simulaciones de entrevista, respondiendo por voz.
- Cada respuesta se transcribe y se evalúa con la API de OpenAI; el sistema detecta muletillas y genera un reporte de retroalimentación por simulación.
- Dashboards de desempeño por rol e historial de simulaciones.
- Administración de usuarios (activar y desactivar), empresas, postulantes, carreras y bancos de preguntas.

## Tecnologías

Java 21 · Spring Boot 4 · Spring Security · JWT (jjwt) · Spring Data JPA · PostgreSQL · Bean Validation · Lombok · Maven · API de OpenAI

## Estructura

```
src/main/java/com/hireready
├── controllers   # endpoints REST
├── dtos          # objetos de entrada y salida
├── entities      # modelo de datos JPA
├── enums         # roles y estados de la simulación
├── exceptions    # manejo centralizado de errores
├── repositories  # acceso a datos con Spring Data JPA
├── securities    # configuración de Spring Security y filtro JWT
├── services      # interfaces de servicio
└── serviceimpl   # lógica de negocio
```

## Endpoints principales

Todas las rutas usan el prefijo `/hireready`.

| Método | Ruta | Acceso |
| --- | --- | --- |
| POST | `/login` | Público |
| POST | `/applicants`, `/companies` | Público (registro) |
| GET | `/careers` | Público |
| GET, PUT | `/applicants/me`, `/companies/me` | Postulante / Empresa |
| POST, GET | `/companies/me/question-banks` | Empresa |
| GET | `/applicants/me/question-banks` | Postulante |
| POST | `/applicants/me/simulations` | Postulante |
| POST | `/applicants/me/simulations/{id}/responses` | Postulante |
| POST | `/applicants/me/simulations/{id}/finalize` | Postulante |
| GET | `/applicants/me/simulations/{id}/report` | Postulante |
| GET | `/applicants/me/simulations/history` | Postulante |
| GET | `/applicants/me/dashboard`, `/companies/me/dashboard` | Postulante / Empresa |
| GET, POST, PUT | `/admin/**` | Administrador |

## Cómo ejecutarlo

Requisitos: Java 21, PostgreSQL y una clave de API de OpenAI.

Con el perfil `dev` (por defecto), la aplicación usa una base local llamada `db_HireReady`; revisa `src/main/resources/application-dev.properties`.

```bash
export OPENAI_API_KEY=tu_clave
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080/hireready`.

Para producción, activa el perfil `prod` (`SPRING_PROFILES_ACTIVE=prod`) y define estas variables de entorno: `DB_URL`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`, `OPENAI_API_KEY` y `FRONTEND_URL`.

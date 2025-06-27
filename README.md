# 📄 empleoMadrynBackend

**Documentación del Backend**  
Este repositorio contiene el backend del sistema de empleos de Puerto Madryn, desarrollado con Spring Boot.

---

## 🧾 Información General del Proyecto

- **Nombre del Proyecto:** empleoMadrynBackend
- **Versión:** 0.0.1-SNAPSHOT
- **Java Version:** 21
- **Spring Boot Version:** 3.4.4

---

## 🛠️ Stack Tecnológico

### ⚙️ Framework Principal
- Spring Boot 3.4.4 con Java 21  
- Spring Data JPA – Operaciones con base de datos  
- Spring Web – Creación de APIs REST  
- Spring Mail – Envío de correos electrónicos  

### 🗄️ Base de Datos
- PostgreSQL

### 🧰 Herramientas de Desarrollo
- Lombok – Reducción de código boilerplate

---

## 🧱 Arquitectura del Sistema

### 1. 🔐 Capa de Seguridad

**Configuración:**  
Maneja autenticación con JWT y configuración CORS (`SecurityConfig.java`)

#### Endpoints Públicos
- `POST /api/auth/**` – Autenticación
- `GET /api/ofertas` – Visualización de ofertas
- `GET /api/categorias` – Listado de categorías
- `/uploads/**` – Acceso a archivos estáticos

#### Endpoints Protegidos
- `/api/admin/**` – Requiere rol `ADMIN`
- Otros endpoints – Requieren autenticación

---

### 2. 🌐 Capa de Controladores (API REST)

#### `AuthController`
- `POST /api/auth/google` – Login con Google
- `POST /api/auth/validate-token` – Validación de token JWT

#### `OfertaEmpleoController`
- `POST /api/ofertas` – Crear oferta (con logo opcional)
- `GET /api/ofertas` – Listar todas las ofertas
- `GET /api/ofertas/{id}` – Obtener oferta por ID
- `PUT /api/ofertas/{id}` – Actualizar oferta
- `DELETE /api/ofertas/{id}` – Eliminar oferta
- `GET /api/ofertas/mis-avisos` – Ver mis avisos publicados

#### `AdminController`
- `GET /api/admin/{entityName}` – Listado de entidades
- `POST /api/admin/ofertas/habilitar/{id}` – Habilitar oferta

#### `FavoritosController`
- `POST /api/favoritos/{ofertaId}` – Agregar oferta a favoritos
- `DELETE /api/favoritos/{ofertaId}` – Quitar de favoritos

#### `PostulacionOfertaController`
- `POST /api/applications/apply/{jobId}` – Postularse con CV adjunto

---

### 3. 🔧 Capa de Servicios (Lógica de Negocio)

#### `OfertaEmpleoServiceImpl`
- Validación de datos de ofertas
- Conversión a DTOs
- Integración con envío de correos

#### `EmailServiceImpl`
- Notificación de nuevas ofertas a administradores
- Confirmación de aprobación a empresas
- Envío de CV en postulaciones

#### `AdminServiceImpl`
- CRUD genérico para entidades
- Habilitación de ofertas con envío de notificaciones

#### `AuthServiceImpl`
- Verificación de tokens de Google OAuth2
- Generación y validación de tokens JWT

---

### 4. 🗃️ Capa de Datos

#### Entidades Principales
- `OfertaEmpleo` – Ofertas de empleo
- `Usuario` – Usuarios del sistema
- `Categoria` – Categorías de empleos
- `Favoritos` – Ofertas marcadas como favoritas

---

## ✅ Estado del Proyecto
Proyecto en desarrollo activo con funcionalidades implementadas para gestión de ofertas, autenticación mediante Google, postulaciones y administración.

---

## 🚀 Cómo empezar

```bash
# Clonar el repositorio
git clone https://github.com/tuusuario/empleoMadrynBackend.git
cd empleoMadrynBackend

# Construir el proyecto (requiere Java 21 y Maven)
./mvnw clean install

# Ejecutar la aplicación
./mvnw spring-boot:run


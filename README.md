Documentación del Backend - empleoMadrynBackend 
Este documento proporciona información completa sobre el backend del sistema de empleos de Puerto Madryn, basado en Spring Boot.

Información General del Proyecto 
Nombre del Proyecto: empleoMadrynBackend pom.xml:12-14
Versión: 0.0.1-SNAPSHOT pom.xml:13
Java Version: 21 pom.xml:30
Spring Boot Version: 3.4.4 pom.xml:8

Stack Tecnológico 
Framework Principal 
Spring Boot 3.4.4 con Java 21 pom.xml:6-8
Spring Data JPA para operaciones de base de datos pom.xml:34-37
Spring Web para APIs REST pom.xml:38-41
Spring Mail para funcionalidad de correo electrónico pom.xml:42-45
Base de Datos 
PostgreSQL como base de datos principal pom.xml:46-50
Herramientas de Desarrollo 
Lombok para reducir código boilerplate pom.xml:51-55
Arquitectura del Sistema 
Capas de la Aplicación 
1. Capa de Seguridad
La configuración de seguridad maneja autenticación JWT y CORS SecurityConfig.java:20-23 :

Endpoints Públicos:

/api/auth/** - Autenticación SecurityConfig.java:40
GET /api/ofertas - Visualización de ofertas SecurityConfig.java:41-43
GET /api/categorias - Categorías SecurityConfig.java:42
/uploads/** - Archivos estáticos SecurityConfig.java:45
Endpoints Protegidos:

/api/admin/** - Requiere rol ADMIN SecurityConfig.java:46
Otros endpoints requieren autenticación SecurityConfig.java:47-48
2. Capa de Controladores (API REST)
AuthController - Maneja autenticación con Google OAuth2 AuthController.java:13-15 :

POST /api/auth/google - Login con Google AuthController.java:26-39
POST /api/auth/validate-token - Validación de tokens JWT AuthController.java:41-50
OfertaEmpleoController - Gestión de ofertas de empleo OfertaEmpleoController.java:23-25 :

POST /api/ofertas - Crear oferta con logo opcional OfertaEmpleoController.java:39-62
GET /api/ofertas - Listar todas las ofertas OfertaEmpleoController.java:64-68
GET /api/ofertas/{id} - Obtener oferta por ID OfertaEmpleoController.java:70-74
PUT /api/ofertas/{id} - Actualizar oferta OfertaEmpleoController.java:76-106
DELETE /api/ofertas/{id} - Eliminar oferta OfertaEmpleoController.java:108-112
GET /api/ofertas/mis-avisos - Ofertas del usuario autenticado OfertaEmpleoController.java:114-119
AdminController - Operaciones administrativas AdminController.java:15-17 :

GET /api/admin/{entityName} - Listar entidades AdminController.java:27-30
POST /api/admin/ofertas/habilitar/{id} - Habilitar oferta de empleo AdminController.java:51-68
FavoritosController - Gestión de favoritos FavoritosController.java:13-15 :

POST /api/favoritos/{ofertaId} - Agregar a favoritos FavoritosController.java:23-30
DELETE /api/favoritos/{ofertaId} - Remover de favoritos FavoritosController.java:32-39
PostulacionOfertaController - Postulaciones a ofertas PostulacionOfertaController.java:13-15 :

POST /api/applications/apply/{jobId} - Postularse con CV PostulacionOfertaController.java:23-51
3. Capa de Servicios (Lógica de Negocio)
OfertaEmpleoServiceImpl - Gestión completa de ofertas OfertaEmpleoServiceImpl.java:24-25 :

Validación de datos de oferta OfertaEmpleoServiceImpl.java:148-161
Conversión a DTOs OfertaEmpleoServiceImpl.java:163-193
Integración con servicio de email OfertaEmpleoServiceImpl.java:44-47
EmailServiceImpl - Sistema completo de notificaciones por email EmailService.java:6-13 :

Notificación de nueva oferta a administradores EmailService.java:9
Notificación de aprobación a empresas EmailServiceImpl.java:49-117
Envío de postulaciones con CV adjunto EmailService.java:11
AdminServiceImpl - Operaciones administrativas genéricas AdminServiceImpl.java:20-21 :

CRUD genérico para entidades AdminServiceImpl.java:26-35
Habilitación de ofertas con notificación automática AdminServiceImpl.java:74-88
AuthServiceImpl - Autenticación con Google OAuth2 AuthServiceImpl.java:20-21 :

Verificación de tokens de Google AuthServiceImpl.java:35-69
Generación de tokens JWT AuthServiceImpl.java:71-74
4. Capa de Datos
Entidades Principales:

OfertaEmpleo - Ofertas de trabajo con validaciones OfertaEmpleo.java:14-19
Usuario - Usuarios del sistema OfertaEmpleo.java:33-37
Categoria - Categorías de empleos OfertaEmpleo.java:60-63
Favoritos - Ofertas favoritas de usuarios <cite repo="madrynempleos/empleoP

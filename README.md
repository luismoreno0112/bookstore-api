# bookstore-api

API REST para gestión de una librería en línea, desarrollada con Spring Boot.

## Tecnologías

- Java 17
- Spring Boot 4.0.5
- Spring Security + JWT (jjwt 0.12.6)
- Spring Data JPA + H2 (desarrollo)
- Lombok
- SpringDoc OpenAPI (Swagger)
- Maven

## Requisitos previos

- Java 17+
- Maven 3.8+

## Configuración y ejecución local

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/bookstore-api.git
cd bookstore-api
```

### 2. Configurar variable de entorno JWT (opcional en dev)

El proyecto ya incluye un secret por defecto en `application.yml` para desarrollo.
En producción se debe usar variable de entorno:

```bash
export JWT_SECRET=tu_secret_seguro_aqui
```

### 3. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación arranca en: `http://localhost:8080/api/v1`

### 4. Consola H2 (base de datos en memoria)

Disponible en: `http://localhost:8080/api/v1/h2-console`

| Campo | Valor |
|---|---|
| JDBC URL | `jdbc:h2:mem:bookstoredb` |
| User | `sa` |
| Password | *(vacío)* |

### 5. Documentación Swagger

Disponible en: `http://localhost:8080/api/v1/swagger-ui/index.html`

## Endpoints principales

### Autenticación (públicos)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/auth/register` | Registrar usuario |
| POST | `/auth/login` | Iniciar sesión → retorna JWT |

### Libros
| Método | Endpoint | Acceso |
|--------|----------|--------|
| GET | `/books` | Público (soporta `?categoryId=` y `?authorId=`) |
| GET | `/books/{id}` | Público |
| POST | `/books` | ADMIN |
| PUT | `/books/{id}` | ADMIN |
| DELETE | `/books/{id}` | ADMIN |

### Autores
| Método | Endpoint | Acceso |
|--------|----------|--------|
| GET | `/authors` | Público |
| GET | `/authors/{id}` | Público |
| GET | `/authors/{id}/books` | Público |
| POST | `/authors` | Autenticado |
| PUT | `/authors/{id}` | Autenticado |
| DELETE | `/authors/{id}` | ADMIN |

### Categorías
| Método | Endpoint | Acceso |
|--------|----------|--------|
| GET | `/categories` | Público |
| GET | `/categories/{id}/books` | Público |
| POST | `/categories` | Autenticado |
| PUT | `/categories/{id}` | Autenticado |
| DELETE | `/categories/{id}` | Autenticado |

### Pedidos
| Método | Endpoint | Acceso |
|--------|----------|--------|
| POST | `/orders` | USER autenticado |
| GET | `/orders` | ADMIN |
| GET | `/orders/my` | USER autenticado |
| GET | `/orders/{id}` | Propietario o ADMIN |
| PUT | `/orders/{id}/cancel` | Propietario o ADMIN |

## Uso con Postman

1. Registrarse: `POST /auth/register`
2. Hacer login: `POST /auth/login` → copiar el `token`
3. En cada request protegido: Header `Authorization: Bearer <token>`

## Flujo Git

```
main
└── develop
    ├── feature/auth-module
    ├── feature/book-catalog
    ├── feature/order-management
    └── feature/author-category
```

### Convención de commits

```
feat: descripción
fix: descripción
refactor: descripción
docs: descripción
chore: descripción
```

## Estructura del proyecto

```
com.taller.bookstore
├── config/          # Beans globales (CORS, Swagger, PasswordEncoder)
├── controller/      # Endpoints HTTP
├── dto/
│   ├── request/     # Contratos de entrada
│   └── response/    # Contratos de salida (ApiResponse, ApiErrorResponse)
├── entity/          # Modelos JPA
├── exception/
│   ├── custom/      # Excepciones de dominio
│   └── handler/     # GlobalExceptionHandler
├── mapper/          # Conversión entidad ↔ DTO (manual, sin librerías)
├── repository/      # Spring Data JPA
├── security/        # JWT + Spring Security
└── service/
    └── impl/        # Lógica de negocio
```

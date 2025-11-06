# 🏦 BankApp - Sistema de Gestión Bancaria

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Sistema bancario moderno desarrollado con **Arquitectura Hexagonal (Ports & Adapters)** y principios de **Clean Architecture**, implementando gestión de clientes, cuentas y movimientos bancarios.

---

## 📋 Tabla de Contenidos

- [Características](#características)
- [Arquitectura](#arquitectura)
- [Tecnologías](#tecnologías)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#instalación-y-configuración)
- [Ejecución](#ejecución)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Principios de Diseño](#principios-de-diseño)

---

## Características

### Funcionalidades Principales

- ✅ **Gestión de Clientes**: CRUD completo con validaciones
- ✅ **Gestión de Cuentas**: Creación y administración de cuentas bancarias
- ✅ **Movimientos Bancarios**: Registro de débitos y créditos con validación de saldo

### Características Técnicas

- **Arquitectura Hexagonal**: Separación clara de capas y dependencias
- **Seguridad**: Encriptación de contraseñas con BCrypt
- **Reactividad**: Endpoints reactivos con Project Reactor
- **Clean Code**: Principios SOLID y DIP aplicados
- **Base de Datos**: PostgreSQL con migraciones automáticas
- **Testing**: Tests unitarios y de integración con H2

---

## Arquitectura

El proyecto implementa **Arquitectura Hexagonal** con clara separación de responsabilidades:

### Diagrama de Capas

![Diagrama de Capas](./images/DIAGRAMA%20DE%20CAPAS.png)

### Flujo de Dependencias (Principio DIP)

![Flujo de Dependencias](./images/FLUJO%20DE%20DEPENDENCIAS.png)

### Flujo de una Operación (Ejemplo: Crear Movimiento)

![Flujo de una Operación](./images/FLUJO%20DE%20UNA%20OPERACION.png)

---

## Tecnologías

### Backend
- **Java 17**: Lenguaje base
- **Spring Boot 3.5.6**: Framework principal
- **Spring Data JPA**: Capa de persistencia
- **Spring WebFlux**: Endpoints reactivos
- **Spring Security**: Autenticación y encriptación
- **Project Reactor**: Programación reactiva

### Base de Datos
- **PostgreSQL 15**: Base de datos principal
- **H2 Database**: Base de datos en memoria para tests
- **Hibernate**: ORM

### Testing
- **JUnit 5**: Framework de testing
- **Mockito**: Mocking
- **Reactor Test**: Testing reactivo

### Herramientas
- **Maven**: Gestión de dependencias
- **Lombok**: Reducción de boilerplate
- **Docker Compose**: Orquestación de contenedores

---

## Requisitos Previos

- **Java 17** o superior
- **Maven 3.6+**
- **Docker & Docker Compose** (opcional, para PostgreSQL)
- **PostgreSQL 15** (si no usas Docker)

---

## Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/SebasRo17/Challenge.git
cd Challenge/bankapp/bankapp
```

### 2. Configurar Base de Datos

#### Opción A: Usando Docker Compose (Recomendado)

```bash
docker-compose up -d
```

Esto levantará PostgreSQL en `localhost:5432` con las credenciales por defecto.

#### Opción B: PostgreSQL Local

Crea la base de datos manualmente:

```sql
CREATE DATABASE banco_db;
CREATE USER admin WITH PASSWORD 'admin';
GRANT ALL PRIVILEGES ON DATABASE banco_db TO admin;
```

### 3. Configurar `application.properties` (Opcional)

Si usas credenciales diferentes, edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/banco_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

---

## ▶️ Ejecución

### Compilar el Proyecto

```bash
./mvnw clean install
```

### Ejecutar la Aplicación

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

### Verificar Salud

```bash
curl http://localhost:8080/api/v1/customers
```

---

## API Endpoints

### Clientes (Customers)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/customers` | Listar todos los clientes |
| GET | `/api/v1/customers/{id}` | Obtener cliente por ID |
| POST | `/api/v1/customers` | Crear nuevo cliente |
| PUT | `/api/v1/customers/{id}` | Actualizar cliente |
| DELETE | `/api/v1/customers/{id}` | Eliminar cliente |

#### Ejemplo Request: Crear Cliente

```json
POST /api/v1/customers
Content-Type: application/json

{
  "name": "Juan Pérez",
  "gender": "Masculino",
  "address": "Calle Principal 123",
  "phone": "0991234567",
  "identification": "1234567890",
  "password": "12345",
  "status": true
}
```

### Cuentas (Accounts)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/accounts` | Listar todas las cuentas |
| GET | `/api/v1/accounts/{accountNumber}` | Obtener cuenta por número |
| POST | `/api/v1/accounts` | Crear nueva cuenta |
| PUT | `/api/v1/accounts/{accountNumber}` | Actualizar cuenta |
| DELETE | `/api/v1/accounts/{accountNumber}` | Eliminar cuenta |

#### Ejemplo Request: Crear Cuenta

```json
POST /api/v1/accounts
Content-Type: application/json

{
  "accountNumber": "478758",
  "accountType": "Ahorros",
  "initialBalance": 1000.00,
  "status": true,
  "customerId": 1
}
```

### Movimientos (Movements)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/movements` | Registrar nuevo movimiento |

#### Ejemplo Request: Crear Movimiento (Débito)

```json
POST /api/v1/movements
Content-Type: application/json

{
  "movementType": "Débito",
  "value": 575.00,
  "accountNumber": "478758"
}
```

#### Ejemplo Request: Crear Movimiento (Crédito)

```json
POST /api/v1/movements
Content-Type: application/json

{
  "movementType": "Crédito",
  "value": 2000.00,
  "accountNumber": "478758"
}
```

---

## Testing

### Ejecutar Todos los Tests

```bash
./mvnw test
```

### Ejecutar Tests con Cobertura

```bash
./mvnw test jacoco:report
```

El reporte estará en: `target/site/jacoco/index.html`

### Tests Incluidos

- ✅ **MovementUseCaseTest**: Tests unitarios de lógica de movimientos
  - Débito exitoso con saldo suficiente
  - Débito fallido por saldo insuficiente
- ✅ **BankappApplicationTests**: Test de contexto de Spring

---

## Estructura del Proyecto

```
bankapp/
├── src/
│   ├── main/
│   │   ├── java/com/ntt/challenge/bankapp/
│   │   │   ├── domain/                    # 💎 Capa de Dominio
│   │   │   │   ├── model/                 # Modelos puros (POJOs)
│   │   │   │   │   ├── Account.java
│   │   │   │   │   ├── Customer.java
│   │   │   │   │   ├── Movement.java
│   │   │   │   │   └── Person.java
│   │   │   │   ├── repository/            # Puertos (interfaces)
│   │   │   │   │   ├── AccountRepository.java
│   │   │   │   │   ├── CustomerRepository.java
│   │   │   │   │   └── MovementRepository.java
│   │   │   │   ├── service/               # Contratos de servicios
│   │   │   │   │   ├── AccountService.java
│   │   │   │   │   ├── CustomerService.java
│   │   │   │   │   └── MovementService.java
│   │   │   │   ├── policy/                # Políticas de negocio
│   │   │   │   │   ├── MovementPolicy.java
│   │   │   │   │   └── DefaultMovementPolicy.java
│   │   │   │   └── exception/             # Excepciones de dominio
│   │   │   │       ├── InsufficientBalanceException.java
│   │   │   │       └── AccountTypeAlreadyExistsException.java
│   │   │   │
│   │   │   ├── application/               # 🎯 Capa de Aplicación
│   │   │   │   ├── usecase/               # Casos de uso
│   │   │   │   │   ├── AccountUseCase.java
│   │   │   │   │   ├── CustomerUseCase.java
│   │   │   │   │   └── MovementUseCase.java
│   │   │   │   ├── dto/                   # Data Transfer Objects
│   │   │   │   │   ├── AccountDto.java
│   │   │   │   │   ├── CustomerDto.java
│   │   │   │   │   └── MovementDto.java
│   │   │   │   └── mapper/                # Conversores DTO ↔ Domain ↔ Entity
│   │   │   │       ├── AccountDtoMapper.java
│   │   │   │       ├── AccountEntityMapper.java
│   │   │   │       ├── CustomerDtoMapper.java
│   │   │   │       ├── CustomerEntityMapper.java
│   │   │   │       ├── MovementDtoMapper.java
│   │   │   │       └── MovementEntityMapper.java
│   │   │   │
│   │   │   └── infrastructure/            # 🌐 Capa de Infraestructura
│   │   │       ├── entrypoint/            # Controladores REST
│   │   │       │   ├── AccountController.java
│   │   │       │   ├── CustomerController.java
│   │   │       │   └── MovementController.java
│   │   │       ├── repository/            # Implementaciones JPA
│   │   │       │   ├── AccountJpaRepository.java
│   │   │       │   ├── CustomerJpaRepository.java
│   │   │       │   ├── MovementJpaRepository.java
│   │   │       │   └── adapter/           # Adaptadores de puertos
│   │   │       │       ├── AccountRepositoryAdapter.java
│   │   │       │       ├── CustomerRepositoryAdapter.java
│   │   │       │       └── MovementRepositoryAdapter.java
│   │   │       ├── persistence/           # Entidades JPA
│   │   │       │   └── entity/
│   │   │       │       ├── AccountEntity.java
│   │   │       │       ├── CustomerEntity.java
│   │   │       │       └── MovementEntity.java
│   │   │       └── config/                # Configuraciones
│   │   │           ├── SecurityConfig.java
│   │   │           ├── DomainConfig.java
│   │   │           └── GlobalExceptionHandler.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── openapi.yaml
│   │
│   └── test/
│       ├── java/com/ntt/challenge/bankapp/
│       │   └── application/usecase/
│       │       └── MovementUseCaseTest.java
│       └── resources/
│           └── application.properties     # Config H2 para tests
│
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## Principios de Diseño

### 1. **Arquitectura Hexagonal (Ports & Adapters)**

- **Dominio puro**: Sin dependencias de frameworks
- **Puertos**: Interfaces que definen contratos
- **Adaptadores**: Implementaciones concretas de infraestructura

### 2. **Dependency Inversion Principle (DIP)**

```
Infrastructure → Application → Domain
     ↓              ↓             ↑
  Adapters    Use Cases     Ports (interfaces)
```

Las capas externas dependen de las internas, **nunca al revés**.

### 3. **Separation of Concerns**

- **Domain Models**: POJOs sin anotaciones JPA/Jackson
- **JPA Entities**: Solo en infraestructura
- **DTOs**: Para entrada/salida de controladores
- **Mappers**: Conversión entre capas

### 4. **Single Responsibility**

- **Use Cases**: Orquestan la lógica de negocio
- **Policies**: Encapsulan reglas de negocio complejas
- **Repositories**: Solo acceso a datos

### 5. **Clean Code**

- Nombres descriptivos
- Métodos pequeños y cohesivos
- Validaciones tempranas
- Manejo explícito de errores

---

## Modelo de Datos

![Modelo de Datos](./images/MODELO%20DE%20DATOS.png)

---

## Seguridad

### Encriptación de Contraseñas

Las contraseñas se encriptan usando **BCrypt** antes de ser almacenadas:

```java
String encodedPassword = passwordEncoder.encode(customer.getPassword());
```

### Configuración Actual

Por defecto, **todos los endpoints están abiertos** para facilitar el desarrollo. En producción, se recomienda:

1. Implementar autenticación JWT
2. Configurar roles y permisos
3. Habilitar HTTPS
4. Validar CSRF tokens

---

## 🐛 Manejo de Errores

### Excepciones Personalizadas

| Excepción | HTTP Status | Descripción |
|-----------|-------------|-------------|
| `InsufficientBalanceException` | 400 | Saldo insuficiente para débito |
| `AccountTypeAlreadyExistsException` | 409 | Cliente ya tiene cuenta de ese tipo |
| `DataIntegrityViolationException` | 409 | Violación de restricción única |
| `WebExchangeBindException` | 400 | Validación de DTO fallida |
| `RuntimeException` | 500 | Error genérico |

### Ejemplo de Respuesta de Error

```json
{
  "error": "Saldo no disponible",
  "mensaje": "El saldo actual (1000.00) es insuficiente para el débito de 1500.00"
}
```

---

## 🔄 Flujo de Datos Completo

### Ejemplo: Registro de Movimiento Bancario

1. **Cliente** envía `POST /api/v1/movements` con `MovementDto`
2. **Controller** valida DTO con `@Valid`
3. **Mapper** convierte `MovementDto → Movement` (domain)
4. **Use Case** orquesta:
   - Consulta cuenta via **Port** → **Adapter** → **JPA Repo**
   - Obtiene último saldo
   - Invoca **Policy** para calcular nuevo saldo
   - Valida reglas de negocio (saldo suficiente)
   - Guarda movimiento via **Port** → **Adapter** → **JPA Repo**
5. **Mapper** convierte `Movement → MovementDto`
6. **Controller** retorna respuesta al cliente

---


## 📧 Contacto

**Proyecto**: [Challenge](https://github.com/SebasRo17/Challenge)  
**Autor**: SebasRo17

---

<div align="center">
  <p>Desarrollado usando Clean Architecture y Spring Boot</p>
  <p>Si te gustó este proyecto, considera darle una estrella ⭐</p>
</div>

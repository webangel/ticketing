# API Ticketing - Documentación de Endpoints

## Información General

- **Base URL:** `http://localhost:8080`
- **Autenticación:** JWT Bearer Token
- **Content-Type:** `application/json`

---

## Enums

| Enum | Valores |
|------|---------|
| **UserRole** | `ADMIN`, `CUSTOMER`, `ORGANIZER` |
| **EventStatus** | `DRAFT`, `PUBLISHED`, `CANCELLED`, `COMPLETED` |
| **TicketStatus** | `IN_QUEUE`, `PROCESSING`, `COMPLETED`, `REJECTED`, `CANCELLED` |

---

## Headers para Endpoints Autenticados

```
Authorization: Bearer <token>
Content-Type: application/json
```

---

## 1. Autenticación (`/api/auth`)

### 1.1 Registrar Usuario

| Campo | Descripción |
|-------|-------------|
| **Método** | `POST` |
| **URL** | `/api/auth/register` |
| **Auth** | No requerida |

**Request Body:**
```json
{
  "name": "string (requerido)",
  "email": "string (requerido, formato email)",
  "password": "string (requerido, mínimo 6 caracteres)",
  "role": "enum: ADMIN, CUSTOMER, ORGANIZER (requerido)"
}
```

**Response (200 OK):**
```json
"Usuario registrado exitosamente"
```

---

### 1.2 Iniciar Sesión

| Campo | Descripción |
|-------|-------------|
| **Método** | `POST` |
| **URL** | `/api/auth/login` |
| **Auth** | No requerida |

**Request Body:**
```json
{
  "email": "string (requerido)",
  "password": "string (requerido)"
}
```

**Response (200 OK):**
```json
{
  "token": "string (JWT token)"
}
```

---

## 2. Usuarios (`/api/users`)

### 2.1 Obtener Todos los Usuarios

| Campo | Descripción |
|-------|-------------|
| **Método** | `GET` |
| **URL** | `/api/users` |
| **Auth** | Requerida: `USER_READ_ALL` |

**Response (200 OK):**
```json
[
  {
    "userId": "string",
    "name": "string",
    "email": "string",
    "role": "enum",
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
]
```

---

### 2.2 Obtener Usuario por ID

| Campo | Descripción |
|-------|-------------|
| **Método** | `GET` |
| **URL** | `/api/users/{userId}` |
| **Auth** | Requerida: `USER_READ_ONE` |

**Path Parameters:**
- `userId` (String): ID del usuario

**Response (200 OK):**
```json
{
  "userId": "string",
  "name": "string",
  "email": "string",
  "role": "enum",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

### 2.3 Actualizar Usuario

| Campo | Descripción |
|-------|-------------|
| **Método** | `PUT` |
| **URL** | `/api/users/{userId}` |
| **Auth** | Requerida: `USER_UPDATE` |

**Path Parameters:**
- `userId` (String): ID del usuario

**Request Body:**
```json
{
  "name": "string (requerido)",
  "email": "string (requerido)",
  "password": "string (requerido)",
  "role": "enum (requerido)"
}
```

**Response (200 OK):**
```json
{
  "userId": "string",
  "name": "string",
  "email": "string",
  "role": "enum",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

### 2.4 Eliminar Usuario

| Campo | Descripción |
|-------|-------------|
| **Método** | `DELETE` |
| **URL** | `/api/users/{userId}` |
| **Auth** | Requerida: `USER_DELETE` |

**Path Parameters:**
- `userId` (String): ID del usuario

**Response:** `204 No Content`

---

### 2.5 Restablecer Contraseña

| Campo | Descripción |
|-------|-------------|
| **Método** | `POST` |
| **URL** | `/api/users/reset-password?email={email}` |
| **Auth** | No requerida |

**Query Parameters:**
- `email` (String): Email del usuario

**Response (200 OK):**
```
"Contraseña restablecida para {email}"
```

---

## 3. Eventos (`/api/events`)

### 3.1 Crear Evento

| Campo | Descripción |
|-------|-------------|
| **Método** | `POST` |
| **URL** | `/api/events` |
| **Auth** | Requerida: `EVENT_CREATE` |

**Request Body:**
```json
{
  "name": "string (requerido)",
  "eventDate": "datetime (requerido, fecha futura o presente)",
  "venueId": "long (requerido)",
  "totalSeats": "int (requerido, mínimo 0)",
  "availableSeats": "int (requerido, mínimo 0)",
  "status": "enum: DRAFT, PUBLISHED, CANCELLED, COMPLETED (opcional)",
  "price": "decimal (requerido, mayor a 0)",
  "image": "string (URL de imagen, opcional)"
}
```

**Response (201 Created):**
```json
{
  "eventId": "string",
  "name": "string",
  "eventDate": "datetime",
  "totalSeats": "int",
  "availableSeats": "int",
  "status": "enum",
  "price": "decimal",
  "image": "string (URL de imagen)",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

### 3.2 Obtener Todos los Eventos

| Campo | Descripción |
|-------|-------------|
| **Método** | `GET` |
| **URL** | `/api/events` |
| **Auth** | Requerida: `EVENT_READ_ALL` |

**Response (200 OK):**
```json
[
  {
    "eventId": "string",
    "name": "string",
    "eventDate": "datetime",
    "totalSeats": "int",
    "availableSeats": "int",
    "status": "enum",
    "price": "decimal"
  }
]
```

---

### 3.3 Obtener Evento por ID

| Campo | Descripción |
|-------|-------------|
| **Método** | `GET` |
| **URL** | `/api/events/{eventId}` |
| **Auth** | Requerida: `EVENT_READ_ONE` |

**Path Parameters:**
- `eventId` (String): ID del evento

**Response (200 OK):**
```json
{
  "eventId": "string",
  "name": "string",
  "eventDate": "datetime",
  "totalSeats": "int",
  "availableSeats": "int",
  "status": "enum",
  "price": "decimal",
  "image": "string (URL de imagen)",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

### 3.4 Actualizar Evento

| Campo | Descripción |
|-------|-------------|
| **Método** | `PUT` |
| **URL** | `/api/events/{eventId}` |
| **Auth** | Requerida: `EVENT_UPDATE` |

**Path Parameters:**
- `eventId` (String): ID del evento

**Request Body:**
```json
{
  "name": "string (requerido)",
  "eventDate": "datetime (requerido)",
  "venueId": "long (requerido)",
  "totalSeats": "int (requerido)",
  "availableSeats": "int (requerido)",
  "status": "enum (opcional)",
  "price": "decimal (requerido)"
}
```

**Response (200 OK):**
```json
{
  "eventId": "string",
  "name": "string",
  "eventDate": "datetime",
  "totalSeats": "int",
  "availableSeats": "int",
  "status": "enum",
  "price": "decimal",
  "image": "string (URL de imagen)",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

### 3.5 Eliminar Evento

| Campo | Descripción |
|-------|-------------|
| **Método** | `DELETE` |
| **URL** | `/api/events/{eventId}` |
| **Auth** | No requerida |

**Path Parameters:**
- `eventId` (String): ID del evento

**Response:** `204 No Content`

**Nota:** No se puede eliminar si el evento tiene tickets asociados.

---

### 3.6 Reducir Disponibilidad

| Campo | Descripción |
|-------|-------------|
| **Método** | `POST` |
| **URL** | `/api/events/{eventId}/reduce-availability` |
| **Auth** | No requerida |

**Path Parameters:**
- `eventId` (String): ID del evento

**Response (200 OK):**
```json
{
  "eventId": "string",
  "name": "string",
  "eventDate": "datetime",
  "totalSeats": "int",
  "availableSeats": "int",
  "status": "enum",
  "price": "decimal",
  "image": "string (URL de imagen)",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

## 4. Tickets (`/api/tickets`)

### 4.1 Unirse a la Cola

| Campo | Descripción |
|-------|-------------|
| **Método** | `POST` |
| **URL** | `/api/tickets/join` |
| **Auth** | No requerida |

**Request Body:**
```json
{
  "userId": "string (requerido)",
  "eventId": "string (requerido)"
}
```

**Response (201 Created):**
```json
{
  "ticketId": "string",
  "userId": "string",
  "eventId": "string",
  "status": "string",
  "quantity": "int",
  "queuePosition": "long",
  "qtoken": "string (token del QR)",
  "pathPdf": "string (ruta del PDF, null si no completado)",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

### 4.2 Intentar Comprar

| Campo | Descripción |
|-------|-------------|
| **Método** | `POST` |
| **URL** | `/api/tickets/{ticketId}/purchase` |
| **Auth** | No requerida |

**Path Parameters:**
- `ticketId` (String): ID del ticket

**Response (200 OK):**
```json
{
  "ticketId": "string",
  "userId": "string",
  "eventId": "string",
  "status": "string (COMPLETED o REJECTED)",
  "quantity": "int",
  "queuePosition": "long",
  "qtoken": "string (token del QR)",
  "pathPdf": "string (ruta del PDF si COMPLETED, null si REJECTED)",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

### 4.3 Obtener Todos los Tickets

| Campo | Descripción |
|-------|-------------|
| **Método** | `GET` |
| **URL** | `/api/tickets` |
| **Auth** | No requerida |

**Response (200 OK):**
```json
[
  {
    "ticketId": "string",
    "userId": "string",
    "eventId": "string",
    "status": "string",
    "quantity": "int",
    "queuePosition": "long",
    "qtoken": "string",
  "pathPdf": "string",
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
]
```

---

### 4.4 Obtener Ticket por ID

| Campo | Descripción |
|-------|-------------|
| **Método** | `GET` |
| **URL** | `/api/tickets/{ticketId}` |
| **Auth** | No requerida |

**Path Parameters:**
- `ticketId` (String): ID del ticket

**Response (200 OK):**
```json
{
  "ticketId": "string",
  "userId": "string",
  "eventId": "string",
  "status": "string",
  "quantity": "int",
  "queuePosition": "long",
  "qtoken": "string",
  "pathPdf": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

### 4.5 Obtener Tickets por Evento

| Campo | Descripción |
|-------|-------------|
| **Método** | `GET` |
| **URL** | `/api/tickets/event/{eventId}` |
| **Auth** | No requerida |

**Path Parameters:**
- `eventId` (String): ID del evento

**Response (200 OK):**
```json
[
  {
    "ticketId": "string",
    "userId": "string",
    "eventId": "string",
    "status": "string",
    "quantity": "int",
    "queuePosition": "long",
    "qtoken": "string",
  "pathPdf": "string",
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
]
```

---

### 4.6 Obtener Tickets por Usuario

| Campo | Descripción |
|-------|-------------|
| **Método** | `GET` |
| **URL** | `/api/tickets/user/{userId}` |
| **Auth** | No requerida |

**Path Parameters:**
- `userId` (String): ID del usuario

**Response (200 OK):**
```json
[
  {
    "ticketId": "string",
    "userId": "string",
    "eventId": "string",
    "status": "string",
    "quantity": "int",
    "queuePosition": "long",
    "qtoken": "string",
  "pathPdf": "string",
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
]
```

---

### 4.7 Cancelar Ticket

| Campo | Descripción |
|-------|-------------|
| **Método** | `POST` |
| **URL** | `/api/tickets/{ticketId}/cancel` |
| **Auth** | No requerida |

**Path Parameters:**
- `ticketId` (String): ID del ticket

**Response (200 OK):**
```json
{
  "ticketId": "string",
  "userId": "string",
  "eventId": "string",
  "status": "CANCELLED",
  "quantity": "int",
  "queuePosition": "long",
  "createdAt": "long"
}
```

---

## 5. Lugares (`/api/venues`)

### 5.1 Crear Lugar

| Campo | Descripción |
|-------|-------------|
| **Método** | `POST` |
| **URL** | `/api/venues` |
| **Auth** | Requerida: `VENUE_CREATE` |

**Request Body:**
```json
{
  "name": "string (requerido)",
  "address": "string (requerido)",
  "capacity": "int (requerido, mínimo 1)",
  "layout": "string (opcional)"
}
```

**Response (201 Created):**
```json
{
  "venueId": "long",
  "name": "string",
  "address": "string",
  "capacity": "int",
  "layout": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

### 5.2 Obtener Todos los Lugares

| Campo | Descripción |
|-------|-------------|
| **Método** | `GET` |
| **URL** | `/api/venues` |
| **Auth** | Requerida: `VENUE_READ_ALL` |

**Response (200 OK):**
```json
[
  {
    "venueId": "long",
    "name": "string",
    "address": "string",
    "capacity": "int",
    "layout": "string",
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
]
```

---

### 5.3 Obtener Lugar por ID

| Campo | Descripción |
|-------|-------------|
| **Método** | `GET` |
| **URL** | `/api/venues/{venueId}` |
| **Auth** | Requerida: `VENUE_READ_ONE` |

**Path Parameters:**
- `venueId` (Long): ID del lugar

**Response (200 OK):**
```json
{
  "venueId": "long",
  "name": "string",
  "address": "string",
  "capacity": "int",
  "layout": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

### 5.4 Actualizar Lugar

| Campo | Descripción |
|-------|-------------|
| **Método** | `PUT` |
| **URL** | `/api/venues/{venueId}` |
| **Auth** | Requerida: `VENUE_UPDATE` |

**Path Parameters:**
- `venueId` (Long): ID del lugar

**Request Body:**
```json
{
  "name": "string (requerido)",
  "address": "string (requerido)",
  "capacity": "int (requerido, mínimo 1)",
  "layout": "string (opcional)"
}
```

**Response (200 OK):**
```json
{
  "venueId": "long",
  "name": "string",
  "address": "string",
  "capacity": "int",
  "layout": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

### 5.5 Eliminar Lugar

| Campo | Descripción |
|-------|-------------|
| **Método** | `DELETE` |
| **URL** | `/api/venues/{venueId}` |
| **Auth** | Requerida: `VENUE_DELETE` |

**Path Parameters:**
- `venueId` (Long): ID del lugar

**Response:** `204 No Content`

---

## Resumen de Endpoints

| # | Método | Endpoint | Descripción | Auth |
|---|--------|----------|-------------|------|
| 1 | POST | `/api/auth/register` | Registrar usuario | No |
| 2 | POST | `/api/auth/login` | Iniciar sesión | No |
| 3 | GET | `/api/users` | Listar usuarios | USER_READ_ALL |
| 4 | GET | `/api/users/{userId}` | Obtener usuario | USER_READ_ONE |
| 5 | PUT | `/api/users/{userId}` | Actualizar usuario | USER_UPDATE |
| 6 | DELETE | `/api/users/{userId}` | Eliminar usuario | USER_DELETE |
| 7 | POST | `/api/users/reset-password` | Restablecer contraseña | No |
| 8 | POST | `/api/events` | Crear evento | EVENT_CREATE |
| 9 | GET | `/api/events` | Listar eventos | EVENT_READ_ALL |
| 10 | GET | `/api/events/{eventId}` | Obtener evento | EVENT_READ_ONE |
| 11 | PUT | `/api/events/{eventId}` | Actualizar evento | EVENT_UPDATE |
| 12 | DELETE | `/api/events/{eventId}` | Eliminar evento | No |
| 13 | POST | `/api/events/{eventId}/reduce-availability` | Reducir disponibilidad | No |
| 14 | POST | `/api/tickets/join` | Unirse a la cola | No |
| 15 | POST | `/api/tickets/{ticketId}/purchase` | Intentar comprar | No |
| 16 | GET | `/api/tickets` | Listar tickets | No |
| 17 | GET | `/api/tickets/{ticketId}` | Obtener ticket | No |
| 18 | GET | `/api/tickets/event/{eventId}` | Tickets por evento | No |
| 19 | GET | `/api/tickets/user/{userId}` | Tickets por usuario | No |
| 20 | POST | `/api/tickets/{ticketId}/cancel` | Cancelar ticket | No |
| 21 | POST | `/api/venues` | Crear lugar | VENUE_CREATE |
| 22 | GET | `/api/venues` | Listar lugares | VENUE_READ_ALL |
| 23 | GET | `/api/venues/{venueId}` | Obtener lugar | VENUE_READ_ONE |
| 24 | PUT | `/api/venues/{venueId}` | Actualizar lugar | VENUE_UPDATE |
| 25 | DELETE | `/api/venues/{venueId}` | Eliminar lugar | VENUE_DELETE |

---

## Flujo Típico de Uso

```
1. POST /api/auth/register  → Registrar usuario nuevo
2. POST /api/auth/login     → Obtener token JWT
3. GET /api/events           → Listar eventos disponibles
4. POST /api/tickets/join    → Unirse a la cola de un evento
5. POST /api/tickets/{id}/purchase → Intentar comprar entrada
6. GET /api/tickets/user/{id} → Ver tickets del usuario
```

---

## Notas Importantes

1. **Autenticación:** Los endpoints que requieren auth deben incluir el header `Authorization: Bearer <token>`
2. **Eventos históricos:** No se pueden eliminar eventos que tengan tickets asociados
3. **Cola de tickets:** El sistema usa una cola FIFO para la compra de entradas
4. **Roles:** El usuario puede registrarse como ADMIN, CUSTOMER u ORGANIZER
5. **PDF de tickets:** Al completar una compra (`COMPLETED`), se genera automáticamente un PDF con código QR. El token del QR se guarda en `qtoken` y la ruta del PDF en `pathPdf`
6. **Email de tickets:** Si el email está habilitado (`app.email.enabled=true`), se envía un email con el PDF adjunto al usuario que compró el ticket
7. **Swagger UI:** Disponible en `/swagger-ui.html` para explorar la API interactivamente
8. **Campos de auditoría:** Todas las entidades incluyen `createdAt` y `updatedAt` automáticamente

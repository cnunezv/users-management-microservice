# Users Management — Spring Boot, Arquitectura Hexagonal y DDD

Aplicación de gestión de usuarios construida con Java 17 y Spring Boot. La API REST es el punto de entrada activo. El código de la antigua CLI se conserva como adaptador inactivo y no posee un contenedor de dependencias independiente.

Spring es el único *composition root*: `Main` inicia el contexto y las dependencias se resuelven mediante configuración y component scanning de Spring.

## Verificación

`GET /health` es publico y responde HTTP 200 con los metadatos generados desde
`pom.xml` durante el build:

```json
{"status":"UP","name":"users-management","version":"2.2.0"}
```

Indica que la API responde; no comprueba MySQL ni el proveedor de correo.

```bash
./mvnw clean test
./mvnw clean package
```

En Windows se puede utilizar `mvnw.cmd`.

## Despliegue en Render

El archivo `render.yaml` define el servicio web, el build con Docker y el despliegue
automático de cada commit que llegue a la rama `main`.

1. Crear un Blueprint en Render y seleccionar este repositorio.
2. Completar en el panel los secretos marcados como requeridos: `DB_HOST`,
   `DB_USERNAME`, `DB_PASSWORD`, `SMTP_USERNAME`, `SMTP_PASSWORD` y
   `SMTP_FROM_ADDRESS`.
3. Usar una instancia MySQL accesible desde Internet o desde la red privada de
   Render y ejecutar `src/main/resources/schema.sql` una vez para crear el esquema.
4. Desplegar el Blueprint. La API quedará disponible en el subdominio
   `onrender.com` asignado por Render y Swagger UI en `/swagger-ui.html`.

Las credenciales nunca deben guardarse en `application.properties` ni en
`render.yaml`. Para desarrollo local, deben proporcionarse como variables de entorno.

---

## Ejercicio 19: CRUDL de CarreraTaxi

Sobre la aplicación base se implementó el CRUDL de la entidad **CarreraTaxi**
con las mismas capas, patrones y convenciones del CRUD de usuarios.

**Autor:** Carlos Andrés Núñez Vargas — Ingeniería de Software, Universidad de
Cartagena (Desarrollo de Software Web, Unidad 3).

### Atributos y reglas de negocio

| Atributo | Value object | Regla |
|---|---|---|
| `id` | `CarreraTaxiId` | UUID generado por el servidor; no puede estar vacío |
| `cliente` | `CarreraTaxiCliente` | Entre 3 y 100 caracteres |
| `taxi` | `CarreraTaxiTaxi` | Placa con formato `ABC123`; se normaliza a mayúsculas sin guion |
| `kilometros` | `CarreraTaxiKilometros` | Mayor que 0 y máximo 1000, dos decimales |
| `barrioInicio`, `barrioLlegada` | `CarreraTaxiBarrio` | Entre 3 y 100 caracteres |
| `cantidadPasajeros` | `CarreraTaxiCantidadPasajeros` | Entre 1 y 4 |
| `taxista` | `CarreraTaxiTaxista` | Entre 3 y 100 caracteres |
| `precio` | `CarreraTaxiPrecio` | Mayor que 0 (COP), dos decimales |
| `duracionMinutos` | `CarreraTaxiDuracionMinutos` | Entero mayor que 0 |

`CarreraTaxiModel` es inmutable y calcula el **precio por kilómetro** y la
**velocidad promedio**, que la API devuelve en cada respuesta.

### Recorrido por capas

| Capa | Elementos |
|---|---|
| Dominio | `CarreraTaxiModel`, 9 value objects, 10 excepciones, 3 eventos |
| Aplicación — puertos de salida | `Save`, `Update`, `GetById`, `GetAll` y `Delete` `CarreraTaxiPort` |
| Aplicación — casos de uso | Comandos y queries (CQRS), puertos de entrada, un servicio por caso de uso y `CarreraTaxiApplicationMapper` |
| Infraestructura — persistencia | `CarreraTaxiRepositoryMySQL` (JDBC), DTO, entity y mapper |
| Infraestructura — REST | `CarreraTaxiRestController`, requests, response, mapper, Swagger (`CarreraTaxiRestControllerDocs`) y errores en `GlobalExceptionHandler` |
| Configuración | Reglas de seguridad, pool configurable y `DatabaseMigrationRunner` |

### Endpoints

Todos requieren el JWT de `POST /api/auth/login` en el encabezado
`Authorization: Bearer <token>`. Swagger UI: `/swagger-ui.html`.

| Método | Ruta | Operación | Roles |
|---|---|---|---|
| `POST` | `/api/carreras-taxi` | **C** — registrar | ADMIN, MEMBER |
| `GET` | `/api/carreras-taxi/{id}` | **R** — consultar | todos |
| `PUT` | `/api/carreras-taxi/{id}` | **U** — actualizar | ADMIN, MEMBER |
| `DELETE` | `/api/carreras-taxi/{id}` | **D** — eliminar | ADMIN |
| `GET` | `/api/carreras-taxi` | **L** — listar | todos |

Errores: 400 (datos inválidos o regla de negocio), 401 (sin token), 403 (rol sin
permiso) y 404 (carrera inexistente).

### Usuarios de prueba

| Correo | Contraseña | Rol | Estado |
|---|---|---|---|
| `admin@example.com` | `Admin1234!` | ADMIN | Activo |
| `member@example.com` | `Member1234!` | MEMBER | Activo |
| `reviewer@example.com` | `Reviewer1234!` | REVIEWER | Activo |
| `pending@example.com` | `Pending1234!` | MEMBER | Pendiente (el login lo rechaza) |

### Base de datos

`src/main/resources/schema.sql` crea las tablas `users` y `carreras_taxi`, el
administrador, los usuarios de prueba y tres carreras de ejemplo. Es idempotente
(`CREATE TABLE IF NOT EXISTS`, `INSERT IGNORE`).

Con `DB_MIGRATE_ON_STARTUP=true` la aplicación lo aplica al arrancar sobre la
base configurada, omitiendo `CREATE DATABASE` y `USE`, así que sirve para un MySQL
administrado en la nube. Si no se definen las variables `DB_*`, se usan las
`MYSQL_ADDON_*` que exporta Clever Cloud.

### Ejecutar con Docker

```bash
docker compose up --build
```

API en <http://localhost:8080> y Swagger en <http://localhost:8080/swagger-ui.html>.

### Pruebas

- **Unitarias:** `./mvnw clean verify`.
- **Prueba de humo:** `BASE_URL=http://localhost:8080 bash tests/smoke.sh` (requiere `curl` y `jq`).
- **Postman:** importar `postman/CarreraTaxi.postman_collection.json` y ejecutar
  primero la carpeta *Auth*.

### Integración y despliegue continuo

`.github/workflows/ci.yml` compila y ejecuta las pruebas unitarias; luego arranca
la API sobre un MySQL vacío con la migración automática y corre la prueba de humo
y la colección de Postman con Newman. En cada push a `main` llama al deploy hook
de Render si existe el secreto `RENDER_DEPLOY_HOOK_URL`.

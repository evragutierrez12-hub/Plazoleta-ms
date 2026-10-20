# plazoleta-ms

Microservicio de plazoleta (restaurantes, platos y pedidos) del reto Plaza de comidas.
Java 21, Spring Boot, arquitectura hexagonal, H2.

## Cómo correrlo

En NetBeans: clic derecho en el proyecto > Test, y luego Run (o `spring-boot:run`).
En consola: `.\mvnw.cmd clean test` y `.\mvnw.cmd spring-boot:run`.

- API: http://localhost:8082
- Consola H2: http://localhost:8082/h2-console (JDBC URL `jdbc:h2:file:./data/plazoletadb`, usuario `sa`, clave vacía)
- Necesita usuarios-ms corriendo en http://localhost:8081 (propiedad `usuarios.url`)

## Implementado

- HU2 Crear Restaurante: `POST /api/v1/restaurants`
- HU9 Listar restaurantes: `GET /api/v1/restaurants?page=0&size=10` (solo CLIENT; orden alfabético; devuelve nombre y logo)
- HU3 Crear Plato: `POST /api/v1/dishes` (el plato queda activo; el restaurante debe existir)
- HU4 Modificar Plato: `PUT /api/v1/dishes/{id}` (solo cambia precio y descripción; 404 si no existe)

Ejemplo de body (el `ownerId` debe ser el id de un usuario con rol OWNER creado en usuarios-ms):

```json
{
  "name": "Sabor Costeño", "nit": "900123456", "address": "Calle 1 # 2-3",
  "phone": "+573005698325", "logoUrl": "http://logo.com/l.png", "ownerId": 1
}
```

Ejemplo de body para crear un plato (`restaurantId` es el id de un restaurante ya creado):

```json
{
  "name": "Bandeja paisa", "price": 25000, "description": "Plato típico",
  "imageUrl": "http://img.com/p.png", "category": "Almuerzos", "restaurantId": 1
}
```

Ejemplo de body para modificar un plato (solo precio y descripción):

```json
{ "price": 30000, "description": "Nueva descripción" }
```

- HU6 (parte plazoleta): `POST /api/v1/restaurants/{restaurantId}/employees` asocia un empleado con un restaurante. Solo OWNER y dueño del restaurante. Lo llama usuarios-ms al crear un empleado.

- HU7 Habilitar/Deshabilitar Plato: `PATCH /api/v1/dishes/{id}/status` con `{"active": true}` o `{"active": false}` (solo OWNER dueño del restaurante del plato)

## Seguridad (HU5)

Todas las peticiones necesitan `Authorization: Bearer <token>` con el token que entrega
`POST http://localhost:8081/api/v1/auth/login` (usuarios-ms).

- `POST /api/v1/restaurants`: solo rol ADMIN.
- `POST /api/v1/dishes` y `PUT /api/v1/dishes/{id}`: solo rol OWNER y además debe ser el propietario del restaurante (403 si no).
- `jwt.secret` debe ser igual al de usuarios-ms.

## Pendiente

HU10 a HU16.

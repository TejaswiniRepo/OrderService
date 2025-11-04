# Online Food Delivery - Order Service

Java 21 / Spring Boot (Gradle) Order Service using MongoDB.

## What is included
- A single Spring Boot microservice `order-service` under package `com.example.order`
- REST API: `/v1/orders` (create, get, list)
- Idempotency support via `Idempotency-Key` header (stored in `idempotency_keys` collection)
- Embedded mock endpoints under `/mocks/*` to simulate external services (restaurant/menu, payment, customer, delivery) for demo/local runs
- MongoDB configuration in `application.yml`
- OpenAPI UI available at `/swagger-ui.html` when running
- Actuator endpoints (including `/actuator/health` and `/actuator/prometheus`)

## Requirements
- Java 21
- Gradle (or use your IDE to import the Gradle project)
- MongoDB running locally at `mongodb://localhost:27017/orderdb` (or change `spring.data.mongodb.uri` in `application.yml`)

## Build & Run
1. Start MongoDB (example):
   ```
   docker run -d --name mongodb -p 27017:27017 -v mongo-data:/data/db mongo:6.0
   ```

2. Build and run:
   ```
   ./gradlew bootRun
   ```
   or
   ```
   gradle bootRun
   ```

3. Demo endpoints:
   - Create order:
     ```
     curl -X POST http://localhost:8080/v1/orders \
       -H "Content-Type: application/json" \
       -H "Idempotency-Key: test-123" \
       -d '{"customerId":54,"restaurantId":36,"addressId":73,"items":[{"itemId":317,"quantity":3},{"itemId":316,"quantity":3}],"clientTotal":1666.26,"paymentMethod":"CARD"}'
   ```
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - Actuator health: http://localhost:8080/actuator/health
   - Prometheus metrics: http://localhost:8080/actuator/prometheus

## Notes
- The project includes internal mock endpoints under `/mocks` so you can demo full flow without separate services.
- This is the Order Service focused implementation for your assignment. You can extend it with real restaurant/payment services later.

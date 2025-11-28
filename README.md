# url-shortener
Production-ready URL Shortener with Redis caching, Rate Limiting &amp; Analytics (Spring Boot 3 + Java 21)
# URL Shortener 🚀

**Production-Ready bit.ly Clone — Built in 1 Day**

### Features
- 8-char unique short codes
- **Super fast redirect** with Redis caching
- Click analytics & top URLs
- **Rate limiting** (100 req/min per IP)
- Zero-config H2 database (PostgreSQL ready)

### Tech Stack
- Java 21
- Spring Boot 3.5.7
- Redis (Lettuce)
- Bucket4j (Rate Limiting)
- Spring Boot Actuator

### API Endpoints
- `POST /api/shorten` → Get short link
- `GET /{shortCode}` → Redirect
- `GET /api/analytics` → JSON stats

### Quick Start
```bash
./mvnw spring-boot:run

# Feature Flag System

## What Is This?

Imagine you're running a website and you want to show a new feature to only some users — maybe 10% of visitors, or just your internal team. You don't want to redeploy your entire app every time you want to turn something on or off. That's what a **feature flag** does.

A feature flag is like a light switch for your software. You can turn features on or off instantly, without touching any code. This project is a **feature flag management system** — it lets you create, manage, and distribute these switches to your applications.

---

## Why Would You Use This?

- **Safe releases** — Turn features on for a small group first, then roll out to everyone
- **Instant rollback** — If something breaks, flip the switch off immediately
- **A/B testing** — Show different versions to different users and see which works better
- **No redeploys** — Change behavior without pushing new code

---

## How It Works (Simple)

1. **Admin** creates a flag (e.g., "dark-mode") using the admin API
2. **Your app** asks the flag server: "Is dark-mode enabled?"
3. **Your app** shows or hides the feature based on the answer

---

## How It Works (Technical)

```
┌─────────────┐      ┌─────────────┐      ┌─────────────┐
│   Admin     │─────►│  Flag Server │─────►│  PostgreSQL │
│   API       │      │  (Spring Boot)│      │  Database   │
└─────────────┘      └──────┬──────┘      └─────────────┘
                            │
                            │ REST API
                            ▼
                      ┌─────────────┐
                      │   Your App  │
                      │   (SDK)     │
                      └─────────────┘
```

---

## Features

### Current

| Feature | Description |
|---|---|
| **Flag Management** | Create, read, update, delete (soft-delete) feature flags |
| **Toggle Switches** | Instantly enable/disable flags via API |
| **Metadata** | Track description, creation date, last modified date |
| **Validation** | Input validation with clear error messages |
| **Error Handling** | Structured error responses (400/404/409/500) |
| **PostgreSQL** | Production-ready database with Flyway migrations |
| **SDK** | Java SDK for easy integration into your apps |
| **Polling** | SDK polls server for flag updates (30s interval) |
| **Fallback** | SDK keeps last known values if server is down |

### API Endpoints

| Method | Endpoint | Auth | Purpose |
|---|---|---|---|
| GET | `/api/sdk/flags` | None | Get all flags (SDK) |
| GET | `/api/admin/flags` | Basic | Get all flags (admin) |
| POST | `/api/admin/flags` | Basic | Create a flag |
| PUT | `/api/admin/flags/{id}/toggle` | Basic | Toggle a flag |
| PATCH | `/api/admin/flags/{id}` | Basic | Update a flag |
| DELETE | `/api/admin/flags/{id}` | Basic | Delete a flag (soft-delete) |

---

## Tech Stack

| Component | Technology |
|---|---|
| Backend | Spring Boot 4.1.1 (Java 21) |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| SDK | Java 21 |
| Build Tool | Gradle |
| Auth | HTTP Basic (JWT coming soon) |

---

## Project Structure

```
feature-flag/
├── flag-server/          # Spring Boot server
│   ├── src/main/java/
│   │   └── com/zyop/featureFlag/
│   │       ├── controller/    # REST endpoints
│   │       ├── dto/           # Request/response objects
│   │       ├── exception/     # Error handling
│   │       └── config/        # Security config
│   └── src/main/resources/
│       ├── application.properties
│       └── db/migration/      # Flyway migrations
├── flag-sdk/             # Java SDK for client apps
│   └── src/main/java/
│       └── com/featureflag/sdk/
│           ├── FeatureFlagClient.java
│           └── FlagPoller.java
└── test-client/          # Demo app showing SDK usage
```

---

## Getting Started

### Prerequisites

- Java 21
- Docker (for PostgreSQL)
- Gradle

### Setup

1. **Start PostgreSQL:**
   ```bash
   docker run --name feature-flag-db -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=featureflag -p 5432:5432 -d postgres:16
   ```

2. **Run the server:**
   ```bash
   ./gradlew :flag-server:bootRun
   ```

3. **Create a flag:**
   ```bash
   curl -X POST http://localhost:8080/api/admin/flags \
     -H "Content-Type: application/json" \
     -H "Authorization: Basic admin:changeme" \
     -d '{"key": "dark-mode", "name": "Dark Mode", "enabled": true}'
   ```

4. **Check flags:**
   ```bash
   curl http://localhost:8080/api/sdk/flags
   ```

---

## Future Roadmap

### Phase 2B (In Progress) — Database & Auth
- [x] PostgreSQL integration
- [x] Flyway migrations
- [ ] JWT authentication (replacing HTTP Basic)
- [ ] Refresh tokens

### Phase 2C — Core Feature-Flag Features
- [ ] Percentage rollout (e.g., 10% of users)
- [ ] User targeting (e.g., only user123)
- [ ] Server-side evaluation endpoint
- [ ] Rule priority and ordering
- [ ] Audit log for flag changes

### Phase 2D — Production Readiness
- [ ] Docker support for the server
- [ ] CI/CD pipeline (GitHub Actions)
- [ ] API documentation (Swagger/OpenAPI)
- [ ] Health checks and metrics
- [ ] Structured logging

### Phase 2E — Advanced Features
- [ ] Real-time updates (SSE/WebSocket)
- [ ] Multi-tenancy
- [ ] Flag groups/namespaces
- [ ] A/B testing framework
- [ ] Webhook notifications
- [ ] Analytics dashboard

---

## Contributing

This is a personal project for learning and portfolio purposes. Feel free to fork and experiment!

---

## License

MIT

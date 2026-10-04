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
| **JWT Authentication** | Secure token-based auth for admin endpoints |
| **SDK** | Java SDK for easy integration into your apps |
| **Polling** | SDK polls server for flag updates (30s interval) |
| **Fallback** | SDK keeps last known values if server is down |

### API Endpoints

| Method | Endpoint | Auth | Purpose |
|---|---|---|---|
| POST | `/api/auth/login` | None | Get JWT token |
| GET | `/api/sdk/flags` | None | Get all flags (SDK) |
| GET | `/api/admin/flags` | JWT | Get all flags (admin) |
| POST | `/api/admin/flags` | JWT | Create a flag |
| PUT | `/api/admin/flags/{id}/toggle` | JWT | Toggle a flag |
| PATCH | `/api/admin/flags/{id}` | JWT | Update a flag |
| DELETE | `/api/admin/flags/{id}` | JWT | Delete a flag (soft-delete) |

---

## Tech Stack

| Component | Technology |
|---|---|
| Backend | Spring Boot 4.1.1 (Java 21) |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| Auth | JWT (JSON Web Tokens) |
| SDK | Java 21 |
| Build Tool | Gradle |

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
│   │       ├── security/      # JWT utilities
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

3. **Get a JWT token:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "admin", "password": "changeme"}'
   ```

4. **Create a flag:**
   ```bash
   curl -X POST http://localhost:8080/api/admin/flags \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     -d '{"key": "dark-mode", "name": "Dark Mode", "enabled": true}'
   ```

5. **Check flags:**
   ```bash
   curl http://localhost:8080/api/sdk/flags
   ```

---

## Future Roadmap

- **Percentage rollout** — Roll out features to a percentage of users (e.g., 10% of traffic)
- **User targeting** — Target specific users or groups with flags
- **Server-side evaluation** — Evaluate flags on the server with user context
- **Audit logging** — Track who changed what and when
- **Real-time updates** — Push flag changes instantly via SSE/WebSocket
- **Multi-tenancy** — Support multiple organizations with isolated flags
- **A/B testing** — Built-in A/B testing with metrics
- **Analytics dashboard** — Visualize flag usage and rollout metrics

---

## Contributing

This is a personal project for learning and portfolio purposes. Feel free to fork and experiment!

---

## License

MIT

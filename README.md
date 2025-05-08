# JobFlow

JobFlow is a comprehensive task management system designed to streamline workflow processes, track user performance, and manage task assignments efficiently.

## Project Overview

JobFlow is a full-stack application with:
- Spring Boot backend (Java 17)
- Angular frontend (Angular 19)
- PostgreSQL database
- Docker support for containerized deployment

The application allows for task creation, assignment, tracking, and reporting with different user roles (Admin, Manager, User) and category-based organization.

## Features

- **User Management**: Registration, authentication, and role-based access control
- **Task Management**: Create, assign, track, and report on tasks
- **Category System**: Organize users and tasks by categories
- **Performance Tracking**: Monitor user performance and task completion
- **Responsive UI**: Modern Angular-based interface with Bootstrap styling
- **Security**: JWT-based authentication and authorization
- **Docker Support**: Easy deployment with Docker Compose

## Tech Stack

### Backend
- Java 17
- Spring Boot 3.2.3
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL
- Lombok
- SpringDoc OpenAPI (Swagger)

### Frontend
- Angular 19
- TypeScript
- Bootstrap
- ApexCharts for data visualization
- AOS for animations
- HTML2Canvas & jsPDF for report generation

### DevOps
- Docker & Docker Compose
- Nginx (for frontend serving)

## Project Structure

```
jobflow/
├── back/                   # Backend Spring Boot application
│   ├── src/                # Source code
│   │   ├── main/
│   │   │   ├── java/com/app/  # Java code
│   │   │   └── resources/     # Application resources
│   │   └── test/          # Test code
│   ├── Dockerfile         # Backend Docker configuration
│   └── pom.xml            # Maven dependencies
├── front/                  # Frontend Angular application
│   ├── src/                # Source code
│   │   ├── app/            # Angular components
│   │   └── assets/         # Static assets
│   ├── public/             # Public assets
│   ├── Dockerfile         # Frontend Docker configuration
│   └── package.json       # NPM dependencies
├── compose.yml            # Docker Compose configuration
└── README.md              # Project documentation
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Node.js 18 or higher
- PostgreSQL 
- Docker and Docker Compose (optional)

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/jobflow.git
   cd jobflow
   ```

2. **Backend Setup**
   ```bash
   cd back
   # Set the DB_PASSWORD environment variable
   export DB_PASSWORD=your_password
   # Build the project
   ./mvnw clean install
   # Run the application
   ./mvnw spring-boot:run
   ```

3. **Frontend Setup**
   ```bash
   cd front
   # Install dependencies
   npm install
   # Run the development server
   npm start
   ```

4. **Access the application**
   - Backend API: http://localhost:8080
   - Frontend: http://localhost:4200

### Docker Deployment

```bash
# Build and start all services
docker-compose up -d

# Access the application
# Frontend: http://localhost:4200
# Backend API: http://localhost:8080
```

## Database Schema

The application uses a PostgreSQL database with the following main entities:
- AppUser: User information and authentication
- Task: Task details and assignments
- TaskReport: Reports submitted for tasks
- Category: Organizational categories for users and tasks
- Roles: Expanded role management
- Credentials: Enhanced security and authentication
- Grades: User performance tracking
- Limits: Constraints for different user categories or roles

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

# Rescue Wheels backend

## Introduction

This is a backend project for the **Rescue Wheels** web application and **ResWee** mobile application. This backend has
been developed already using Node.js, and I'm rewriting the backend project in Java to apply the newly acquired skills
in **Java Spring Boot**.

## Features

- User registration and authentication using **JWT**.
- Managing user vehicles.
- Create, get, and update emergency requests.
- Fetch nearby requests.
- User and technician rating system.

## Missing features from the original project (Not implemented yet)

### Emergency request features

- Real-time location updates.
- Push notifications.

### Repair center features

- Register repair centers.
- Appointment booking.
- Push notifications.
- Confirmation emails.

## Before getting started

Make sure that you have **MongoDB** installed locally if you are going to use the default configuration.

## Getting started

1. After cloning the repository, open a terminal then navigate to the project's root directory.

   ```bash
   cd <project_root_dir>
   ```

2. Build and run the app.

   ```bash
   ./mvnw spring-boot:run
   ```

## API endpoints

- The base URL will be `http://localhost:3000` if using default configuration. 
- All routes require a bearer token in the authorization header except `/auth/**` endpoints.

### User authentication

| Method | Endpoint       | Access roles | Body                                                                                                          | Description                                                    |
|--------|----------------|--------------|---------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------|
| `POST` | `/auth/signUp` | All          | `{"firstName": "string","lastName": "string","email": "string","password": "string","phoneNumber": "string"}` | Register user and returns user metadata.                       |
| `POST` | `/auth/login`  | All          | `{"email": "string","password": "string"}`                                                                    | Authenticates user and returns user metadata and access token. |

### User management

| Method   | Endpoint            | Access roles | Body                                                                                                          | Description                                           |
|----------|---------------------|--------------|---------------------------------------------------------------------------------------------------------------|-------------------------------------------------------|
| `POST`   | `/users/technician` | Admin        | `{"firstName": "string","lastName": "string","email": "string","password": "string","phoneNumber": "string"}` | Register technician and returns technician meta data. |
| `DELETE` | `/users/{id}`       | All          |                                                                                                               | Deletes user by id.                                   |

### Vehicle management

| Method   | Endpoint              | Access roles | Body                                                                                                                       | Description                              |
|----------|-----------------------|--------------|----------------------------------------------------------------------------------------------------------------------------|------------------------------------------|
| `GET`    | `/vehicles`           | Admin        |                                                                                                                            | Get all vehicles.                        |
| `GET`    | `/vehicles/{id}`      | All          |                                                                                                                            | Get vehicle by id.                       |
| `GET`    | `/vehicles/user/{id}` | All          |                                                                                                                            | Get all vehicles for a specific user id. |
| `POST`   | `/vehicles`           | All          | `{"make": "string","model": "string","type": "string","energySource": "string","licensePlate": "string","year": "number"}` | Return added vehicle metadata.           |
| `DELETE` | `/vehicles/{id}`      | All          |                                                                                                                            | Deletes vehicle by id.                   |

### Emergency request management

| Method | Endpoint                                                                | Access roles      | Body                                                                                                                          | Description                                                     |
|--------|-------------------------------------------------------------------------|-------------------|-------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------|
| `POST` | `/emergency-requests`                                                   | Admin, User       | `{"requestedBy": "string","vehicleId": "string","coordinate": {"latitude": "number","longitude": "number"},"type": "string"}` | Add emergency request and return request metadata.              |
| `GET`  | `/emergency-requests`                                                   | Admin             |                                                                                                                               | Get all requests.                                               |
| `GET`  | `/emergency-requests/{id}`                                              | All               |                                                                                                                               | Get a request by id.                                            |
| `GET`  | `/emergency-requests/estimated-price?type={type}&vehicleId={vehicleId}` | Admin, User       |                                                                                                                               | Get the estimated price for an emergency request service.       |
| `GET`  | `/emergency-requests/nearby?lat={lat}&long={long}`                      | Admin, Technician |                                                                                                                               | Get nearby requests for the specified coordinate.               |
| `GET`  | `/emergency-requests?userId={userId}`                                   | Admin, User       |                                                                                                                               | Get all requests for a specific user id.                        |
| `PUT`  | `/emergency-requests/{id}/accept`                                       | Admin, Technician |                                                                                                                               | Change request state to "RESPONDING" for the specified id.      |
| `PUT`  | `/emergency-requests/{id}/cancel-responder`                             | Admin, Technician |                                                                                                                               | Change request state back to "RESPONDING" for the specified id. |
| `PUT`  | `/emergency-requests/{id}/in-progress`                                  | Admin, Technician |                                                                                                                               | Change request state to "IN_PROGRESS" for the specified id.     |
| `PUT`  | `/emergency-requests/{id}/complete`                                     | Admin, Technician |                                                                                                                               | Change request state to "DONE" for the specified id.            |
| `PUT`  | `/emergency-requests/{id}/cancel`                                       | Admin, User       |                                                                                                                               | Change request state to "CANCELLED" for the specified id.       |
| `PUT`  | `/emergency-requests/{id}`                                              | All               |                                                                                                                               | Handle user and technician rating for the request id.           |

## Note
MongoDB URI, JWT secret key and JWT expiration are included in the `application.properties` file for demonstration purposes only.

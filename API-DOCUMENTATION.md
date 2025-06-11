# 📡 CleanMe API Documentation

> **Complete REST API reference for the CleanMe cleaning service platform**

## 🎯 Table of Contents

1. [🚀 Getting Started](#-getting-started)
2. [🔐 Authentication](#-authentication)
3. [👤 User Management](#-user-management)
4. [🧹 Cleaner Management](#-cleaner-management)
5. [📅 Reservation System](#-reservation-system)
6. [⭐ Review System](#-review-system)
7. [❤️ Favorites Management](#️-favorites-management)
8. [🔔 Notifications](#-notifications)
9. [📊 Error Handling](#-error-handling)
10. [🔍 Testing & Examples](#-testing--examples)

---

## 🚀 Getting Started

### Base URL

```
Production:  https://cleanme-backend.herokuapp.com
Development: http://localhost:8080
```

### API Versioning

```
All endpoints are prefixed with: /api
```

### Content Type

```
Content-Type: application/json
Accept: application/json
```

### Authentication Header

```
Authorization: Bearer <jwt-token>
```

---

## 🔐 Authentication

### Register User

**POST** `/api/auth/register`

Register a new user (client or cleaner).

**Request Body:**

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePassword123!",
  "userType": "CLIENT"
}
```

**Response (201 Created):**

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "user": {
    "id": "uuid-string",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "userType": "CLIENT"
  }
}
```

### Login User

**POST** `/api/auth/login`

Authenticate existing user.

**Request Body:**

```json
{
  "email": "john.doe@example.com",
  "password": "SecurePassword123!"
}
```

**Response (200 OK):**

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "user": {
    "id": "uuid-string",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "userType": "CLIENT"
  }
}
```

---

## 👤 User Management

### Get Current User

**GET** `/api/users/me`

Get current authenticated user information.

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Response (200 OK):**

```json
{
  "id": "uuid-string",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "userType": "CLIENT",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### Update User Profile

**PUT** `/api/users/me`

Update current user's profile information.

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Request Body:**

```json
{
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@example.com"
}
```

**Response (200 OK):**

```json
{
  "id": "uuid-string",
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@example.com",
  "userType": "CLIENT",
  "updatedAt": "2024-01-15T11:30:00Z"
}
```

---

## 🧹 Cleaner Management

### Get All Cleaners

**GET** `/api/cleaners`

Get paginated list of all cleaners.

**Query Parameters:**

- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 10)
- `zone` (optional): Filter by zone
- `minRate` (optional): Minimum hourly rate
- `maxRate` (optional): Maximum hourly rate

**Example:**

```
GET /api/cleaners?page=0&size=5&zone=Centar&minRate=20&maxRate=50
```

**Response (200 OK):**

```json
{
  "content": [
    {
      "id": "cleaner-uuid",
      "fullName": "Ana Marić",
      "bio": "Professional house cleaner with 5+ years experience",
      "rating": 4.8,
      "reviewCount": 24,
      "location": "Centar",
      "shortBio": "Professional house cleaner...",
      "services": ["House Cleaning", "Deep Cleaning", "Office Cleaning"],
      "price": 25.0,
      "currency": "BAM"
    }
  ],
  "totalElements": 50,
  "totalPages": 10,
  "currentPage": 0,
  "pageSize": 5
}
```

### Get Cleaner Details

**GET** `/api/cleaners/{id}`

Get detailed information about a specific cleaner.

**Response (200 OK):**

```json
{
  "id": "cleaner-uuid",
  "fullName": "Ana Marić",
  "bio": "Professional house cleaner with 5+ years of experience. I specialize in deep cleaning and organizing homes and offices.",
  "servicesOffered": [
    "House Cleaning",
    "Deep Cleaning",
    "Office Cleaning",
    "Move-in/Move-out"
  ],
  "hourlyRate": 25.0,
  "minHours": 2,
  "zones": ["Centar", "Novi Grad", "Stari Grad"],
  "rating": 4.8,
  "reviewCount": 24,
  "createdAt": "2024-01-01T00:00:00Z"
}
```

### Create Cleaner Profile

**POST** `/api/cleaners`

Create cleaner profile (only for users with CLEANER type).

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Request Body:**

```json
{
  "bio": "Professional cleaner with extensive experience",
  "servicesOffered": ["House Cleaning", "Office Cleaning"],
  "hourlyRate": 30.0,
  "minHours": 3,
  "zones": ["Centar", "Novi Grad"]
}
```

**Response (201 Created):**

```json
{
  "id": "cleaner-uuid",
  "fullName": "Ana Marić",
  "bio": "Professional cleaner with extensive experience",
  "servicesOffered": ["House Cleaning", "Office Cleaning"],
  "hourlyRate": 30.0,
  "minHours": 3,
  "zones": ["Centar", "Novi Grad"],
  "rating": 0.0,
  "reviewCount": 0
}
```

### Update Cleaner Profile

**PUT** `/api/cleaners/{id}`

Update cleaner profile information.

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Request Body:**

```json
{
  "bio": "Updated professional cleaner bio",
  "hourlyRate": 35.0,
  "servicesOffered": ["House Cleaning", "Office Cleaning", "Deep Cleaning"]
}
```

**Response (200 OK):**

```json
{
  "id": "cleaner-uuid",
  "fullName": "Ana Marić",
  "bio": "Updated professional cleaner bio",
  "servicesOffered": ["House Cleaning", "Office Cleaning", "Deep Cleaning"],
  "hourlyRate": 35.0,
  "minHours": 3,
  "zones": ["Centar", "Novi Grad"]
}
```

---

## 📅 Reservation System

### Get All Reservations

**GET** `/api/reservations`

Get reservations for current user.

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Query Parameters:**

- `status` (optional): Filter by status (PENDING, CONFIRMED, COMPLETED, CANCELLED)
- `page` (optional): Page number
- `size` (optional): Page size

**Response (200 OK):**

```json
{
  "content": [
    {
      "id": "reservation-uuid",
      "cleanerName": "Ana Marić",
      "cleanerId": "cleaner-uuid",
      "date": "2024-01-20",
      "time": "10:00:00",
      "location": "Zmaja od Bosne 1, Sarajevo",
      "status": "CONFIRMED",
      "comment": "Deep cleaning needed for kitchen and bathrooms",
      "createdAt": "2024-01-15T09:00:00Z"
    }
  ],
  "totalElements": 5,
  "totalPages": 1,
  "currentPage": 0
}
```

### Create Reservation

**POST** `/api/reservations`

Create a new cleaning reservation.

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Request Body:**

```json
{
  "cleanerId": "cleaner-uuid",
  "date": "2024-01-25",
  "time": "14:00:00",
  "location": "Ferhadija 15, Sarajevo",
  "comment": "Regular house cleaning, pet-friendly products preferred"
}
```

**Response (201 Created):**

```json
{
  "id": "new-reservation-uuid",
  "cleanerName": "Ana Marić",
  "cleanerId": "cleaner-uuid",
  "date": "2024-01-25",
  "time": "14:00:00",
  "location": "Ferhadija 15, Sarajevo",
  "status": "PENDING",
  "comment": "Regular house cleaning, pet-friendly products preferred",
  "createdAt": "2024-01-15T12:00:00Z"
}
```

### Update Reservation Status

**PUT** `/api/reservations/{id}/status`

Update reservation status (for cleaners).

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Request Body:**

```json
{
  "status": "CONFIRMED"
}
```

**Response (200 OK):**

```json
{
  "id": "reservation-uuid",
  "cleanerName": "Ana Marić",
  "cleanerId": "cleaner-uuid",
  "date": "2024-01-25",
  "time": "14:00:00",
  "location": "Ferhadija 15, Sarajevo",
  "status": "CONFIRMED",
  "comment": "Regular house cleaning, pet-friendly products preferred",
  "updatedAt": "2024-01-15T13:00:00Z"
}
```

### Cancel Reservation

**DELETE** `/api/reservations/{id}`

Cancel a reservation.

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Response (204 No Content)**

---

## ⭐ Review System

### Get Cleaner Reviews

**GET** `/api/cleaners/{cleanerId}/reviews`

Get all reviews for a specific cleaner.

**Query Parameters:**

- `page` (optional): Page number
- `size` (optional): Page size

**Response (200 OK):**

```json
{
  "content": [
    {
      "id": "review-uuid",
      "userName": "Marko P.",
      "rating": 5,
      "comment": "Excellent service! Very thorough and professional.",
      "createdAt": "2024-01-10T15:30:00Z"
    },
    {
      "id": "review-uuid-2",
      "userName": "Amela S.",
      "rating": 4,
      "comment": "Good cleaning service, arrived on time.",
      "createdAt": "2024-01-08T11:20:00Z"
    }
  ],
  "averageRating": 4.8,
  "totalReviews": 24
}
```

### Create Review

**POST** `/api/reviews`

Create a review for a completed reservation.

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Request Body:**

```json
{
  "reservationId": "reservation-uuid",
  "rating": 5,
  "comment": "Outstanding service! Highly recommend."
}
```

**Response (201 Created):**

```json
{
  "id": "new-review-uuid",
  "reservationId": "reservation-uuid",
  "cleanerId": "cleaner-uuid",
  "userName": "John D.",
  "rating": 5,
  "comment": "Outstanding service! Highly recommend.",
  "createdAt": "2024-01-15T16:00:00Z"
}
```

### Update Review

**PUT** `/api/reviews/{id}`

Update an existing review.

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Request Body:**

```json
{
  "rating": 4,
  "comment": "Updated review: Very good service overall."
}
```

**Response (200 OK):**

```json
{
  "id": "review-uuid",
  "reservationId": "reservation-uuid",
  "cleanerId": "cleaner-uuid",
  "userName": "John D.",
  "rating": 4,
  "comment": "Updated review: Very good service overall.",
  "updatedAt": "2024-01-15T17:00:00Z"
}
```

---

## ❤️ Favorites Management

### Get User Favorites

**GET** `/api/favourites`

Get user's favorite cleaners.

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Response (200 OK):**

```json
[
  {
    "id": "favourite-uuid",
    "cleaner": {
      "id": "cleaner-uuid",
      "fullName": "Ana Marić",
      "rating": 4.8,
      "location": "Centar",
      "services": ["House Cleaning", "Deep Cleaning"],
      "price": 25.0
    },
    "addedAt": "2024-01-10T10:00:00Z"
  }
]
```

### Add to Favorites

**POST** `/api/favourites`

Add a cleaner to favorites.

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Request Body:**

```json
{
  "cleanerId": "cleaner-uuid"
}
```

**Response (201 Created):**

```json
{
  "id": "new-favourite-uuid",
  "cleanerId": "cleaner-uuid",
  "addedAt": "2024-01-15T14:00:00Z"
}
```

### Remove from Favorites

**DELETE** `/api/favourites/{id}`

Remove a cleaner from favorites.

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Response (204 No Content)**

---

## 🔔 Notifications

### Get User Notifications

**GET** `/api/notifications`

Get notifications for current user.

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Query Parameters:**

- `unreadOnly` (optional): Show only unread notifications (default: false)

**Response (200 OK):**

```json
[
  {
    "id": "notification-uuid",
    "message": "Your reservation with Ana Marić has been confirmed for Jan 25, 2024 at 2:00 PM",
    "isRead": false,
    "createdAt": "2024-01-15T13:00:00Z"
  },
  {
    "id": "notification-uuid-2",
    "message": "Don't forget to leave a review for your completed cleaning service",
    "isRead": true,
    "createdAt": "2024-01-12T16:00:00Z"
  }
]
```

### Mark Notification as Read

**PUT** `/api/notifications/{id}/read`

Mark a specific notification as read.

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Response (200 OK):**

```json
{
  "id": "notification-uuid",
  "message": "Your reservation with Ana Marić has been confirmed for Jan 25, 2024 at 2:00 PM",
  "isRead": true,
  "readAt": "2024-01-15T18:00:00Z"
}
```

### Mark All Notifications as Read

**PUT** `/api/notifications/read-all`

Mark all notifications as read for current user.

**Headers:**

```
Authorization: Bearer <jwt-token>
```

**Response (200 OK):**

```json
{
  "message": "All notifications marked as read",
  "updatedCount": 5
}
```

---

## 📊 Error Handling

### Error Response Format

All API errors follow this standard format:

```json
{
  "error": "ERROR_CODE",
  "message": "Human readable error message",
  "timestamp": "2024-01-15T18:00:00Z",
  "path": "/api/cleaners/invalid-id"
}
```

### Common Error Codes

#### 400 Bad Request

```json
{
  "error": "VALIDATION_ERROR",
  "message": "Hourly rate must be between 5 and 200",
  "timestamp": "2024-01-15T18:00:00Z",
  "path": "/api/cleaners"
}
```

#### 401 Unauthorized

```json
{
  "error": "UNAUTHORIZED",
  "message": "JWT token is invalid or expired",
  "timestamp": "2024-01-15T18:00:00Z",
  "path": "/api/reservations"
}
```

#### 403 Forbidden

```json
{
  "error": "FORBIDDEN",
  "message": "You don't have permission to access this resource",
  "timestamp": "2024-01-15T18:00:00Z",
  "path": "/api/cleaners/123"
}
```

#### 404 Not Found

```json
{
  "error": "CLEANER_NOT_FOUND",
  "message": "Cleaner not found with id: invalid-uuid",
  "timestamp": "2024-01-15T18:00:00Z",
  "path": "/api/cleaners/invalid-uuid"
}
```

#### 409 Conflict

```json
{
  "error": "EMAIL_ALREADY_EXISTS",
  "message": "User with this email already exists",
  "timestamp": "2024-01-15T18:00:00Z",
  "path": "/api/auth/register"
}
```

#### 422 Unprocessable Entity

```json
{
  "error": "BUSINESS_RULE_VIOLATION",
  "message": "Cannot review a reservation that is not completed",
  "timestamp": "2024-01-15T18:00:00Z",
  "path": "/api/reviews"
}
```

### Validation Errors

Field validation errors include details about each invalid field:

```json
{
  "error": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "timestamp": "2024-01-15T18:00:00Z",
  "path": "/api/auth/register",
  "fieldErrors": [
    {
      "field": "email",
      "message": "Invalid email format"
    },
    {
      "field": "password",
      "message": "Password must be at least 8 characters long"
    }
  ]
}
```

---

## 🔍 Testing & Examples

### cURL Examples

#### Register New User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "SecurePass123!",
    "userType": "CLIENT"
  }'
```

#### Login User

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123!"
  }'
```

#### Get Cleaners (with authentication)

```bash
curl -X GET http://localhost:8080/api/cleaners \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

#### Create Reservation

```bash
curl -X POST http://localhost:8080/api/reservations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -d '{
    "cleanerId": "cleaner-uuid",
    "date": "2024-01-25",
    "time": "14:00:00",
    "location": "Ferhadija 15, Sarajevo",
    "comment": "Regular house cleaning needed"
  }'
```

### Postman Collection

Import the following collection structure for testing:

```json
{
  "info": {
    "name": "CleanMe API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080"
    },
    {
      "key": "token",
      "value": ""
    }
  ],
  "auth": {
    "type": "bearer",
    "bearer": [
      {
        "key": "token",
        "value": "{{token}}",
        "type": "string"
      }
    ]
  }
}
```

### Environment Variables for Testing

```bash
# Development
API_BASE_URL=http://localhost:8080
JWT_SECRET=your-test-secret-key

# Test Database
TEST_DB_URL=jdbc:postgresql://localhost:5432/cleanme_test
TEST_DB_USERNAME=postgres
TEST_DB_PASSWORD=password
```

---

## 📋 API Summary

| Endpoint                        | Method | Description               | Auth Required |
| ------------------------------- | ------ | ------------------------- | ------------- |
| `/api/auth/register`            | POST   | Register new user         | ❌            |
| `/api/auth/login`               | POST   | Login user                | ❌            |
| `/api/users/me`                 | GET    | Get current user          | ✅            |
| `/api/users/me`                 | PUT    | Update user profile       | ✅            |
| `/api/cleaners`                 | GET    | Get all cleaners          | ❌            |
| `/api/cleaners/{id}`            | GET    | Get cleaner details       | ❌            |
| `/api/cleaners`                 | POST   | Create cleaner profile    | ✅            |
| `/api/cleaners/{id}`            | PUT    | Update cleaner profile    | ✅            |
| `/api/cleaners/{id}/reviews`    | GET    | Get cleaner reviews       | ❌            |
| `/api/reservations`             | GET    | Get user reservations     | ✅            |
| `/api/reservations`             | POST   | Create reservation        | ✅            |
| `/api/reservations/{id}/status` | PUT    | Update reservation status | ✅            |
| `/api/reservations/{id}`        | DELETE | Cancel reservation        | ✅            |
| `/api/reviews`                  | POST   | Create review             | ✅            |
| `/api/reviews/{id}`             | PUT    | Update review             | ✅            |
| `/api/favourites`               | GET    | Get favorites             | ✅            |
| `/api/favourites`               | POST   | Add to favorites          | ✅            |
| `/api/favourites/{id}`          | DELETE | Remove from favorites     | ✅            |
| `/api/notifications`            | GET    | Get notifications         | ✅            |
| `/api/notifications/{id}/read`  | PUT    | Mark notification as read | ✅            |
| `/api/notifications/read-all`   | PUT    | Mark all as read          | ✅            |

---

**API Documentation Complete! 🎯**

_This comprehensive API reference covers all endpoints, request/response formats, error handling, and testing examples for the CleanMe backend service._

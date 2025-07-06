# Clientback API - Postman Collection

This directory contains the complete Postman collection and environments for testing the Clientback Spring Boot API.

## 📁 Files

- `Curoo_Clientback_API.postman_collection.json` - Main API collection
- `postman-environments/development.postman_environment.json` - Development environment
- `postman-environments/production.postman_environment.json` - Production environment template

## 🚀 Quick Start

### 1. Import Collection

1. Open Postman
2. Click **Import**
3. Select `Curoo_Clientback_API.postman_collection.json`
4. Import the development environment from `postman-environments/development.postman_environment.json`

### 2. Set Environment

1. Select the **Clientback - Development** environment
2. Ensure the `baseUrl` is set to `http://localhost:8080` (or your local URL)

### 3. Test the API

1. **Start with User Registration**: Create a new user account
2. **Login**: Get your JWT token (automatically saved to environment)
3. **Test Client Operations**: Create, read, update, delete clients

## 📋 Collection Structure

### 👤 User Management

- **Register User**: Create a new user account
- **Login User**: Authenticate and get JWT token
- **Update User**: Modify user profile information

### 🏢 Client Management

- **Get All Clients**: Retrieve all clients for authenticated user
- **Get Client by ID**: Find specific client by ID type and number
- **Create Client**: Add new client (linked to authenticated user)
- **Update Client**: Modify existing client data
- **Delete Client**: Remove client from system

### 🧪 Test Scenarios

- **Test: Get Non-Existent Client**: Verify 404 responses
- **Test: Create Client Without Auth**: Verify 401 responses
- **Test: Get All Clients Without Auth**: Verify authentication requirements

### 🔍 Health & Monitoring

- **Health Check**: Application health status
- **Application Info**: Build and version information
- **Metrics**: Application metrics (may require auth in production)
- **OpenAPI Documentation**: API specification (dev only)

## 🌍 Environments

### Development Environment

```json
{
  "baseUrl": "http://localhost:8080",
  "testUserEmail": "testuser@example.com",
  "testUserPassword": "password123"
}
```

### Production Environment

```json
{
  "baseUrl": "https://your-production-domain.com",
  "testUserEmail": "your-prod-test-email@yourdomain.com",
  "testUserPassword": "your-secure-prod-password"
}
```

## 🔄 Typical Workflow

### 1. Initial Setup

```
Register User → Login → Save JWT Token
```

### 2. Client Management

```
Create Client → Get All Clients → Update Client → Delete Client
```

### 3. Testing & Validation

```
Test Invalid Scenarios → Check Health Endpoints → Validate Security
```

## 🔑 Authentication

The collection automatically handles JWT authentication:

1. **Login Request**: Automatically saves JWT token to `{{jwtToken}}` variable
2. **Subsequent Requests**: Use `Authorization: Bearer {{jwtToken}}` header
3. **Token Persistence**: Token persists across requests in the same environment

## 📊 Expected Response Codes

### User Management

- **Register**: 201 (Created), 400 (Bad Request), 409 (Conflict)
- **Login**: 200 (OK), 401 (Unauthorized), 400 (Bad Request)
- **Update**: 200 (OK), 404 (Not Found), 401 (Unauthorized), 403 (Forbidden)

### Client Management

- **Get All**: 200 (OK), 401 (Unauthorized)
- **Get by ID**: 200 (OK), 404 (Not Found), 401 (Unauthorized)
- **Create**: 201 (Created), 400 (Bad Request), 401 (Unauthorized), 409 (Conflict)
- **Update**: 200 (OK), 404 (Not Found), 400 (Bad Request), 401 (Unauthorized)
- **Delete**: 204 (No Content), 404 (Not Found), 401 (Unauthorized)

### Health & Monitoring

- **Health**: 200 (OK)
- **Info**: 200 (OK)
- **Metrics**: 200 (OK), 401 (Unauthorized in prod)

## 🛠 Customization

### Adding New Requests

1. Right-click on a folder
2. Select "Add Request"
3. Configure method, URL, headers, and body
4. Use environment variables (e.g., `{{baseUrl}}`, `{{jwtToken}}`)

### Modifying Environments

1. Click the environment settings (eye icon)
2. Edit variables as needed
3. Save changes

### Adding Pre-request Scripts

```javascript
// Example: Set dynamic timestamp
pm.environment.set("timestamp", new Date().toISOString());
```

### Adding Test Scripts

```javascript
// Example: Validate response
pm.test("Status code is 200", function () {
  pm.response.to.have.status(200);
});

pm.test("Response contains required fields", function () {
  const jsonData = pm.response.json();
  pm.expect(jsonData).to.have.property("clientId");
  pm.expect(jsonData).to.have.property("name");
});
```

## 🔒 Security Notes

### Development

- Uses default test credentials
- JWT tokens expire (default: 1 hour)
- All endpoints accessible via HTTP

### Production

- **Change default credentials** in production environment
- Use **HTTPS only**
- Consider **API rate limiting**
- **Swagger/OpenAPI disabled** in production
- **Metrics may require authentication**

## 🐛 Troubleshooting

### Common Issues

1. **401 Unauthorized**

   - Ensure you've logged in and JWT token is set
   - Check if token has expired (re-login)
   - Verify `Authorization` header format: `Bearer {{jwtToken}}`

2. **Connection Refused**

   - Ensure application is running
   - Check `baseUrl` in environment
   - Verify port number (default: 8080)

3. **404 Not Found**

   - Check API endpoint URLs
   - Ensure application context path (`/api`) is included
   - Verify resource exists and belongs to authenticated user

4. **CORS Issues** (Browser/Web client)
   - Check `CORS_ALLOWED_ORIGINS` in application configuration
   - Ensure your client domain is in the allowed origins list

### Useful Console Commands

Check environment variables:

```javascript
console.log("Base URL:", pm.environment.get("baseUrl"));
console.log("JWT Token:", pm.environment.get("jwtToken"));
```

Debug response:

```javascript
console.log("Response Status:", pm.response.code);
console.log("Response Body:", pm.response.text());
```

## 📚 Additional Resources

- [Postman Documentation](https://learning.postman.com/docs/)
- [JWT Authentication Guide](https://jwt.io/introduction)
- [Spring Boot Actuator Endpoints](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [OpenAPI 3.0 Specification](https://swagger.io/specification/)

# ConfigSync - Backend (Spring Boot)

## Project Description
ConfigSync is a backend service that dynamically provides configuration data (in YAML or JSON format) to modify web pages based on predefined rules. The backend enforces authentication and authorization to ensure secure access to configuration data.

## Technologies Used
- **Java 21**
- **Spring Boot**
- **Spring Security (Basic Auth)**

## Authentication & Authorization
The application uses **In-Memory Authentication** with two predefined users:

| Username | Password  | Role  | Permissions |
|----------|----------|-------|-------------|
| `user`   | `password` | `USER`  | Can only perform `GET` requests |
| `admin`  | `admin` | `ADMIN` | Can perform all requests |

## Installation & Running the Application
### 1. Clone the repository:
   ```sh
   git clone https://github.com/kubilayckr/ConfigSync.git
   cd configsync-backend
  ```
### 2. Build and run the application using Maven:
  ```sh
  ./mvnw spring-boot:run
  ```
### 3. Access the API:
Once the application starts, it will be available at:
  ```
  http://localhost:8080/
  ```
## API Endpoints
The backend provides various endpoints to manage configurations.

For a complete list of available API endpoints, refer to the Postman collection located in:

```
postmancollections/ConfigSync.postman_collection.json
```

You can import this file into Postman to easily test the API.

### 1. **POST /api/v1/specific-configurations**  
Creates a new specific configuration. The request body is validated based on the predefined template and saved as a YAML file. The YAML file is created with the `SPECIFIC_CONFIG_` prefix and a randomly generated UUID.

#### Response:
- **201 Created**: When the configuration is successfully created.

### 2. **GET /api/v1/specific-configurations/{id}**  
Retrieves the specific configuration based on the provided `id`.

#### Response:
- **200 OK**: The configuration associated with the provided `id`.

### 3. **PUT /api/v1/specific-configurations/{id}**  
Replaces the specific configuration associated with the provided `id` with the specific configuration from the request body.

#### Response:
- **200 OK**: When the configuration is successfully replaced.

### 4. **DELETE /api/v1/specific-configurations/{id}**  
Deletes the specific configuration associated with the provided `id`.

#### Response:
- **200 OK**: When the configuration is successfully deleted.

### 5. **GET /api/v1/specific-configurations**  
Retrieves and merges all specific configuration files, returning the consolidated configuration data.

#### Response:
- **200 OK**: The merged configuration data.

### 6. **GET /api/v1/specific-configurations/specific**  
Retrieves and merges all specific configurations that match the provided query parameters (`page`, `url`, `host`).

#### Query Parameters:
- `page`: The page name to filter by.
- `url`: The URL to filter by.
- `host`: The host to filter by.

#### Response:
- **200 OK**: The merged configuration data for the matching query parameters.

### 7. **GET /api/v1/specific-configurations/specific-config-ids**  
Retrieves a list of all the specific configuration IDs that have been created.

#### Response:
- **200 OK**: A list of configuration IDs.

---

### 1. **POST /api/v1/configurations**  
Creates a new configuration. The request body is validated based on the predefined template and saved as a YAML file. The YAML file is created with the `CONFIG_` prefix and a randomly generated UUID.

#### Response:
- **201 Created**: When the configuration is successfully created.

### 2. **GET /api/v1/configurations/{id}**  
Retrieves the configuration based on the provided `id`.

#### Response:
- **200 OK**: The configuration associated with the provided `id`.

### 3. **PUT /api/v1/configurations/{id}**  
Replaces the configuration associated with the provided `id` with the configuration from the request body.

#### Response:
- **200 OK**: When the configuration is successfully replaced.

### 4. **DELETE /api/v1/configurations/{id}**  
Deletes the configuration associated with the provided `id`.

#### Response:
- **200 OK**: When the configuration is successfully deleted.

### 5. **GET /api/v1/configurations**  
Retrieves and merges all configuration files, returning the consolidated configuration data.

#### Response:
- **200 OK**: The merged configuration data.

### 6. **GET /api/v1/configurations/config-ids**  
Retrieves a list of all the configuration IDs that have been created.

#### Response:
- **200 OK**: A list of configuration IDs.

---

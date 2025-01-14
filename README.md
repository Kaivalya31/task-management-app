
# Task Management API Documentation

This API provides endpoints for managing users and tasks, including user registration & authentication along with task creation, approvals, and comments.
---

## Steps to run the application
### 1. Run the docker-compose.yaml file from the project directory using the command "docker-compose up -d". This will spin up the Postgre and MailHog containers.
### 2. Run the application by executing the TaskManagementApplication.main().

---

## Steps to stop the application
### 1. Shut down the containers using the command "docker-compose down" from the project directory. This will stop the containers.
### 2. Stop the application.

---

## Base URLs

- **User Management**: `/api/users`
- **Task Management**: `/api/tasks`

---

## User Management Endpoints

### 1. Get All Users
**GET** `/api/users`

Fetches a list of all registered users.

- **Request**: No parameters.
- **Response**:
    - **200 OK**
      ```json
      {
        "success": true,
        "message": "Users fetched successfully",
        "data": [
          {
            "id": 1,
            "username": "john_doe",
            "email": "john.doe@example.com"
          }
        ]
      }
      ```

### 2. Get User By ID
**GET** `/api/users/{userId}`

Fetches details of a specific user.

- **Path Parameter**:
    - `userId`: The unique ID of the user.
- **Response**:
    - **200 OK**
      ```json
      {
        "success": true,
        "message": "User fetched successfully",
        "data": {
          "id": 1,
          "username": "john_doe",
          "email": "john.doe@example.com"
        }
      }
      ```

### 3. Register User
**POST** `/api/users/register`

Registers a new user.

- **Request Body**:
  ```json
  {
    "username": "john_doe",
    "password": "password123",
    "email": "john.doe@example.com"
  }
  ```
- **Response**:
    - **201 Created**
      ```json
      {
        "success": true,
        "message": "User registered successfully",
        "data": {
          "id": 1,
          "username": "john_doe",
          "email": "john.doe@example.com"
        }
      }
      ```

### 4. Login
**POST** `/api/users/login`

Authenticates a user and returns a success message.

- **Request Body**:
  ```json
  {
    "username": "john_doe",
    "password": "password123"
  }
  ```
- **Response**:
    - **200 OK**
      ```json
      {
        "success": true,
        "message": "Login successful",
        "data": null
      }
      ```

### 5. Unregister User
**DELETE** `/api/users/unregister/{userId}`

Deletes a user by ID.

- **Path Parameter**:
    - `userId`: The unique ID of the user.
- **Response**:
    - **200 OK**
      ```json
      {
        "success": true,
        "message": "User unregistered successfully",
        "data": null
      }
      ```

### 6. Reset Password
**PUT** `/api/users/reset-password`

Resets the password for a user by their email.

- **Query Parameters**:
    - `email`: The email of the user.
    - `newPassword`: The new password.
- **Response**:
    - **200 OK**
      ```json
      {
        "success": true,
        "message": "Password reset successfully",
        "data": null
      }
      ```

### 7. Reset Username
**PUT** `/api/users/reset-username`

Resets the username for a user by their email.

- **Query Parameters**:
    - `email`: The email of the user.
    - `username`: The new username.
- **Response**:
    - **200 OK**
      ```json
      {
        "success": true,
        "message": "Username reset successfully",
        "data": null
      }
      ```

---

## Task Management Endpoints

### 1. Create Task
**POST** `/api/tasks`

Creates a new task.

- **Request Body**:
  ```json
  {
    "title": "Task Title",
    "description": "Task Description",
    "creator": {
      "id": 1
    }
  }
  ```
- **Query Parameters**:
    - `approverIds`: List of IDs of approvers.
- **Response**:
    - **201 Created**
      ```json
      {
        "success": true,
        "message": "Task created successfully",
        "data": {
          "id": 101,
          "title": "Task Title",
          "description": "Task Description",
          "creator": {
            "id": 1,
            "username": "john_doe"
          }
        }
      }
      ```

### 2. Get All Tasks
**GET** `/api/tasks`

Fetches all tasks.

- **Request**: No parameters.
- **Response**:
    - **200 OK**
      ```json
      {
        "success": true,
        "message": "Tasks fetched successfully",
        "data": [
          {
            "id": 101,
            "title": "Task Title",
            "description": "Task Description",
            "status": "PENDING"
          }
        ]
      }
      ```

### 3. Get Task By ID
**GET** `/api/tasks/{taskId}`

Fetches details of a specific task.

- **Path Parameter**:
    - `taskId`: The unique ID of the task.
- **Response**:
    - **200 OK**
      ```json
      {
        "success": true,
        "message": "Task fetched successfully",
        "data": {
          "id": 101,
          "title": "Task Title",
          "description": "Task Description",
          "status": "PENDING"
        }
      }
      ```

### 4. Delete Task
**DELETE** `/api/tasks/{taskId}`

Deletes a task by ID.

- **Path Parameter**:
    - `taskId`: The unique ID of the task.
- **Response**:
    - **200 OK**
      ```json
      {
        "success": true,
        "message": "Task deleted successfully",
        "data": null
      }
      ```

### 5. Add Approver to Task
**POST** `/api/tasks/{taskId}/add-approver/{approverId}`

Adds an approver to a task.

- **Path Parameter**:
    - `taskId`: The unique ID of the task.
    - `approverId`: The ID of the approver.

- **Response**:
    - **201 Created**
      ```json
      {
        "success": true,
        "message": "Approver added successfully",
        "data": {
          "id": 101,
          "title": "Task Title",
          "approvers": [
            {
              "id": 2,
              "username": "approver_user"
            }
          ]
        }
      }
      ```

### 6. Approve Task
**POST** `/api/tasks/{taskId}/approve/{approverId}`

Approves a task by an approver.

- **Path Parameters**:
    - `taskId`: The unique ID of the task.
    - `approverId`: The ID of the approver.
- **Response**:
    - **200 OK**
      ```json
      {
        "success": true,
        "message": "Task approved successfully",
        "data": null
      }
      ```

### 7. Add Comment to Task
**POST** `/api/tasks/{taskId}/add-comment/{commenterId}`

Adds a comment to a task.

- **Path Parameters**:
    - `taskId`: The unique ID of the task.
    - `commenterId`: The ID of the user adding the comment.
- **Request Body**:
  ```json
  {
    "content": "This is a comment."
  }
  ```
- **Response**:
    - **201 Created**
      ```json
      {
        "success": true,
        "message": "Comment added successfully",
        "data": {
          "id": 301,
          "content": "This is a comment.",
          "commenter": {
            "id": 1,
            "username": "john_doe"
          }
        }
      }
      ```

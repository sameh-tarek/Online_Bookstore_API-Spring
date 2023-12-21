# Online Bookstore Management System API with Java Spring Boot

## Overview

The Online Bookstore Management System simplifies book-related activities for customers and administrators. For detailed information, refer to the [Features](#features) section.

[![Under Development](https://img.shields.io/badge/Status-Under%20Development-yellow)](https://github.com/sameh-tarek/Online_Bookstore_API-Spring)

# Test API here

[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://god.gw.postman.com/run-collection/28660393-a2d78342-816a-459b-acd9-c0497faf33f7?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D28660393-a2d78342-816a-459b-acd9-c0497faf33f7%26entityType%3Dcollection%26workspaceId%3D2530de03-f49e-405c-a206-6791503e028b)

## Requirements

1. **Java Development Kit (JDK) 17 or above:**
   - Ensure that you have Java Development Kit version 17 or a later version installed on your system.

2. **MySQL Database:**
    - Utilize a local MySQL instance or connect to a remote MySQL server.

## How to Run

1- Clone the project repository from Git (if it's not already cloned).

2- Import the project into your favorite Java IDE (e.g., IntelliJ, Eclipse, etc.).

3- Build the project to resolve dependencies.


## Features

### Customer

| No. | Feature                 | Description                                     | Endpoint                        |
|----:|-------------------------|-------------------------------------------------|---------------------------------|
| 1.  | Browse Books            | Explore books by categories.                    | `GET /book/category/{category}` |
| 2.  | View Book Details       | Get detailed information about a specific book. | `GET /book/{id}`                |
| 3.  | Request to Borrow a Book| Request to borrow a specific book.              | `POST /book/{id}/borrow`        |
| 4.  | Check Borrowing Status  | View borrowing requests for a specific customer.| `GET /book/myrequests` |
| 5.  | Register                | Register a new customer.                        | `POST /auth/register`           |
| 6.  | Authenticate            | Authenticate as a customer.                    | `POST /auth/authentication`     |

### Admin

| No. | Feature                 | Description                                     | Endpoint                              |
|----:|-------------------------|-------------------------------------------------|----------------------------------------|
| 1.  | Add New Book            | Add a new book to the inventory.                | `POST /book/add`                      |
| 2.  | Update Book Details     | Update details of an existing book.             | `PUT /book/update/{id}`               |
| 3.  | Manage Inventory        |                                                 |                                        |
|     | - Update Stock Levels   | Update stock levels of a book.                  | `PUT /book/{id}/stock`                |
|     | - Set Availability       | Set the availability status of a book.          | `PUT /book/{id}/availability`         |
| 4.  | Delete Book             | Delete a book from the inventory.               | `DELETE /book/delete/{id}`            |
| 5.  | Manage Borrowing Requests| Update the status of a borrowing request.       | `PUT /book/borrow/{requestId}/status` |
| 6.  | Get All Borrowing Requests| View a list of all borrowing requests.        | `GET /book/requests`                  |
| 7.  | Authenticate            | Authenticate as an admin.                      | `POST /auth/authentication`           |


## Creating RSA Key Pair
- in a new folder under /src/main/resources/certs 
- run this commands in terminal

```bash
# create rsa key pair
openssl genrsa -out keypair.pem 2048
# extract public key
openssl rsa -in keypair.pem -pubout -out public.pem
# create private key in PKCS#8 format
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
```

## Swagger Documentation

After running the project, you can access the Swagger documentation to explore and understand the APIs.

### Swagger UI

Visit [http://localhost:8282/swagger-ui/index.html](http://localhost:8282/swagger-ui/index.html) to interact with the Swagger UI.

### API Documentation

You can also access the raw API documentation in JSON format at [http://localhost:8282/v3/api-docs](http://localhost:8282/v3/api-docs).


## ERD 
![](C:\Users\acer\Desktop\BorrowingRequest.png)
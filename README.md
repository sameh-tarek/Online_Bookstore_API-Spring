# Online Bookstore Management System API with java Spring Boot
## Overview

The Online Bookstore Management System simplifies book-related activities for customers and administrators. For detailed information, refer to the [Features](#features) section.

[![Under Development](https://img.shields.io/badge/Status-Under%20Development-yellow)](https://github.com/sameh-tarek/Online_Bookstore_API-Spring)

# Test API here

[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://god.gw.postman.com/run-collection/28660393-a2d78342-816a-459b-acd9-c0497faf33f7?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D28660393-a2d78342-816a-459b-acd9-c0497faf33f7%26entityType%3Dcollection%26workspaceId%3D2530de03-f49e-405c-a206-6791503e028b)

## Features

### Customer

| No. | Feature                 | Description                                     | Endpoint                              |
|----:|-------------------------|-------------------------------------------------|----------------------------------------|
| 1.  | Browse Books            | Explore books by categories.                    | `GET /book/category/{category}`        |
| 2.  | View Book Details       | Get detailed information about a specific book. | `GET /book/{id}`                       |
| 3.  | Request to Borrow a Book| Request to borrow a specific book.              | `POST /book/{id}/borrow`               |
| 4.  | Check Borrowing Status  | View borrowing requests for a specific customer.| `GET /book/requests/{userId}`          |

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

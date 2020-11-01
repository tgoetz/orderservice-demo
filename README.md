# Howto test and run the application

* Clone the GIT repository
  ```text
  $ git clone git@github.com:tgoetz/orderservice-demo.git
  ```
* Run tests
  ```text
  $ ./mvnw test
  ```
* Run the application
  ```text
  $ ./mvnw spring-boot:run
  ```
## Endpoints

The application runs on port `8080` and has two endpoints:

* `http://localhost:8080/products`
* `http://localhost:8080/orders`

## Data storage

An in-memory H2 database is used as storage solution, initial data is inserted by Spring Boot via `src/main/resources/data.sql`.

## API docs and swagger-ui

Please see the detailed API documentation below.
There's also a swagger-ui endpoint available that can be reached via `http://localhost:8080/swagger-ui/`
<br/>

---

# API Documentation

## Product Resource
CRUD resource for reading, creating, updating and deleting products

### Get all active products
Returns JSON data of all active products. Products are active if they are not deleted.

* **URL**

  /products

* **Method**

  `GET`

*  **URL Params**

   None

* **Data Params**

  None

* **Success Response**

  200 OK
  ```json
  [
    {
      "sku": "1111111",
      "name": "Banana",
      "price": 1.99,
      "createdAt": "2020-01-01T00:00:00",
      "deleted": false
    },
    {
      "sku": "2222222",
      "name": "Apple",
      "price": 1.29,
      "createdAt": "2020-01-01T00:00:00",
      "deleted": false
    },
    {
      "sku": "3333333",
      "name": "Kiwi",
      "price": 0.69,
      "createdAt": "2020-01-01T00:00:00",
      "deleted": false
    }
  ]
  ```

* **Error Response**

  None

* **Sample Call**

  ```bash
  curl -X GET "http://localhost:8080/products" -H "accept: */*"
  ```

---

### Get single product by SKU
Returns JSON data of a single product with the requested SKU

* **URL**

  /products/{sku}

* **Method**

  `GET`

*  **URL Params**

   **Required**

   `sku=[string]`

* **Data Params**

  None

* **Success Response**

  200 OK
  ```json
  [
    {
      "sku": "1111111",
      "name": "Banana",
      "price": 1.99,
      "createdAt": "2020-01-01T00:00:00",
      "deleted": false
    },
  ]
  ```

* **Error Response**
  If no product was found with the given SKU

  404 NOT FOUND

  ```json
  {
      "timestamp": "2020-10-31T13:40:23.964042",
      "message": "Product not found with SKU: 1",
      "details": "uri=/products/123",
      "status": "404 (NOT_FOUND)"
  }
  ```

* **Sample Call**

  ```bash
  curl -X GET "http://localhost:8080/products/1111111" -H "accept: */*"
  ```

---

### Create new product
Creates and persists a new product

* **URL**

  /products

* **Method**

  `POST`

*  **URL Params**

   None

* **Data Params**

  **Required**

  `sku=[string]`
  `name=[string]`
  `price=[number]`

* **Success Response**

  201 CREATED
  ```text
  HTTP/1.1 201 
  Location: http://localhost:8080/products/4444444
  Content-Length: 0
  Date: Sat, 31 Oct 2020 13:13:39 GMT
  Connection: close
  ```

* **Error Response**
  If not all required parameters were supplied

  400 BAD REQUEST

  ```json
  {
      "timestamp": "2020-10-31T14:19:03.407427",
      "message": "sku ist mandatory, price ist mandatory",
      "details": "uri=/products",
      "status": "400 (BAD_REQUEST)"
  }
  ```

* **Sample Call**

  ```bash
  curl -X "POST" "http://localhost:8080/products" \
       -H 'Content-Type: application/json; charset=utf-8' \
       -d $'{"sku": 9572394, "name": "Cherry", "price": 3.49}'
  ```

---

### Update Product
Updates an existing product with new values

* **URL**

  /products/{sku}

* **Method**

  `PUT`

*  **URL Params**

   **Required**

   `sku=[string]`

* **Data Params**

  **Required**

  `name=[string]`
  `price=[number]`

* **Success Response**

  204 NO CONTENT

* **Error Response**
  If no product was found with the given SKU

  404 NOT FOUND <br />
  ```json
  {
      "timestamp": "2020-10-31T15:17:29.672282",
      "message": "Product not found with SKU: 123",
      "details": "uri=/products/123",
      "status": "404 (NOT_FOUND)"
  }
  ```

* **Sample Call**

  ```bash
  curl -X "PUT" "http://localhost:8080/products/3333333" \
       -H 'Content-Type: application/json; charset=utf-8' \
       -d $'{ "name": "Kiwi", "price": 0.49 }'
  ```

---

### Delete Product
Deletes the product with the specified SKU (soft deletion).
The product will _not_ be removed from the datastore but instead flagged as 'deleted'.

* **URL**

  /products/{sku}

* **Method**

  `DELETE`

*  **URL Params**

   **Required**

   `sku=[string]`

* **Data Params**

  None

* **Success Response**

  200 OK<br />

* **Error Response**
  If no product was found with the given SKU or the product has already been deleted

  404 NOT FOUND <br />
  ```json
  {
      "timestamp": "2020-10-31T15:17:29.672282",
      "message": "Product not found with SKU: 123",
      "details": "uri=/products/123",
      "status": "404 (NOT_FOUND)"
  }
  ```

* **Sample Call**

  ```bash
  curl -X "PUT" "http://localhost:8080/products/3333333" \
       -H 'Content-Type: application/json; charset=utf-8' \
       -d $'{ "name": "Kiwi", "price": 0.49 }'
  ```

---
<br/>

## Order Resource
Resource for receiving orders within a certain time period and for placing new orders
### Get all orders
Returns JSON data of all existing orders or only of those within a given time period

* **URL**

  /orders

* **Method**

  `GET`

*  **URL Params**

   **Optional**
   `from=[datetime]`
   `to=[datetime]`

   Datetime values must be provided in common ISO format,
   e.g. `2020-10-30T00:00:00`


* **Data Params**

  None

* **Success Response**

  200 OK
  ```json
  [
    {
      "id": 1,
      "buyerEmail": "tom@decoded.de",
      "createdAt": "2020-01-01T12:00:00",
      "items": [
        {
          "id": 1,
          "product": {
            "sku": "1111111",
            "name": "Banana",
            "price": 1.99,
            "createdAt": "2020-01-01T00:00:00",
            "deleted": false
          },
          "amount": 3
        }
      ],
      "totalPrice": 5.97
    },
    {
      "id": 2,
      "buyerEmail": "tom@decoded.de",
      "createdAt": "2020-01-01T13:00:00",
      "items": [
        {
          "id": 3,
          "product": {
            "sku": "3333333",
            "name": "Kiwi",
            "price": 0.49,
            "createdAt": "2020-10-31T15:59:43.242522",
            "deleted": false
          },
          "amount": 9
        },
        {
          "id": 2,
          "product": {
            "sku": "2222222",
            "name": "Apple",
            "price": 1.29,
            "createdAt": "2020-01-01T00:00:00",
            "deleted": false
          },
          "amount": 5
        }
      ],
      "totalPrice": 10.86
    }
  ]
  ```

* **Error Response**

  None

* **Sample Call**

  ```bash
  curl "http://localhost:8080/orders?from=2020-01-01T00:00:00&to=2020-03-01T00:00:00" \
       -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8'
  ```

---

### Place New Order
Places a new order


* **URL**

  /orders

* **Method**

  `POST`

*  **URL Params**

   None

* **Data Params**

  Example:
  ```json
  {
    "buyerEmail": "tom@decoded.de",
    "items": [
        {
          "product": {
            "sku": 1111111
           },
           "amount": 3
       }
    ]
  }
  ```

* **Success Response**

  201 CREATED
  ```text
  HTTP/1.1 201 
  Location: http://localhost:8080/orders/6
  Content-Length: 0
  Date: Sat, 31 Oct 2020 15:16:49 GMT
  Connection: close
  ```

* **Error Response**
  If one of the products was not found

  400 BAD REQUEST
  ```json
  {
    "timestamp": "2020-10-31T16:18:04.375327",
    "message": "Product not found with SKU: 666",
    "details": "uri=/orders",
    "status": "400 (BAD_REQUEST)"
  }
  ```



* **Sample Call**

  ```bash
  curl -X "POST" "http://localhost:8080/orders" \
       -H 'Content-Type: application/json; charset=utf-8' \
       -d $'{"items": [{"product": {"sku": 1111111}, "amount": 3}], "buyerEmail": "tom@decoded.de"}'
  ```

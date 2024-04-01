# Routes

Later this will be on wiki

**Notes:**

1. The base url usually is your localhost.
2. The `#d` means that you need to replace it with your data.
3. The `#t` means your jwt token.
4. The `#s` means that it was generated on the server-side.
5. Content-type is `Application/json` everywhere.

## Login

```txt
url: {baseUrl}/login
method: Post
responses: 202, 405, 409
```

```json
{
  "subject": "#d",
  "password": "#d"
}
```

## User (Crud operations)

### Create a new user

- Also, profiles are defined as: `Admin, Manager, Customer, Cutter, Inspector,
Tailor, Others`.
- **Token here is not used for convenience!**

```txt
url: {baseUrl}/new/user
method: Post
responses: 201, 500, 409, 405
```

```json
{
  "subject": "#d",
  "password": "#d",
  "image": "#d",
  "first_name": "#d",
  "last_name": "#d",
  "email": "#d",
  "profile": "#d"
}
```

### Get the user by subject

```txt
url: {baseUrl}/user/{subject?}
method: Get
headers: Authorization: Bearer #t
responses: 200, 409
```

- Json response:

```json
{
  "id": ...,
  ...
}
```

### Update the user's column

- You need to specify the user's subject (`updater` in json) - who's the updater
of a column.
- You can update:

```text
1. subject
2. password
3. image
4. first_name
5. last_name
6. email
7. profile
8. salt (if you want to)`
```

- But only updaters with `Admin` profile can
update others passwords!`

```txt
url: {baseUrl}/user/{subject?}/edit/{columnToUpdate?}
method: Put
headers: Authorization: Bearer #t
responses: 200, 500, 409, 405
```

```json
{
  "updater": "#d",
  "value": "#d"
}
```

### Delete the user by subject

- You need to specify the user's subject (`deleter` in json) - who's the deleter.

```txt
url: {baseUrl}/delete/user/{subject?}
method: Delete
headers: Authorization: Bearer #t
responses: 200, 500, 409, 405
```

```json
{
  "deleter": "#d"
}
```

## Orders (Crud and pagination)

### Create a new order

- Also, status are defined as: `NotStarted, Cutter, Checked`.

```txt
url: {baseUrl}/new/order
method: Post
headers: Authorization: Bearer #t
responses: 201, 500, 409, 405
```

```json
{
  "order_id": #d,
  "customer": "#d",
  "date": "#d",
  "title": "#d",
  "model": "#d",
  "size": "#d",
  "color": "#d",
  "category": "#d",
  "quantity": #d,
  "status": "#d"
}
```

### Get the order

```txt
url: {baseUrl}/order/{order_id?}
method: Get
headers: Authorization: Bearer #t
responses: 200, 405
```

- Json response:

```json
{
  "order_id": ...,
  ...
}
```

### Get the orders (Pagination)

```txt
url: {baseUrl}/orders
method: Get
headers: Authorization: Bearer #t
responses: 200
parameters: page, per_page
```

- Json response:

```json
{
  "paginated_orders": [
    {
      "order_id": ...,
      ...
    },
    ...
  ],
  "has_next_page": "#s"
}
```

### Update the order's column

- You need to specify the user's subject (`updater` in json) - who's the updater
of a column.
- You can update:

```text
1. order_id
2. customer
3. date
4. title
5. model 
6. size 
7. color 
8. category
9. quantity
10. status
```

- But only updaters with `Customer` profile can
update others columns!`

```txt
url: {baseUrl}/{order_id?}/edit/{columnToUpdate?}
method: Put
headers: Authorization: Bearer #t
responses: 200, 500, 409, 405
```

```json
{
  "updater": "#d",
  "value": "#d"
}
```

### Delete the order

- You need to specify the user's subject (`deleter` in json) - who's the deleter.

```txt
url: {baseUrl}/delete/order/{order_id?}
method: Delete
headers: Authorization: Bearer #t
responses: 200, 500, 409, 405
```

```json
{
  "deleter": "#d"
}
```

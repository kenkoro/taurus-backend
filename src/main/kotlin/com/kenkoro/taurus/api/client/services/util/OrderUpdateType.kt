package com.kenkoro.taurus.api.client.services.util

enum class OrderUpdateType(val toSql: String) {
  OrderId("order_id"),
  Customer("customer"),
  Date("date"),
  Title("title"),
  Model("model"),
  Size("size"),
  Color("color"),
  Category("category"),
  Quantity("quantity"),
  Status("status")
}
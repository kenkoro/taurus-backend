package com.kenkoro.taurus.api.client.services

import com.kenkoro.taurus.api.client.core.mappers.fromResult
import com.kenkoro.taurus.api.client.core.mappers.setValues
import com.kenkoro.taurus.api.client.models.request.order.Order
import com.kenkoro.taurus.api.client.services.util.OrderUpdateType
import java.sql.Connection

class PostgresOrderCrudService(
  private val db: Connection
) {
  companion object {
    const val TABLE = "instruct"
    const val ORDER_ID = "order_id"
    const val CUSTOMER = "customer"
    const val DATE = "date"
    const val TITLE = "title"
    const val MODEL = "model"
    const val SIZE = "size"
    const val COLOR = "color"
    const val CATEGORY = "category"
    const val QUANTITY = "quantity"
    const val STATUS = "status"
  }

  fun create(model: Order): Boolean {
    val preparedStatement = db.prepareStatement(
      "INSERT INTO " +
          "$TABLE($ORDER_ID, $CUSTOMER, $DATE, $TITLE, $MODEL, $SIZE, $COLOR, $CATEGORY, $QUANTITY, $STATUS)" +
          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CAST(? AS order_status))"
    )
    preparedStatement.setValues(model)

    val updatedRows = preparedStatement.executeUpdate()

    return updatedRows > 0
  }

  fun read(orderId: Int): Order {
    val preparedStatement = db.prepareStatement("SELECT * FROM $TABLE WHERE $ORDER_ID = ?")
    preparedStatement.setInt(1, orderId)
    val result = preparedStatement.executeQuery()

    val orders = mutableListOf<Order>()
    while (result.next()) {
      orders += Order.fromResult(result)
    }

    return orders.first()
  }

  fun readAll(offset: Int, perPage: Int): List<Order> {
    val preparedStatement = db.prepareStatement(
      "SELECT * FROM $TABLE LIMIT ? OFFSET ?"
    )
    preparedStatement.setInt(1, perPage)
    preparedStatement.setInt(2, offset)
    val result = preparedStatement.executeQuery()

    val orders = mutableListOf<Order>()
    while (result.next()) {
      orders += Order.fromResult(result)
    }

    return orders
  }

  fun update(type: OrderUpdateType, value: String, orderId: Int): Int {
    val sql = if (type == OrderUpdateType.Status) {
      "UPDATE $TABLE SET ${type.toSql} = CAST(? AS order_status) WHERE $ORDER_ID = ?"
    } else {
      "UPDATE $TABLE SET ${type.toSql} = ? WHERE $ORDER_ID = ?"
    }
    val preparedStatement = db.prepareStatement(sql)
    preparedStatement.setString(1, value)
    preparedStatement.setInt(2, orderId)

    return preparedStatement.executeUpdate()
  }

  fun delete(orderId: Int): Int {
    val preparedStatement = db.prepareStatement(
      "DELETE FROM $TABLE WHERE $ORDER_ID = ?"
    )
    preparedStatement.setInt(1, orderId)

    return preparedStatement.executeUpdate()
  }
}
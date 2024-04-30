package com.kenkoro.taurus.api.client.services.dao

import com.kenkoro.taurus.api.client.models.orm.Order
import com.kenkoro.taurus.api.client.models.orm.Orders
import com.kenkoro.taurus.api.client.services.DbService.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class OrderDaoFacadeImpl : OrderDaoFacade {
  private fun resultRowToOrder(row: ResultRow) = Order(
    recordId = row[Orders.orderId],
    orderId = row[Orders.orderId],
    customer = row[Orders.customer],
    date = row[Orders.date],
    title = row[Orders.title],
    model = row[Orders.model],
    size = row[Orders.size],
    color = row[Orders.color],
    category = row[Orders.category],
    quantity = row[Orders.quantity],
  )

  override suspend fun order(id: Int): Order? = dbQuery {
    Orders
      .selectAll()
      .where { Orders.orderId eq id }
      .map(::resultRowToOrder)
      .singleOrNull()
  }

  override suspend fun addNewOrder(id: Int, fields: OrderFields): Order? = dbQuery {
    val insertStatement = Orders.insertIgnore {
      it[orderId] = id
      it[customer] = fields.customer
      it[date] = System.currentTimeMillis()
      it[title] = fields.title
      it[model] = fields.model
      it[size] = fields.size
      it[color] = fields.color
      it[category] = fields.category
      it[quantity] = fields.quantity
    }
    insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToOrder)
  }

  override suspend fun deleteOrder(id: Int): Boolean = dbQuery {
    Orders.deleteWhere { orderId eq id } > 0
  }

  override suspend fun allOrders(): List<Order> = dbQuery {
    Orders.selectAll().map(::resultRowToOrder)
  }

  override suspend fun paginatedOrders(offset: Long, perPage: Int): List<Order> = dbQuery {
    Orders.selectAll().limit(n = perPage, offset = offset).map(::resultRowToOrder)
  }

  override suspend fun editOrder(id: Int, fields: OrderFields): Boolean = dbQuery {
    Orders.update({ Orders.orderId eq id }) {
      it[customer] = fields.customer
      it[title] = fields.title
      it[model] = fields.model
      it[size] = fields.size
      it[color] = fields.color
      it[category] = fields.category
      it[quantity] = fields.quantity
    } > 0
  }
}
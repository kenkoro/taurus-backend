package com.kenkoro.taurus.api.client.services.dao

import com.kenkoro.taurus.api.client.models.orm.Order
import com.kenkoro.taurus.api.client.models.orm.Orders
import com.kenkoro.taurus.api.client.models.orm.Orders.category
import com.kenkoro.taurus.api.client.models.orm.Orders.color
import com.kenkoro.taurus.api.client.models.orm.Orders.model
import com.kenkoro.taurus.api.client.models.orm.Orders.quantity
import com.kenkoro.taurus.api.client.models.orm.Orders.size
import com.kenkoro.taurus.api.client.models.orm.Orders.status
import com.kenkoro.taurus.api.client.models.orm.Orders.title
import com.kenkoro.taurus.api.client.services.DbService.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder

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
    status = row[Orders.status],
  )

  private fun setFields(builder: UpdateBuilder<*>, fields: OrderFields) {
    builder[title] = fields.title
    builder[model] = fields.model
    builder[size] = fields.size
    builder[color] = fields.color
    builder[category] = fields.category
    builder[quantity] = fields.quantity
    builder[status] = fields.status
  }

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
      setFields(it, fields)
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
      setFields(it, fields)
    } > 0
  }
}
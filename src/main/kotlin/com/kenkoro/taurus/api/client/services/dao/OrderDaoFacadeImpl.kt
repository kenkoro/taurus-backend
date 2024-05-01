package com.kenkoro.taurus.api.client.services.dao

import com.kenkoro.taurus.api.client.models.orm.NewOrder
import com.kenkoro.taurus.api.client.models.orm.Order
import com.kenkoro.taurus.api.client.models.orm.Orders
import com.kenkoro.taurus.api.client.models.orm.Orders.category
import com.kenkoro.taurus.api.client.models.orm.Orders.color
import com.kenkoro.taurus.api.client.models.orm.Orders.creatorId
import com.kenkoro.taurus.api.client.models.orm.Orders.customer
import com.kenkoro.taurus.api.client.models.orm.Orders.date
import com.kenkoro.taurus.api.client.models.orm.Orders.model
import com.kenkoro.taurus.api.client.models.orm.Orders.orderId
import com.kenkoro.taurus.api.client.models.orm.Orders.quantity
import com.kenkoro.taurus.api.client.models.orm.Orders.recordId
import com.kenkoro.taurus.api.client.models.orm.Orders.size
import com.kenkoro.taurus.api.client.models.orm.Orders.status
import com.kenkoro.taurus.api.client.models.orm.Orders.title
import com.kenkoro.taurus.api.client.services.DbService.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder

class OrderDaoFacadeImpl : OrderDaoFacade {
  private fun resultRowToOrder(row: ResultRow) = Order(
    recordId = row[recordId],
    orderId = row[orderId],
    customer = row[customer],
    date = row[date],
    title = row[title],
    model = row[model],
    size = row[size],
    color = row[color],
    category = row[category],
    quantity = row[quantity],
    status = row[status],
    creatorId = row[creatorId],
  )

  private fun setFields(builder: UpdateBuilder<*>, fields: NewOrder) {
    builder[customer] = fields.customer
    builder[title] = fields.title
    builder[model] = fields.model
    builder[size] = fields.size
    builder[color] = fields.color
    builder[category] = fields.category
    builder[quantity] = fields.quantity
    builder[status] = fields.status
    builder[creatorId] = fields.creatorId
  }

  override suspend fun order(id: Int): Order? = dbQuery {
    Orders
      .selectAll()
      .where { orderId eq id }
      .map(::resultRowToOrder)
      .singleOrNull()
  }

  override suspend fun addNewOrder(order: NewOrder): Order? = dbQuery {
    val insertStatement = Orders.insertIgnore {
      it[orderId] = order.orderId
      it[date] = System.currentTimeMillis()
      setFields(it, order)
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

  override suspend fun editOrder(order: NewOrder): Boolean = dbQuery {
    Orders.update({ orderId eq order.orderId }) {
      setFields(it, order)
    } > 0
  }
}
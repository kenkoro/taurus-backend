package com.kenkoro.taurus.api.client.services.dao

import com.kenkoro.taurus.api.client.models.EditOrder
import com.kenkoro.taurus.api.client.models.NewOrder
import com.kenkoro.taurus.api.client.models.Order
import com.kenkoro.taurus.api.client.models.Orders
import com.kenkoro.taurus.api.client.models.Orders.category
import com.kenkoro.taurus.api.client.models.Orders.color
import com.kenkoro.taurus.api.client.models.Orders.creatorId
import com.kenkoro.taurus.api.client.models.Orders.customer
import com.kenkoro.taurus.api.client.models.Orders.date
import com.kenkoro.taurus.api.client.models.Orders.model
import com.kenkoro.taurus.api.client.models.Orders.orderId
import com.kenkoro.taurus.api.client.models.Orders.quantity
import com.kenkoro.taurus.api.client.models.Orders.recordId
import com.kenkoro.taurus.api.client.models.Orders.size
import com.kenkoro.taurus.api.client.models.Orders.status
import com.kenkoro.taurus.api.client.models.Orders.title
import com.kenkoro.taurus.api.client.services.DbService.dbQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update
import java.io.File
import java.io.FileOutputStream

class OrderDaoFacadeImpl : OrderDaoFacade {
  private fun resultRowToOrder(row: ResultRow) =
    Order(
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

  private fun UpdateBuilder<*>.setOrderFields(order: NewOrder) {
    this[customer] = order.customer
    this[title] = order.title
    this[model] = order.model
    this[size] = order.size
    this[color] = order.color
    this[category] = order.category
    this[quantity] = order.quantity
    this[status] = order.status
    this[creatorId] = order.creatorId
  }

  override suspend fun order(id: Int): Order? =
    dbQuery {
      Orders
        .selectAll()
        .where { orderId eq id }
        .map(::resultRowToOrder)
        .singleOrNull()
    }

  private suspend fun autoIncOrderIdAndResetItIfNeeded(): Int =
    dbQuery {
      val firstOrderId = 1
      val currentOrderId =
        try {
          Orders.select(orderId).map { it[orderId] }.last()
        } catch (_: NoSuchElementException) {
          0
        }

      if (currentOrderId < 1000) {
        currentOrderId + 1
      } else {
        backUpOrdersInfo()
        resetOrders()

        firstOrderId
      }
    }

  private suspend fun backUpOrdersInfo() {
    val orders = allOrders()
    val backUpFile = File("backup/${System.currentTimeMillis()}-ms.txt")

    withContext(Dispatchers.IO) {
      if (!backUpFile.exists()) {
        backUpFile.createNewFile()
      }

      val fos = FileOutputStream(backUpFile)
      for (order in orders) {
        fos.write(order.toString().toByteArray())
      }
    }
  }

  private suspend fun resetOrders() {
    (1..1000).map { deleteOrder(it) }
  }

  override suspend fun addNewOrder(order: NewOrder): Order? =
    dbQuery {
      val newOrderId = autoIncOrderIdAndResetItIfNeeded()
      val insertStatement =
        Orders.insertIgnore {
          it[orderId] = newOrderId
          it[date] = System.currentTimeMillis()
          it.setOrderFields(order)
        }
      insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToOrder)
    }

  override suspend fun deleteOrder(id: Int): Boolean =
    dbQuery {
      Orders.deleteWhere { orderId eq id } > 0
    }

  override suspend fun allOrders(): List<Order> =
    dbQuery {
      Orders.selectAll().map(::resultRowToOrder)
    }

  override suspend fun paginatedOrders(
    offset: Long,
    perPage: Int,
    phrase: String,
  ): List<Order> =
    dbQuery {
      Orders
        .selectAll()
        .where {
          /** Not good for big databases to use like function in sql */
          if (phrase.isDigitsOnly()) {
            orderId eq phrase.toInt()
          } else {
            title like "%${phrase.trim()}%"
          }
        }
        .limit(n = perPage, offset = offset)
        .map(::resultRowToOrder)
    }

  override suspend fun editOrder(order: EditOrder): Boolean =
    dbQuery {
      Orders.update({ orderId eq order.orderId }) {
        it.setOrderFields(order.toNewOrder())
      } > 0
    }
}

private fun EditOrder.toNewOrder(): NewOrder {
  return NewOrder(
    customer = customer,
    title = title,
    model = model,
    size = size,
    color = color,
    category = category,
    quantity = quantity,
    status = status,
    creatorId = creatorId,
  )
}

fun String.isDigitsOnly(): Boolean {
  if (this.isEmpty()) return false

  for (char in this) {
    if (!char.isDigit()) return false
  }

  return true
}

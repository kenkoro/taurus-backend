package com.kenkoro.taurus.api.client.services.dao

import com.kenkoro.taurus.api.client.models.CutOrder
import com.kenkoro.taurus.api.client.models.CutOrders
import com.kenkoro.taurus.api.client.models.CutOrders.comment
import com.kenkoro.taurus.api.client.models.CutOrders.cutOrderId
import com.kenkoro.taurus.api.client.models.CutOrders.cutterId
import com.kenkoro.taurus.api.client.models.CutOrders.date
import com.kenkoro.taurus.api.client.models.CutOrders.orderId
import com.kenkoro.taurus.api.client.models.CutOrders.quantity
import com.kenkoro.taurus.api.client.models.NewCutOrder
import com.kenkoro.taurus.api.client.services.DbService.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder

class CutOrderDaoFacadeImpl : CutOrderDaoFacade {
  private fun resultRowToCutOrder(row: ResultRow) =
    CutOrder(
      cutOrderId = row[cutOrderId],
      orderId = row[orderId],
      date = row[date],
      quantity = row[quantity],
      cutterId = row[cutterId],
      comment = row[comment],
    )

  private fun UpdateBuilder<*>.setOrderFields(newCutOrder: NewCutOrder) {
    this[orderId] = newCutOrder.orderId
    this[quantity] = newCutOrder.quantity
    this[cutterId] = newCutOrder.cutterId
    this[comment] = newCutOrder.comment
  }

  override suspend fun addNewCutOrder(newCutOrder: NewCutOrder): CutOrder? =
    dbQuery {
      val insertStatement =
        CutOrders
          .insertIgnore {
            it[date] = System.currentTimeMillis()
            it.setOrderFields(newCutOrder)
          }
      insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToCutOrder)
    }

  override suspend fun actualCutOrdersQuantity(id: Int): Int? =
    dbQuery {
      val cutOrder =
        CutOrders
          .selectAll()
          .where { orderId eq id }
          .map(::resultRowToCutOrder)
          .singleOrNull()

      cutOrder?.quantity
    }
}

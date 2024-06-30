package com.kenkoro.taurus.api.client.models

import com.kenkoro.taurus.api.client.models.enums.OrderStatus
import com.kenkoro.taurus.api.client.models.util.PGEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Order(
  @SerialName("record_id") val recordId: Int,
  @SerialName("order_id") val orderId: Int,
  val customer: String,
  val date: Long,
  val title: String,
  val model: String,
  val size: String,
  val color: String,
  val category: String,
  val quantity: Int,
  val status: OrderStatus,
  @SerialName("creator_id") val creatorId: Int,
)

@Serializable
data class NewOrder(
  @SerialName("order_id") val orderId: Int,
  val customer: String,
  val title: String,
  val model: String,
  val size: String,
  val color: String,
  val category: String,
  val quantity: Int,
  val status: OrderStatus,
  @SerialName("creator_id") val creatorId: Int,
)

object Orders : Table() {
  override val primaryKey: PrimaryKey
    get() = PrimaryKey(recordId)
  val recordId = integer("record_id").autoIncrement()
  val orderId = integer("order_id").uniqueIndex()
  val customer = varchar("customer", 128)
  val date = long("date")
  val title = varchar("title", 128)
  val model = varchar("model", 128)
  val size = varchar("size", 32)
  val color = varchar("color", 32)
  val category = varchar("category", 32)
  val quantity = integer("quantity")
  val status =
    customEnumeration(
      "status",
      "order_status",
      fromDb = { value -> OrderStatus.valueOf(value as String) },
      toDb = { PGEnum("order_status", it) },
    )
  val creatorId = reference("creator_id", Users.userId)
}

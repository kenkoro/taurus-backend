package com.kenkoro.taurus.api.client.models.orm

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
)

object Orders : Table() {
  val recordId = integer("record_id").autoIncrement()
  val orderId = integer("order_id")
  val customer = varchar("customer", 128)
  val date = long("date")
  val title = varchar("title", 128)
  val model = varchar("model", 128)
  val size = varchar("size", 32)
  val color = varchar("color", 32)
  val category = varchar("category", 32)
  val quantity = integer("quantity")
}
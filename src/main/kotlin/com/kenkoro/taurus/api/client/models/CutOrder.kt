package com.kenkoro.taurus.api.client.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class CutOrder(
  @SerialName("cut_order_id") val cutOrderId: Int,
  @SerialName("order_id") val orderId: Int,
  val date: Long,
  val quantity: Int,
  @SerialName("cutter_id") val cutterId: Int,
  val comment: String,
)

@Serializable
data class NewCutOrder(
  @SerialName("order_id") val orderId: Int,
  val date: Long,
  val quantity: Int,
  @SerialName("cutter_id") val cutterId: Int,
  val comment: String,
)

object CutOrders : Table("cut_orders") {
  override val primaryKey: PrimaryKey
    get() = PrimaryKey(cutOrderId)
  val cutOrderId = integer("cut_order_id").autoIncrement()
  val orderId = reference("order_id", Orders.orderId)
  val date = long("date")
  val quantity = integer("quantity")
  val cutterId = reference("cutter_id", Users.userId)
  val comment = varchar("comment", 128)
}

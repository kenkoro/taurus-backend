package com.kenkoro.taurus.api.client.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class CheckedOrder(
  @SerialName("checked_order_id") val checkedOrderId: Int,
  @SerialName("cut_order_id") val cutOrderId: Int,
  @SerialName("checker_id") val checkerId: Int,
  val date: Long,
  val comment: String,
)

@Serializable
data class NewCheckedOrder(
  @SerialName("cut_order_id") val cutOrderId: Int,
  @SerialName("checker_id") val checkerId: Int,
  val date: Long,
  val comment: String,
)

object CheckedOrders : Table() {
  override val primaryKey: PrimaryKey
    get() = PrimaryKey(checkedOrderId)
  val checkedOrderId = integer("checked_order_id").autoIncrement()
  val cutOrderId = reference("cut_order_id", CutOrders.cutOrderId)
  val checkerId = reference("checker_id", Users.userId)
  val date = long("date")
  val comment = varchar("comment", 128)
}
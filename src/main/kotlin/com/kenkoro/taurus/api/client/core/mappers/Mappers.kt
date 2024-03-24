package com.kenkoro.taurus.api.client.core.mappers

import com.kenkoro.taurus.api.client.core.security.hashing.SaltedHash
import com.kenkoro.taurus.api.client.models.request.order.Order
import com.kenkoro.taurus.api.client.models.request.user.CreateUser
import com.kenkoro.taurus.api.client.models.request.user.CreateUserWithSalt
import com.kenkoro.taurus.api.client.models.request.user.GetUser
import com.kenkoro.taurus.api.client.models.util.OrderStatus
import com.kenkoro.taurus.api.client.models.util.UserProfile
import com.kenkoro.taurus.api.client.services.PostgresOrderUserCrudService.Companion.CATEGORY
import com.kenkoro.taurus.api.client.services.PostgresOrderUserCrudService.Companion.COLOR
import com.kenkoro.taurus.api.client.services.PostgresOrderUserCrudService.Companion.CUSTOMER
import com.kenkoro.taurus.api.client.services.PostgresOrderUserCrudService.Companion.DATE
import com.kenkoro.taurus.api.client.services.PostgresOrderUserCrudService.Companion.MODEL
import com.kenkoro.taurus.api.client.services.PostgresOrderUserCrudService.Companion.ORDER_ID
import com.kenkoro.taurus.api.client.services.PostgresOrderUserCrudService.Companion.QUANTITY
import com.kenkoro.taurus.api.client.services.PostgresOrderUserCrudService.Companion.SIZE
import com.kenkoro.taurus.api.client.services.PostgresOrderUserCrudService.Companion.STATUS
import com.kenkoro.taurus.api.client.services.PostgresOrderUserCrudService.Companion.TITLE
import com.kenkoro.taurus.api.client.services.PostgresUserUserCrudService.Companion.EMAIL
import com.kenkoro.taurus.api.client.services.PostgresUserUserCrudService.Companion.FIRST_NAME
import com.kenkoro.taurus.api.client.services.PostgresUserUserCrudService.Companion.ID
import com.kenkoro.taurus.api.client.services.PostgresUserUserCrudService.Companion.IMAGE
import com.kenkoro.taurus.api.client.services.PostgresUserUserCrudService.Companion.LAST_NAME
import com.kenkoro.taurus.api.client.services.PostgresUserUserCrudService.Companion.PASSWORD
import com.kenkoro.taurus.api.client.services.PostgresUserUserCrudService.Companion.PROFILE
import com.kenkoro.taurus.api.client.services.PostgresUserUserCrudService.Companion.SALT
import com.kenkoro.taurus.api.client.services.PostgresUserUserCrudService.Companion.SUBJECT
import java.sql.PreparedStatement
import java.sql.ResultSet

fun PreparedStatement.setValues(model: CreateUserWithSalt) {
  this.setString(1, model.subject)
  this.setString(2, model.password)
  this.setString(3, model.image)
  this.setString(4, model.firstName)
  this.setString(5, model.lastName)
  this.setString(6, model.email)
  this.setString(7, model.profile.name)
  this.setString(8, model.salt)
}

fun PreparedStatement.setValues(model: Order) {
  this.setInt(1, model.orderId)
  this.setString(2, model.customer)
  this.setString(3, model.date)
  this.setString(4, model.title)
  this.setString(5, model.model)
  this.setString(6, model.size)
  this.setString(7, model.color)
  this.setString(8, model.category)
  this.setInt(9, model.quantity)
  this.setString(10, model.status.name)
}

fun GetUser.Companion.fromResult(result: ResultSet): GetUser {
  return GetUser(
    id = result.getInt(ID),
    subject = result.getString(SUBJECT),
    password = result.getString(PASSWORD),
    image = result.getString(IMAGE),
    firstName = result.getString(FIRST_NAME),
    lastName = result.getString(LAST_NAME),
    email = result.getString(EMAIL),
    profile = UserProfile.valueOf(result.getString(PROFILE)),
    salt = result.getString(SALT)
  )
}

fun Order.Companion.fromResult(result: ResultSet): Order {
  return Order(
    orderId = result.getInt(ORDER_ID),
    customer = result.getString(CUSTOMER),
    date = result.getString(DATE),
    title = result.getString(TITLE),
    model = result.getString(MODEL),
    size = result.getString(SIZE),
    color = result.getString(COLOR),
    category = result.getString(CATEGORY),
    quantity = result.getInt(QUANTITY),
    status = OrderStatus.valueOf(
      result.getString(STATUS)
    )
  )
}

fun CreateUserWithSalt.Companion.fromRequest(request: CreateUser, saltedHash: SaltedHash): CreateUserWithSalt {
  return CreateUserWithSalt(
    subject = request.subject,
    password = saltedHash.hashedPasswordWithSalt,
    image = request.image,
    firstName = request.firstName,
    lastName = request.lastName,
    email = request.email,
    profile = request.profile,
    salt = saltedHash.salt
  )
}
package com.kenkoro.taurus.api.client.routes.util

import com.kenkoro.taurus.api.client.models.NewOrder
import com.kenkoro.taurus.api.client.models.NewUser

object Validator {
  fun isNewOrderValid(order: NewOrder): Boolean {
    return order.orderId > 0 &&
      order.customer.isNotBlank() &&
      order.title.isNotBlank() &&
      order.model.isNotBlank() &&
      order.size.isNotBlank() &&
      order.color.isNotBlank() &&
      order.category.isNotBlank() &&
      order.quantity > 0
  }

  fun isNewUserValid(user: NewUser): Boolean {
    return user.subject.isNotBlank() &&
      user.firstName.isNotBlank() &&
      user.lastName.isNotBlank() &&
      user.email.isNotBlank() &&
      user.image.isNotBlank() &&
      user.profile.name.isNotBlank()
  }
}

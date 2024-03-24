package com.kenkoro.taurus.api.client.services.util

enum class UserUpdateType(val toSql: String) {
  Subject("subject"),
  Password("password"),
  Image("image"),
  FirstName("first_name"),
  LastName("last_name"),
  Email("email"),
  Profile("profile"),
  Salt("salt")
}
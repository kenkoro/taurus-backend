package com.kenkoro.taurus.api.client.services.util

enum class UpdateType(val toSql: String) {
  SUBJECT("subject"),
  PASSWORD("password"),
  IMAGE("image"),
  FIRST_NAME("first_name"),
  LAST_NAME("last_name"),
  ROLE("role"),
  SALT("salt")
}
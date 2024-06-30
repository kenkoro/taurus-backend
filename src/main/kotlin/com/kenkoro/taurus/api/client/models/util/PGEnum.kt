package com.kenkoro.taurus.api.client.models.util

import org.postgresql.util.PGobject

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
  init {
    value = enumValue?.name
    type = enumTypeName
  }
}

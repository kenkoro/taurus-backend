package com.kenkoro.taurus.api.client.core.plugins

import kotlin.properties.Delegates

object SutService {
  private var isUnderTest by Delegates.notNull<Boolean>()

  fun turnOn(isUnderTest: Boolean) {
    SutService.isUnderTest = isUnderTest
  }

  fun get(): Boolean = isUnderTest
}
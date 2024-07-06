package com.kenkoro.taurus.api.client.controllers

interface OrderEditorController {
  suspend fun isOrderIdUnique(id: Int): Boolean
}

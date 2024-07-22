package com.kenkoro.taurus.api.client.controllers

import com.kenkoro.taurus.api.client.models.CutOrder
import com.kenkoro.taurus.api.client.models.NewCutOrder

interface CutOrderController {
  suspend fun addNewCutOrder(newCutOrder: NewCutOrder): CutOrder?

  suspend fun actualCutOrdersQuantity(id: Int): Int?
}

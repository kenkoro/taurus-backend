package com.kenkoro.taurus.api.client.services.dao

import com.kenkoro.taurus.api.client.models.CutOrder
import com.kenkoro.taurus.api.client.models.NewCutOrder

interface CutOrderDaoFacade {
  suspend fun addNewCutOrder(newCutOrder: NewCutOrder): CutOrder?

  suspend fun actualCutOrdersQuantity(id: Int): Int?
}

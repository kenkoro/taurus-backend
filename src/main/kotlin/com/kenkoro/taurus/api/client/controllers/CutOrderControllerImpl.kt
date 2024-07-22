package com.kenkoro.taurus.api.client.controllers

import com.kenkoro.taurus.api.client.models.CutOrder
import com.kenkoro.taurus.api.client.models.NewCutOrder
import com.kenkoro.taurus.api.client.services.dao.CutOrderDaoFacade
import com.kenkoro.taurus.api.client.services.dao.CutOrderDaoFacadeImpl

class CutOrderControllerImpl(
  private val dao: CutOrderDaoFacade = CutOrderDaoFacadeImpl(),
) : CutOrderController {
  override suspend fun addNewCutOrder(newCutOrder: NewCutOrder): CutOrder? =
    dao.addNewCutOrder(newCutOrder)

  override suspend fun actualCutOrdersQuantity(id: Int): Int? = dao.actualCutOrdersQuantity(id)
}

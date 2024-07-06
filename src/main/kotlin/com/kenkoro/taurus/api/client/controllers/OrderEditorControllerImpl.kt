package com.kenkoro.taurus.api.client.controllers

import com.kenkoro.taurus.api.client.services.dao.OrderEditorDaoFacade
import com.kenkoro.taurus.api.client.services.dao.OrderEditorDaoFacadeImpl

class OrderEditorControllerImpl(
  private val dao: OrderEditorDaoFacade = OrderEditorDaoFacadeImpl(),
) : OrderEditorController {
  override suspend fun isOrderIdUnique(id: Int): Boolean = dao.isOrderIdUnique(id)
}

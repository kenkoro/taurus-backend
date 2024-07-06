package com.kenkoro.taurus.api.client.services.dao

interface OrderEditorDaoFacade {
  suspend fun isOrderIdUnique(id: Int): Boolean
}

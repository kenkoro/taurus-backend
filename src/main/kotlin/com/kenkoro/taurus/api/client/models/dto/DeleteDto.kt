package com.kenkoro.taurus.api.client.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteDto(@SerialName("deleter_subject") val deleterSubject: String)

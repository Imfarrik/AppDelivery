package com.ccinc.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagsResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String
)
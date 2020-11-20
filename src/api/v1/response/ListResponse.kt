package com.xsc.api.v1.response

data class ListResponse<T>(
    val count: Int,
    val data: List<T>
)

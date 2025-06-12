package com.example.carspotter.domain.model

import java.time.Instant

data class Friend(
    val userId: Int,
    val friendId: Int,
    val createdAt: Instant? = null
)
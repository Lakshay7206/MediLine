package com.example.mediline.User.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
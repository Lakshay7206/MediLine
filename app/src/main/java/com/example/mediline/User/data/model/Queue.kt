package com.example.mediline.User.data.model

data class Queue (

    val queueLength:Int,
    val nowServing:Int
)

interface QueueRepository{
    suspend fun getQueue(deptId: String):Result<Int>

}
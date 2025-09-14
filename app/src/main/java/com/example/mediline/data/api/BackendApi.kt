package com.example.mediline.data.api

import retrofit2.http.Body
import retrofit2.http.POST

data class InviteRequest(val email: String)
data class AcceptInviteRequest(val token: String, val password: String)

interface BackendApi {
    @POST("admin/invite")
    suspend fun sendInvite(@Body request: InviteRequest)

    @POST("admin/accept")
    suspend fun acceptInvite(@Body request: AcceptInviteRequest)
}

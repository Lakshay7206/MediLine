package com.example.mediline.data.repo

import com.example.mediline.data.api.AcceptInviteRequest
import com.example.mediline.data.api.BackendApi
import com.example.mediline.data.api.InviteRequest


interface AdminRepository {
    suspend fun sendInvite(email: String): Result<Unit>
    suspend fun acceptInvite(token: String, password: String): Result<Unit>
}
class AdminRepositoryImpl(
    private val backendApi: BackendApi
) : AdminRepository {

    override suspend fun sendInvite(email: String): Result<Unit> {
        return try {
            backendApi.sendInvite(InviteRequest(email))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun acceptInvite(token: String, password: String): Result<Unit> {
        return try {
            backendApi.acceptInvite(AcceptInviteRequest(token, password))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

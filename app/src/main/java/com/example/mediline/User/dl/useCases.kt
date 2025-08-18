package com.example.mediline.User.dl

import android.app.Activity
import javax.inject.Inject


class SendOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String, activity: Activity): Result<String> {
        return repository.sendOtp(phone, activity)
    }
}

class VerifyOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(verificationId: String, otp: String): Result<String> {
        return repository.verifyOtp(verificationId, otp)
    }
}

class CheckUserExistsUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(uid: String): Result<Boolean> {
        return repository.checkUserExists(uid)
    }
}

class CreateUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> {
        return repository.createUser(user)
    }
}
package com.example.mediline.User.dl

import android.app.Activity
import com.example.mediline.User.data.model.AuthRepository
import com.example.mediline.User.data.model.Form
import com.example.mediline.User.data.model.FormRepository
import com.example.mediline.User.data.model.PaymentRepository
import com.example.mediline.User.data.model.User
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


class AddFormUseCase @Inject constructor(
    private val repository: FormRepository
){
    suspend operator fun invoke(form: Form): Result<String> {
        return repository.addForm(form)
    }    }

class CreatePaymentOrderUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(amount: Int, currency: String = "INR"): Result<String> {
        return paymentRepository.createOrder(amount, currency)
    }
}
class VerifyPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(orderId: String, paymentId: String, signature: String): Result<Boolean> {
        return paymentRepository.verifyPayment(orderId, paymentId, signature)

    }
}
package com.example.mediline.dl

import android.app.Activity
import com.example.mediline.data.model.AuthRepository
import com.example.mediline.data.model.Department
import com.example.mediline.data.model.DepartmentRepository
import com.example.mediline.data.model.Form
import com.example.mediline.data.model.FormRepository
import com.example.mediline.data.model.PaymentRepository
import com.example.mediline.data.model.PaymentStatus
import com.example.mediline.data.model.QueueRepository
import com.example.mediline.data.model.TicketRepository
import com.example.mediline.data.model.TicketStatus
import com.example.mediline.data.model.User
import com.example.mediline.data.repo.AdminTicketRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SendOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String, activity: Activity): Result<String> {
        return withContext(Dispatchers.IO){ repository.sendOtp(phone, activity) }
    }
}

class VerifyOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(verificationId: String, otp: String): Result<String> {
        return withContext(Dispatchers.IO){ repository.verifyOtp(verificationId, otp) }
    }
}

class CheckUserExistsUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(uid: String): Result<Boolean> {
        return withContext(Dispatchers.IO){ repository.checkUserExists(uid) }
    }
}

class CreateUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> {
        return withContext(Dispatchers.IO){ repository.createUser(user) }
    }
}


class AddFormUseCase @Inject constructor(
    private val repository: FormRepository
){
    suspend operator fun invoke(form: Form): Result<String> {
        return withContext(Dispatchers.IO){ repository.addForm(form) }
    }    }



    class CreateDepartmentUseCase @Inject constructor(
        private val repository: DepartmentRepository
    )
    {
        suspend operator fun invoke(): Flow<List<Department>>{
            return withContext(Dispatchers.IO){ repository.getDepartments() }
        }
    }

    class GetDepartmentIdUseCase @Inject constructor(
        private val repository: DepartmentRepository
    )
    {
        suspend operator fun invoke(id: String): Department?{
            return withContext(Dispatchers.IO){ repository.getDepartmentById(id) }
        }
    }

class GetQueueLengthUseCase @Inject constructor(
    private val repository: QueueRepository
) {
    suspend operator fun invoke(deptId: String): Result<Int> {
        return withContext(Dispatchers.IO){repository.getQueue(deptId)}
    }
}



class CreateOrderUseCase @Inject constructor(private val repo: PaymentRepository) {
    suspend operator fun invoke(amount: Int, currency: String) = withContext(Dispatchers.IO){repo.createOrder(amount, currency)}
}

class VerifyPaymentUseCase @Inject constructor(private val repo: PaymentRepository) {
    suspend operator fun invoke(orderId: String, paymentId: String, signature: String) =
        withContext(Dispatchers.IO) { repo.verifyPayment(orderId, paymentId, signature) }
}


class GetTicketsUseCase @Inject constructor(
    private val repository: TicketRepository
) {
    suspend operator fun invoke(): Result<List<Form>> {
        return repository.getTickets()
    }}


class UpdateTicketStatusUseCase @Inject constructor(
    private val repository: AdminTicketRepository

){
    suspend operator fun invoke(ticketId: String, status: TicketStatus): Result<Unit> {
        return repository.updateTicketStatus(ticketId, status)
    }

}
class GetAllTicketsUseCase @Inject constructor(
    private val repository: AdminTicketRepository
){
    suspend operator fun invoke(): Result<List<Form>> {
        return repository.getAllTickets()
    }

}

class UpdatePaymentStatusUseCase @Inject constructor(
    private val repository: AdminTicketRepository
){
    suspend operator fun invoke(ticketId: String, status: PaymentStatus): Result<Unit> {
        return repository.updatePaymentStatus(ticketId, status)
    }
}
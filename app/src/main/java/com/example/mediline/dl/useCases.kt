package com.example.mediline.dl

import android.app.Activity
import android.content.Context
import android.net.Uri
import com.example.mediline.data.model.AdminFormRepository
import com.example.mediline.data.model.AdminProfile
import com.example.mediline.data.model.AdminProfileRepository

import com.example.mediline.data.model.Department
import com.example.mediline.data.model.DepartmentRepository
import com.example.mediline.data.model.Form
import com.example.mediline.data.model.FormRepository
import com.example.mediline.data.model.PaymentRepository
import com.example.mediline.data.model.PaymentStatus

import com.example.mediline.data.model.TicketRepository
import com.example.mediline.data.model.TicketStatus
import com.example.mediline.data.model.User
import com.example.mediline.data.repo.AdminAuthRepository
import com.example.mediline.data.repo.AdminRepository
import com.example.mediline.data.repo.AdminTicketRepository
import com.example.mediline.data.repo.AuthRepository
import com.example.mediline.data.repo.DownloadTicketRepo
import com.example.mediline.data.repo.OtpData
import com.example.mediline.data.repo.QueueRepository
import com.example.mediline.data.room.DepartmentEntity
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


class SendOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String, activity: Activity): Result<OtpData> {
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
        return withContext(Dispatchers.IO) { repository.checkUserExists(uid) }
    }
}


class ResendOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        phone: String,
        activity: Activity,
        resendToken: PhoneAuthProvider.ForceResendingToken
    ): Result<OtpData> {
        return withContext(Dispatchers.IO) { repository.resendOtp(phone, activity, resendToken) }
    }
}




    class CreateUserUseCase @Inject constructor(
        private val repository: AuthRepository
    ) {
        suspend operator fun invoke(user: User): Result<Unit> {
            return withContext(Dispatchers.IO) { repository.createUser(user) }
        }
    }


class AddFormUseCase @Inject constructor(
    private val repository: FormRepository
){
    suspend operator fun invoke(form: Form): Result<String> {
        return withContext(Dispatchers.IO){ repository.addForm(form) }
    }    }

class GetDepartmentByIdUseCase @Inject constructor(private val repository: DepartmentRepository) {
    operator fun invoke(id: String): Flow<DepartmentEntity?> = repository.getDepartments()
        .map { list -> list.find { it.id == id } }
}


    class CreateDepartmentUseCase @Inject constructor(private val repository: DepartmentRepository) {
        suspend operator fun invoke(department: DepartmentEntity) {
            repository.createDepartment(department)
        }
    }

class GetDepartmentsUseCase @Inject constructor(
    private val repository: DepartmentRepository
) {
    operator fun invoke(): Flow<List<DepartmentEntity>> {
        return repository.getDepartments()
    }
}



class UpdateDepartmentUseCase @Inject constructor(private val repository: DepartmentRepository) {
    suspend operator fun invoke(department: DepartmentEntity): Result<Unit> =
        repository.updateDepartment(department)
}

class DeleteDepartmentUseCase @Inject constructor(private val repository: DepartmentRepository) {
    suspend operator fun invoke(departmentId: String): Result<Unit> =
        repository.deleteDepartment(departmentId)
}

class SyncDepartmentsUseCase @Inject constructor(private val repository: DepartmentRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.syncDepartmentsFromFirestore()
}



    class CreateOrderUseCase @Inject constructor(private val repo: PaymentRepository) {
        suspend operator fun invoke(amount: Int, currency: String) =
            withContext(Dispatchers.IO) { repo.createOrder(amount, currency) }
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
        }
    }




    class UpdateTicketStatusUseCase @Inject constructor(
        private val repository: AdminTicketRepository

    ) {
        suspend operator fun invoke(ticketId: String, status: TicketStatus): Result<Unit> {
            return repository.updateTicketStatus(ticketId, status)
        }

    }

    class GetAllTicketsUseCase @Inject constructor(
        private val repository: AdminTicketRepository
    ) {
        suspend operator fun invoke(): Result<List<Form>> {
            return repository.getAllTickets()
        }

}

    class UpdatePaymentStatusUseCase @Inject constructor(
        private val repository: AdminTicketRepository
    ) {
        suspend operator fun invoke(ticketId: String, status: PaymentStatus): Result<Unit> {
            return repository.updatePaymentStatus(ticketId, status)
        }
    }


    class AdminAddFormUseCase @Inject constructor(
        private val repository: AdminFormRepository
    ) {
        suspend operator fun invoke(form: Form, userId: String?): Result<String> {
            return repository.addTicketForUser(form, userId)
        }
    }

    class InviteAdminUseCase @Inject constructor(
        private val repository: AdminRepository
    ) {
        suspend operator fun invoke(email: String): Result<Unit> {
            return repository.sendInvite(email)
        }
    }

    class AcceptInviteUseCase @Inject constructor(
        private val repository: AdminRepository
    ) {
        suspend operator fun invoke(token: String, password: String): Result<Unit> {
            return repository.acceptInvite(token, password)
        }
    }

    class LoginAdminUseCase @Inject constructor(
        private val repository: AdminAuthRepository
    ) {
        suspend operator fun invoke(email: String, password: String): Result<Unit> {
            return repository.loginAdmin(email, password)
        }
    }

    class GetTodaysTicketsUseCase @Inject constructor(
        private val repository: QueueRepository
    ) {
        operator fun invoke(departmentId: String): Flow<List<Form>> {
            return repository.getTodaysTickets(departmentId)
        }
    }

    class generatePdfUseCase @Inject constructor(
        private val repository: DownloadTicketRepo
    ) {
        operator fun invoke(context: Context, ticket: Form, doctors: List<String>): File {
            return repository.generateTicketPdf(context, ticket, doctors)
        }
    }

class UserUpdatePaymentStatusUseCase @Inject constructor(
    private val repository: FormRepository
){
    suspend operator fun invoke(formId: String, status: String): Result<Unit> {
        return repository.updatePaymentStatus(formId, status)
    }
}



// Use case to load admin profile
class LoadAdminProfileUseCase @Inject constructor(
    private val repository: AdminProfileRepository
) {
    suspend operator fun invoke(uid: String): AdminProfile? = repository.loadProfile(
        uid = uid
    )
}

// Use case to update admin profile
class UpdateAdminProfileUseCase @Inject constructor(
    private val repository: AdminProfileRepository
) {
    suspend operator fun invoke(profile: AdminProfile) {
        repository.saveProfile(profile)
    }
}

class GetAllAdminsUseCase @Inject constructor(
    private val repository: AdminProfileRepository
) {
    suspend operator fun invoke(): Flow<List<AdminProfile>> = repository.getAllAdmins()
}

class DeleteAdminUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(adminId: String) {
        repository.deleteAdmin(adminId)
    }
}


class AssignTicketUseCase(
    private val repository: FormRepository
)  {

   suspend operator  fun invoke(formId: String): Result<Long?> {
        return repository.assignTicketNumber(formId)
    }
}

class AdminSignOutUseCase(
    private val repository: AdminAuthRepository

){
    operator fun invoke(): Result<Unit> {
        return repository.logoutAdmin()
    }
}



package com.example.mediline.User.data.repo

// data/repository/UserRepository.kt



import android.app.Activity
import com.example.mediline.User.data.model.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

interface UserRepository {
    suspend fun userExistsByPhone(phoneE164: String): Result<Boolean>
    suspend fun sendOtp(phoneE164: String, activity: Activity): Result<String> // verificationId
    suspend fun verifyOtp(verificationId: String, otp: String, nameIfNew: String?): Result<Pair<User, Boolean>>
}

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserRepository {

    // 1) Check existence by querying phone (before OTP)
    override suspend fun userExistsByPhone(phoneE164: String): Result<Boolean> = try {
        val snap = firestore.collection("users")
            .whereEqualTo("phone", phoneE164)
            .limit(1)
            .get()
            .await()
        Result.success(!snap.isEmpty)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // 2) Send OTP
    override suspend fun sendOtp(phoneE164: String, activity: Activity): Result<String> {
        return suspendCancellableCoroutine { cont ->
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneE164)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // Auto-retrieval; you *can* sign in directly, but we keep UI consistent.
                        // If you want instant sign-in, call auth.signInWithCredential(...) here.
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        if (cont.isActive) cont.resume(Result.failure(e))
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        if (cont.isActive) cont.resume(Result.success(verificationId))
                    }
                })
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    // 3) Verify OTP → sign in → if new create user, else return existing
    override suspend fun verifyOtp(
        verificationId: String,
        otp: String,
        nameIfNew: String?
    ): Result<Pair<User, Boolean>> = try {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        val authResult = auth.signInWithCredential(credential).await()
        val uid = authResult.user?.uid ?: return Result.failure(IllegalStateException("No UID"))
        val phone = authResult.user?.phoneNumber ?: ""

        val docRef = firestore.collection("users").document(uid)
        val doc = docRef.get().await()

        if (doc.exists()) {
            val existing = doc.toObject(User::class.java) ?: User(id = uid, phone = phone)
            Result.success(existing to false) // existing user
        } else {
            // Create new user record
            val user = User(
                id = uid,
                name = nameIfNew.orEmpty(),
                phone = phone
            )
            val data = mapOf(
                "id" to user.id,
                "name" to user.name,
                "phone" to user.phone,
                "createdAt" to FieldValue.serverTimestamp()
            )
            docRef.set(data).await()
            Result.success(user to true) // new user
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}




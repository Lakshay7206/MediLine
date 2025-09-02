package com.example.mediline.data.repo

import android.app.Activity
import com.example.mediline.data.model.User
import com.example.mediline.data.model.UserEntity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

data class OtpData(
    val verificationId: String,
    val resendToken: PhoneAuthProvider.ForceResendingToken? = null
)

interface AuthRepository {
    suspend fun sendOtp(phone: String,activity: Activity): Result<OtpData>
    suspend fun verifyOtp(verificationId: String, otp: String): Result<String> // returns uid
    suspend fun checkUserExists(uid: String): Result<Boolean>
    suspend fun createUser(user: User): Result<Unit>

    suspend fun resendOtp(phone: String,activity: Activity,resendToken: PhoneAuthProvider.ForceResendingToken): Result<OtpData>
}

class AuthRepositoryImpl(private val auth: FirebaseAuth,
                         private val firestore: FirebaseFirestore,

): AuthRepository{
//    override suspend fun sendOtp(phone: String, activity: Activity): Result<String> {
//        return try {
//            suspendCancellableCoroutine { cont ->
//
//                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                        // Auto-retrieval of OTP (rare, but possible)
//                        if (cont.isActive) {
//                            cont.resume(Result.success("AUTO_VERIFIED"))
//                        }
//                    }
//
//                    override fun onVerificationFailed(p0: FirebaseException) {
//                        if (cont.isActive) {
//                            cont.resume(Result.failure(p0))
//                        }
//                    }
//
//                    override fun onCodeSent(
//                        verificationId: String,
//                        token: PhoneAuthProvider.ForceResendingToken
//                    ) {
//                        if (cont.isActive) {
//                            // return verificationId so it can be used later in verifyOtp()
//                            cont.resume(Result.success(verificationId))
//                        }
//                    }
//                }
//
//                val options = PhoneAuthOptions.newBuilder(auth)
//                    .setPhoneNumber(phone)
//                    .setTimeout(60L, TimeUnit.SECONDS)
//                    .setActivity(activity)
//                    .setCallbacks(callbacks)
//                    .build()
//                PhoneAuthProvider.verifyPhoneNumber(options)
//
//
//            }
//        }catch(e: Exception){
//            Result.failure(e)
//
//        }
//
//    }

    override suspend fun sendOtp(phone: String, activity: Activity): Result<OtpData> {
        return try {
            suspendCancellableCoroutine { cont ->

                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        if (cont.isActive) {
                            cont.resume(Result.success(OtpData("AUTO_VERIFIED", null)))
                        }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        if (cont.isActive) {
                            cont.resume(Result.failure(e))
                        }
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        if (cont.isActive) {
                            // 🔑 return both verificationId + resendToken
                            cont.resume(Result.success(OtpData(verificationId, token)))
                        }
                    }
                }

                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phone)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(callbacks)
                    .build()

                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun resendOtp(
        phone: String,
        activity: Activity,
        resendToken: PhoneAuthProvider.ForceResendingToken
    ): Result<OtpData> {
        return try {
            suspendCancellableCoroutine { cont ->

                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        if (cont.isActive) {
                            cont.resume(Result.success(OtpData("AUTO_VERIFIED", null)))
                        }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        if (cont.isActive) {
                            cont.resume(Result.failure(e))
                        }
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        if (cont.isActive) {
                            cont.resume(Result.success(OtpData(verificationId, token)))
                        }
                    }
                }

                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phone)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(callbacks)
                    .setForceResendingToken(resendToken) // 🔑 important
                    .build()

                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(verificationId: String, otp: String): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            val credential: PhoneAuthCredential =
                PhoneAuthProvider.getCredential(verificationId, otp)

            auth.signInWithCredential(credential)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid
                    if (uid != null) {
                        continuation.resume(Result.success(uid))
                    } else {
                        continuation.resume(Result.failure(Exception("UID is null")))
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resume(Result.failure(e))
                }
        }

    }
    override suspend fun checkUserExists(uid: String): Result<Boolean> {
        return suspendCancellableCoroutine { continuation ->
            firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    continuation.resume(Result.success(snapshot.exists()))
                }
                .addOnFailureListener { e ->
                    continuation.resume(Result.failure(e))
                }
        }

    }
        override suspend fun createUser(user: User): Result<Unit> {
            // Convert domain User → Firebase-friendly UserEntity
            val entity = UserEntity(
                id = user.id,
                phone = user.phone,
                createdAt = user.createdAt,
               // role = user.role.name // Enum → String
                role ="USER"
            )

            return suspendCancellableCoroutine { continuation ->
                firestore.collection("users")
                    .document(user.id)
                    .set(entity)
                    .addOnSuccessListener {
                        continuation.resume(Result.success(Unit))

                    }
                    .addOnFailureListener { e ->
                        continuation.resume(Result.failure(e))
                    }
            }

        }



}
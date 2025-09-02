package com.example.mediline.dl


import android.content.Context
import androidx.room.Room
import com.example.mediline.data.api.BackendApi
import com.example.mediline.data.api.PaymentApi
import com.example.mediline.data.model.DepartmentRepository
import com.example.mediline.data.model.FormRepository
import com.example.mediline.data.model.PaymentRepository
import com.example.mediline.data.model.TicketRepository
import com.example.mediline.data.repo.AdminAuthRepository
import com.example.mediline.data.repo.AdminAuthRepositoryImpl
import com.example.mediline.data.repo.AdminRepository
import com.example.mediline.data.repo.AdminRepositoryImpl
import com.example.mediline.data.repo.AdminTicketRepository
import com.example.mediline.data.repo.AdminTicketRepositoryImpl
import com.example.mediline.data.repo.AuthRepository
import com.example.mediline.data.repo.AuthRepositoryImpl
import com.example.mediline.data.repo.DepartmentRepositoryImpl
import com.example.mediline.data.repo.DownloadTicketRepo
import com.example.mediline.data.repo.DownloadTicketRepoImpl

import com.example.mediline.data.repo.FormRepositoryImpl

import com.example.mediline.data.repo.PaymentRepositoryImpl
import com.example.mediline.data.repo.QueueRepository
import com.example.mediline.data.repo.QueueRepositoryImpl
import com.example.mediline.data.repo.TicketRepositoryImpl
import com.example.mediline.data.room.AppDatabase
import com.example.mediline.data.room.DepartmentDao

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDepartmentDao(appDatabase: AppDatabase): DepartmentDao {
        return appDatabase.departmentDao() // Assumes AppDatabase has abstract fun departmentDao()
    }



    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): AuthRepository =
        AuthRepositoryImpl(auth, firestore)

    @Provides
    @Singleton
    fun provideFormRepository(firestore: FirebaseFirestore, auth: FirebaseAuth): FormRepository =
        FormRepositoryImpl(firestore, auth)


    @Provides
    @Singleton
    fun provideDepartmentRepository(
        db: FirebaseFirestore,
        departmentDao: DepartmentDao
    ): DepartmentRepository = DepartmentRepositoryImpl(
        db,
        departmentDao = departmentDao
    )


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://54a0d1b1c5fc.ngrok-free.app/") // 🔥 use your backend URL
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePaymentApi(retrofit: Retrofit): PaymentApi =
        retrofit.create(PaymentApi::class.java)

    @Provides
    @Singleton
    fun providePaymentRepository(api: PaymentApi): PaymentRepository =
        PaymentRepositoryImpl(api)


    @Provides
    @Singleton
    fun provideTicketRepository(
        db: FirebaseFirestore,
        auth: FirebaseAuth
    ): TicketRepository = TicketRepositoryImpl(
        db,
        auth
    )

    @Provides
    @Singleton
    fun provideAdminTicketRepository(
        db: FirebaseFirestore
    ): AdminTicketRepository = AdminTicketRepositoryImpl(db)


    @Provides
    @Singleton
    fun provideAdminFormRepository(
        db: FirebaseFirestore,
        auth: FirebaseAuth
    ): com.example.mediline.data.model.AdminFormRepository =
        com.example.mediline.data.repo.AdminFormRepositoryImpl(db, auth)

    @Provides
    @Singleton
    fun provideBackendApi(retrofit: Retrofit): BackendApi = retrofit.create(BackendApi::class.java)

    @Provides
    @Singleton
    fun provideAdminRepository(backendApi: BackendApi): AdminRepository =
        AdminRepositoryImpl(backendApi)


    @Provides
    @Singleton
    fun provideAdminAuthRepository(
        auth: FirebaseAuth
    ): AdminAuthRepository = AdminAuthRepositoryImpl(auth)

    @Provides
    @Singleton
    fun provideQueueRepository(
        db: FirebaseFirestore
    ): QueueRepository = QueueRepositoryImpl(db)


    @Provides
    @Singleton
    fun provideDownloadTicketRepo(): DownloadTicketRepo= DownloadTicketRepoImpl()
}
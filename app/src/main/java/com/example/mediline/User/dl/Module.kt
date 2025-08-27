package com.example.mediline.User.dl


import com.example.mediline.User.data.PaymentApi
import com.example.mediline.User.data.model.AuthRepository
import com.example.mediline.User.data.model.DepartmentRepository
import com.example.mediline.User.data.model.FormRepository
import com.example.mediline.User.data.model.PaymentRepository
import com.example.mediline.User.data.model.QueueRepository
import com.example.mediline.User.data.model.TicketRepository
import com.example.mediline.User.data.repo.AuthRepositoryImpl
import com.example.mediline.User.data.repo.DepartmentRepositoryImpl
import com.example.mediline.User.data.repo.FormRepositoryImpl
import com.example.mediline.User.data.repo.PaymentRepositoryImpl
import com.example.mediline.User.data.repo.QueueRepositoryImpl
import com.example.mediline.User.data.repo.TicketRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): AuthRepository =
        AuthRepositoryImpl(auth, firestore)

    @Provides @Singleton
    fun provideFormRepository(firestore: FirebaseFirestore,auth: FirebaseAuth): FormRepository =
        FormRepositoryImpl(firestore, auth)


    @Provides
    @Singleton
    fun provideDepartmentRepository(
        db: FirebaseFirestore
    ): DepartmentRepository = DepartmentRepositoryImpl(db)

    @Provides
    @Singleton
    fun provideQueueRepository(
        db: FirebaseFirestore
    ): QueueRepository = QueueRepositoryImpl(db)





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
            .baseUrl("https://6d943c3bd39d.ngrok-free.app/") // 🔥 use your backend URL
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
    ): TicketRepository =TicketRepositoryImpl(
            db,
            auth
        )



    @Provides
    @Singleton
    fun provideGetTicketsUseCase(
        repository: TicketRepository
    ): GetTicketsUseCase {
        return GetTicketsUseCase(repository)
    }


    }



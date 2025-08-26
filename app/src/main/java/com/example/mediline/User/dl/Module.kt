package com.example.mediline.User.dl


import com.example.mediline.User.data.model.AuthRepository
import com.example.mediline.User.data.model.DepartmentRepository
import com.example.mediline.User.data.model.FormRepository
import com.example.mediline.User.data.model.PaymentRepository
import com.example.mediline.User.data.model.QueueRepository
import com.example.mediline.User.data.repo.AuthRepositoryImpl
import com.example.mediline.User.data.repo.DepartmentRepositoryImpl
import com.example.mediline.User.data.repo.FormRepositoryImpl
import com.example.mediline.User.data.repo.PaymentRepositoryImpl
import com.example.mediline.User.data.repo.QueueRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

    @Provides @Singleton
    fun providePaymentRepository(): PaymentRepository = PaymentRepositoryImpl()

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
    fun provideDepartmentIdRepository(
        db: FirebaseFirestore
    ): DepartmentRepository = DepartmentRepositoryImpl(db)

}
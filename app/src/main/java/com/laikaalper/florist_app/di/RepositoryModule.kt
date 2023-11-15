package com.laikaalper.florist_app.di

import com.google.firebase.auth.FirebaseAuth
import com.laikaalper.florist_app.data.repository.AuthRepository
import com.laikaalper.florist_app.data.repository.ProductRepository
import com.laikaalper.florist_app.data.source.local.ProductDao
import com.laikaalper.florist_app.data.source.remote.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductsRepository(productService: ProductService, productDao: ProductDao) =
        ProductRepository(productService, productDao)

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth) = AuthRepository(firebaseAuth)

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface RepositoryModuleInterface {
        fun getAuthRepo(): AuthRepository
    }
}
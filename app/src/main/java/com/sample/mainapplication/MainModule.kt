package com.sample.mainapplication

import com.sample.mainapplication.networking.MainRemoteDataSource
import com.sample.mainapplication.ui.main.MainRepository
import com.sample.mainapplication.ui.main.MainViewModel
import com.sample.mainapplication.ui.login.AuthRepository
import com.sample.mainapplication.ui.login.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    @Singleton
    fun provideMainFragmentViewModel(
        mainRepository: MainRepository,
    ) = MainViewModel(mainRepository)

    @Provides
    @Singleton
    fun provideMainRepository(
        mainRemoteDataSource: MainRemoteDataSource,
    ) = MainRepository(mainRemoteDataSource.mainService)

    @Provides
    @Singleton
    fun provideMainService() = MainRemoteDataSource()

    @Provides
    @Singleton
    fun provideLoginViewModel(
        authRepository: AuthRepository,
    ) = LoginViewModel(authRepository)

    @Provides
    @Singleton
    fun provideAuthRepository() = AuthRepository()
}
package com.sample.mainapplication

import com.sample.mainapplication.model.ChatRepository
import com.sample.mainapplication.networking.MainRemoteDataSource
import com.sample.mainapplication.ui.chat.ChatViewModel
import com.sample.mainapplication.ui.main.MainRepository
import com.sample.mainapplication.ui.main.MainViewModel
import com.sample.mainapplication.ui.login.AuthRepository
import com.sample.mainapplication.ui.login.LoginViewModel
import com.sample.mainapplication.ui.login.SignupViewModel
import com.sample.mainapplication.ui.profile.ProfileViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    fun provideMainFragmentViewModel(
        mainRepository: MainRepository,
    ) = MainViewModel(mainRepository)

    @Provides
    fun provideMainRepository(
        mainRemoteDataSource: MainRemoteDataSource,
    ) = MainRepository(mainRemoteDataSource.mainService)

    @Provides
    fun provideMainService() = MainRemoteDataSource()

    @Provides
    fun provideLoginViewModel(
        authRepository: AuthRepository,
    ) = LoginViewModel(authRepository)

    @Provides
    fun provideSignupViewModel(
        authRepository: AuthRepository,
    ) = SignupViewModel(authRepository)

    @Provides
    fun provideProfileViewModel(
        authRepository: AuthRepository,
    ) = ProfileViewModel(authRepository)

    @Provides
    @Singleton
    fun provideAuthRepository() = AuthRepository()

    @Provides
    fun provideChatViewModel(
        chatRepository: ChatRepository,
    ) = ChatViewModel(chatRepository)

    @Provides
    fun provideChatRepository(
        authRepository: AuthRepository,
    ) = ChatRepository(authRepository.user)
}
package com.sample.mainapplication

import com.sample.mainapplication.model.MessageRepository
import com.sample.mainapplication.networking.MainRemoteDataSource
import com.sample.mainapplication.ui.message.MessageViewModel
import com.sample.mainapplication.model.PokemonRepository
import com.sample.mainapplication.ui.first.FirstFragmentViewModel
import com.sample.mainapplication.model.AuthRepository
import com.sample.mainapplication.model.ChatroomRepository
import com.sample.mainapplication.networking.FirebaseDataSource
import com.sample.mainapplication.ui.chatroom.ChatroomViewModel
import com.sample.mainapplication.ui.login.LoginViewModel
import com.sample.mainapplication.ui.login.SignupViewModel
import com.sample.mainapplication.ui.profile.ProfileViewModel
import com.sample.mainapplication.ui.second.SecondFragmentViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    fun provideFirstFragmentViewModel(
        pokemonRepository: PokemonRepository,
    ) = FirstFragmentViewModel(pokemonRepository)

    @Provides
    fun provideSecondFragmentViewModel(
        pokemonRepository: PokemonRepository,
    ) = SecondFragmentViewModel(pokemonRepository)

    @Provides
    fun provideMainRepository(
        mainRemoteDataSource: MainRemoteDataSource,
    ) = PokemonRepository(mainRemoteDataSource.mainService)

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
    fun provideAuthRepository(
        firebaseDataSource: FirebaseDataSource,
    ) = AuthRepository(
        firebaseDataSource.auth,
        firebaseDataSource.userId,
        firebaseDataSource.imageStorageRef,
        firebaseDataSource.userDatabaseRef,
    )

    @Provides
    fun provideChatViewModel(
        messageRepository: MessageRepository,
    ) = MessageViewModel(messageRepository)

    @Provides
    fun provideMessageRepository(
        authRepository: AuthRepository,
        firebaseDataSource: FirebaseDataSource,
    ) = MessageRepository(
        authRepository.user,
        firebaseDataSource.chatroomDatabaseRef,
    )

    @Provides
    fun provideChatroomViewModel(
        chatroomRepository: ChatroomRepository,
    ) = ChatroomViewModel(chatroomRepository)

    @Provides
    @Singleton
    fun provideFirebaseDataSource() = FirebaseDataSource()

    @Provides
    fun provideChatromRepository(
        firebaseDataSource: FirebaseDataSource,
    ) = ChatroomRepository(
        firebaseDataSource.chatroomDatabaseRef,
    )
}
package com.sample.mainapplication

import com.sample.mainapplication.networking.MainRemoteDataSource
import com.sample.mainapplication.ui.MainRepository
import com.sample.mainapplication.ui.MainViewModel
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
}
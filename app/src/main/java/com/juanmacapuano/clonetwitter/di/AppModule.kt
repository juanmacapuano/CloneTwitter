package com.juanmacapuano.clonetwitter.di

import com.juanmacapuano.clonetwitter.service.api.ApiSwagger
import com.juanmacapuano.clonetwitter.service.api.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApiSwagger(
        remoteDataSource: RemoteDataSource
    ) : ApiSwagger {
        return remoteDataSource.buildAPI(ApiSwagger::class.java)

    }
}
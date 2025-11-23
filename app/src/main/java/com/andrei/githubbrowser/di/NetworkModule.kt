package com.andrei.githubbrowser.di

import com.andrei.githubbrowser.data.sources.remote.GithubRepoService
import com.andrei.githubbrowser.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideGithubRepoApi(retrofit: Retrofit): GithubRepoService =
        retrofit.create(GithubRepoService::class.java)
}
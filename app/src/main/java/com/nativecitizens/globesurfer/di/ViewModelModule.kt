package com.nativecitizens.globesurfer.di

import com.nativecitizens.globesurfer.network.CountryApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @ViewModelScoped
    @Provides
    fun provideViewModelJob(): Job {
        return Job()
    }

    @ViewModelScoped
    @Provides
    fun provideCoroutineScope(viewModelJob: Job): CoroutineScope {
        return CoroutineScope(Dispatchers.Main + viewModelJob)
    }

    @ViewModelScoped
    @Provides
    fun provideCountryApiService(): CountryApiService {
        val baseUrl = "https://restcountries.com/v3.1/"

        val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(baseUrl)
            .build()

        return retrofit.create(CountryApiService::class.java)
    }
}
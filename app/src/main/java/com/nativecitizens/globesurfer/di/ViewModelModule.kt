package com.nativecitizens.globesurfer.di

import com.nativecitizens.globesurfer.network.CountryApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Qualifier


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

    @countryApiServiceWithMoshi
    @ViewModelScoped
    @Provides
    fun provideCountryApiServiceWithHilt(): CountryApiService {
        val baseUrl = "https://restcountries.com/v3.1/"

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(baseUrl)
            .build()

        return retrofit.create(CountryApiService::class.java)
    }

    @countryApiServiceWithScalar
    @ViewModelScoped
    @Provides
    fun provideCountryApiServiceWithScalar(): CountryApiService {
        val baseUrl = "https://restcountries.com/v3.1/"

        val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(baseUrl)
            .build()

        return retrofit.create(CountryApiService::class.java)
    }
}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class countryApiServiceWithMoshi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class countryApiServiceWithScalar
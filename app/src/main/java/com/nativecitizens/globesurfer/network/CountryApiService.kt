package com.nativecitizens.globesurfer.network


import com.nativecitizens.globesurfer.model.CountryName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


interface CountryApiService {
    @GET("all")
    fun getCountriesByName(): Call<List<CountryName>>

    @GET
    fun getCountryByName(@Url url: String?): Call<String>
}
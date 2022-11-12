package com.nativecitizens.globesurfer.network



import retrofit2.Call
import retrofit2.http.GET


interface CountryApiService {
    @GET("all")
    fun getCountryJsonString(): Call<String>
}
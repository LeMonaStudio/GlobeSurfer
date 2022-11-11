package com.nativecitizens.globesurfer.model



import android.app.Application
import androidx.lifecycle.ViewModel
import com.nativecitizens.globesurfer.di.countryApiServiceWithMoshi
import com.nativecitizens.globesurfer.di.countryApiServiceWithScalar
import com.nativecitizens.globesurfer.network.CountryApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject



@HiltViewModel
class SearchViewModel @Inject constructor(
    private val application: Application,
    private val uiScope: CoroutineScope,
    @countryApiServiceWithMoshi private val countryApiServiceMoshi: CountryApiService,
    @countryApiServiceWithScalar private val countryApiServiceScalar: CountryApiService): ViewModel() {

    private var _countryString = MutableStateFlow("")
    val countryString = _countryString.asStateFlow()

    private var countryNameSearchBaseUrl = "https://restcountries.com/v3.1/name/"


    init {
        getCountryJsonString()
    }


    private fun getCountryJsonString(){
        countryApiServiceMoshi.getCountriesByName().enqueue(object : Callback<List<CountryName>> {
            override fun onResponse(call: Call<List<CountryName>>, response: Response<List<CountryName>>) {
                if (response.body()?.isNotEmpty() == true){
                    //_countryString.value = "${response.body()?.get(0)?.name?.official}"
                    uiScope.launch {
                        getCountryDetailsByName("${response.body()?.get(50)?.name?.official}")
                    }
                }
            }
            override fun onFailure(call: Call<List<CountryName>>, t: Throwable) {
                _countryString.value = "Failure: ${t.message}"
            }
        })
    }


    private fun getCountryDetailsByName(countryName: String) {
        countryNameSearchBaseUrl += countryName

        countryApiServiceScalar.getCountryByName(countryNameSearchBaseUrl).enqueue(object : Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if (response.body()?.isNotEmpty() == true){
                    val jsonObject = JSONObject("${response.body()?.drop(1)?.dropLast(1)}")

                    val name = jsonObject.getJSONObject("name").getString("official")
                    val capital = jsonObject.getJSONArray("capital").getString(0)
                    val population = jsonObject.getInt("population")
                    val continent = jsonObject.getJSONArray("continents").getString(0)
                    val language = getCountryLanguages(jsonObject.getJSONObject("languages")).joinToString(", ")
                    val area = jsonObject.getDouble("area")
                    val currency = jsonObject.getJSONObject("currencies").getJSONObject("${jsonObject.getJSONObject("currencies").names()?.get(0)}").getString("name")
                    val timeZone = jsonObject.getJSONArray("timezones").getString(0)
                    val drivingSide = jsonObject.getJSONObject("car").getString("side").replaceFirstChar { char -> char.uppercase() }

                    val country = Country(name, capital, population, continent, language, area, currency, timeZone, drivingSide)
                    _countryString.value = country.toString()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _countryString.value = "Failure: ${t.message}"
            }
        })
    }


    private fun getCountryLanguages(languageJsonObject: JSONObject):List<String> {
        val numberOfLanguage: Int = languageJsonObject.names()?.length() ?: 0
        val countryLanguage = mutableListOf<String>()
        if (numberOfLanguage >= 1){
            for (i in 0..numberOfLanguage -1){
                countryLanguage.add(languageJsonObject.getString(languageJsonObject.names()?.get(i).toString()))
            }
        }

        return countryLanguage
    }
}
package com.nativecitizens.globesurfer.model



import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nativecitizens.globesurfer.R
import com.nativecitizens.globesurfer.network.CountryApiService
import com.nativecitizens.globesurfer.util.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject




@HiltViewModel
class SearchViewModel @Inject constructor(
    private val application: Application,
    private val uiScope: CoroutineScope,
    private val countryApiService: CountryApiService): ViewModel() {

    private var _countryListResponse = MutableLiveData<ResponseState<MutableList<Country>>>()
    val countryListResponse: LiveData<ResponseState<MutableList<Country>>> get() = _countryListResponse

    private var countryList: MutableList<Country> = mutableListOf()

    private var selectedLanguageCode = "en"

    private var timeZonesString = ""

    private var selectedFilterList: MutableList<String> = mutableListOf()




    init {
        _countryListResponse.value = ResponseState.Loading
        getCountriesJsonString()
    }


    private fun getCountriesJsonString(){
        uiScope.launch {
            countryApiService.getCountryJsonString().enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.body()?.isNotEmpty() == true){
                        val jsonArray = JSONArray(response.body())
                        fetchCountryList(jsonArray)
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    _countryListResponse.value = ResponseState.Error("Error: ${t.message}")
                }
            })
        }
    }


    private fun fetchCountryList(jsonArray: JSONArray) {
        uiScope.launch {
            val size = jsonArray.length()

            for (i in 0 until size){
                getCountryDetails(jsonArray.get(i).toString())
                if (i == size-1){
                    getTranslatedAndFilteredForDisplay()
                }
            }
        }
    }


    private suspend fun getCountryDetails(countryJsonString: String) {
        withContext(Dispatchers.IO) {

            val jsonObject = JSONObject(countryJsonString)

            try {
                val name = jsonObject.getJSONObject("name").getString("common") ?: ""
                val capital = jsonObject.getJSONArray("capital").getString(0) ?: ""
                val population = jsonObject.getInt("population") ?: 0
                val continent = jsonObject.getJSONArray("continents").getString(0) ?: ""
                val language = getCountryLanguages(jsonObject.getJSONObject("languages")).joinToString(", ") ?: ""
                val area = jsonObject.getDouble("area") ?: 0.0
                val currency = jsonObject.getJSONObject("currencies").getJSONObject("${jsonObject.getJSONObject("currencies").names()?.get(0)}").getString("name") ?: ""
                val timeZone = jsonObject.getJSONArray("timezones").getString(0) ?: ""
                val drivingSide = jsonObject.getJSONObject("car").getString("side").replaceFirstChar { char -> char.uppercase() } ?: ""
                val flagUrl = jsonObject.getJSONObject("flags").getString("png") ?: ""
                val translations: String = "en,$name:" + getTranslationsFromApi(jsonObject.getJSONObject("translations"))

                withContext(Dispatchers.Main) {
                    val country = Country(name, capital, population, continent, language, area, currency, timeZone, drivingSide, flagUrl, translations)
                    countryList.add(country)
                }

            } catch (exception: Exception){
                Log.i("Error", "${exception.message}")
            }

        }
    }


    private fun getCountryLanguages(languageJsonObject: JSONObject):List<String> {
        val numberOfLanguage: Int = languageJsonObject.names()?.length() ?: 0
        val countryLanguage = mutableListOf<String>()
        if (numberOfLanguage >= 1){
            for (i in 0 until numberOfLanguage){
                countryLanguage.add(languageJsonObject.getString(languageJsonObject.names()?.get(i).toString()))
            }
        }

        return countryLanguage
    }

    private fun getTranslationsFromApi(jsonObject: JSONObject): String {
        val languages: List<String> = application.resources.getStringArray(R.array.language_list).toList()
        var stringOfTranslations = ""

        languages.forEach { lang ->
            val code = lang.split(":")[1]
            if (code != "en"){
                val translation = jsonObject.getJSONObject(code).getString("official") ?: "No translation"
                stringOfTranslations += "$code,$translation:"
            }
        }
        return stringOfTranslations
    }





    fun getTimeZones(): String {
        countryList.forEachIndexed{index, country ->
            if (!timeZonesString.contains(country.timeZone.toString())){
                if (index != countryList.size -1){
                    timeZonesString += "${country.timeZone.toString()},"
                } else {
                    timeZonesString += country.timeZone.toString()
                }
            }
        }
        return timeZonesString
    }

    fun searchCountry(searchString: String){
        if (searchString.isNotEmpty()){
            val newList: MutableList<Country> = mutableListOf()
            countryList.forEach {
                if (it.name?.startsWith(searchString, true) == true){
                    newList.add(it)
                }
            }
            getTranslatedAndFilteredForDisplay(newList)
        } else {
            getTranslatedAndFilteredForDisplay()
        }
    }

    fun setFilter(filterString: String) {
        selectedFilterList = filterString.split(",").map { it.trim() }.toMutableList()
        getTranslatedAndFilteredForDisplay()
    }

    fun resetFilter() {
        selectedFilterList = mutableListOf()
        getTranslatedAndFilteredForDisplay()
    }



    fun setSelectedTranslation(languageCode: String) {
        selectedLanguageCode = languageCode

        val newList: MutableList<Country> = mutableListOf()
        countryList.forEach { country ->
            val listOfTranslations: List<String>? = country.translations?.split(":")?.map {theList ->
                theList.trim() }?.toList()

            listOfTranslations?.forEach { str ->
                if (str.startsWith(selectedLanguageCode)){
                    country.name = str.split(",")[1]
                }
            }
            newList.add(country)
        }
        getTranslatedAndFilteredForDisplay(newList)
    }


    private fun getTranslatedAndFilteredForDisplay(newList: MutableList<Country>? = null){
        val newFilteredList: MutableList<Country> = mutableListOf()

        if (selectedFilterList.isNotEmpty()) {
            if (newList != null){
                selectedFilterList.forEach { filter ->
                    newList.forEach { country ->
                        if (filter.startsWith("UTC")){
                            //Time zone filter
                            if (country.timeZone == filter) newFilteredList.add(country)
                        } else {
                            //Region or continent filter
                            if (country.continent == filter) newFilteredList.add(country)
                        }
                    }
                }
            } else {
                selectedFilterList.forEach { filter ->
                    countryList.forEach { country ->
                        if (filter.startsWith("UTC")){
                            //Time zone filter
                            if (country.timeZone == filter) newFilteredList.add(country)
                        } else {
                            //Region or continent filter
                            if (country.continent == filter) newFilteredList.add(country)
                        }
                    }
                }
            }
            _countryListResponse.value = ResponseState.Success(newFilteredList.distinct().sortedBy { it.name }.toMutableList())
        } else {
            _countryListResponse.value = if (newList != null)
                ResponseState.Success(newList) else ResponseState.Success(countryList)
        }
    }

    fun prepareForReconnection() {
        uiScope.launch {
            delay(4_000)
            _countryListResponse.value = ResponseState.Loading
            delay(2_000)
            getCountriesJsonString()
        }
    }
}
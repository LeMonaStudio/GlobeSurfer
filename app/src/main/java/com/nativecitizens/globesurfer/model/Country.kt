package com.nativecitizens.globesurfer.model


data class Country(
    var name: String,
    var capital: String,
    var population: Int,
    var continent: String,
    var officialLanguage: String,
    var area: Double,
    var currency: String,
    var timeZone: String,
    var drivingSize: String
)


//Fetch country name using CountryApiService
data class CountryName(
    var name: CountryNameJson
)

data class CountryNameJson(
    var official: String
)
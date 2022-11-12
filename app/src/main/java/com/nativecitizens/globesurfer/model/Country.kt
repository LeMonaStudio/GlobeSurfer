package com.nativecitizens.globesurfer.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep


@Keep
data class Country(
    var name: String?,
    var capital: String?,
    var population: Int,
    var continent: String?,
    var officialLanguage: String?,
    var area: Double,
    var currency: String?,
    var timeZone: String?,
    var drivingSide: String?,
    var flagUrl: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(capital)
        parcel.writeInt(population)
        parcel.writeString(continent)
        parcel.writeString(officialLanguage)
        parcel.writeDouble(area)
        parcel.writeString(currency)
        parcel.writeString(timeZone)
        parcel.writeString(drivingSide)
        parcel.writeString(flagUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Country> {
        override fun createFromParcel(parcel: Parcel): Country {
            return Country(parcel)
        }

        override fun newArray(size: Int): Array<Country?> {
            return arrayOfNulls(size)
        }
    }
}


data class LanguageList(
    var languages: MutableList<String>?
)
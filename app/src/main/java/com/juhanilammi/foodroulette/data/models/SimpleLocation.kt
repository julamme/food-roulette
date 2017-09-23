package com.juhanilammi.foodroulette.data.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Laemmi on 14.9.2017.
 */

data class SimpleLocation(var city: String?, var country: String?, var latitude: Double?, var longitude: Double?, var street: String?, var zip: String?, var located_in: Long?) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readString(),
            source.readString(),
            source.readValue(Long::class.java.classLoader) as Long?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(city)
        writeString(country)
        writeValue(latitude)
        writeValue(longitude)
        writeString(street)
        writeString(zip)
        writeValue(located_in)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SimpleLocation> = object : Parcelable.Creator<SimpleLocation> {
            override fun createFromParcel(source: Parcel): SimpleLocation = SimpleLocation(source)
            override fun newArray(size: Int): Array<SimpleLocation?> = arrayOfNulls(size)
        }
    }
}
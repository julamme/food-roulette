package com.juhanilammi.foodroulette.data.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Laemmi on 12.9.2017.
 */

data class SimpleFacebookLocation(var location: SimpleLocation, var name: String, var id: Long) : Parcelable {
    constructor(source: Parcel) : this(
            source.readParcelable<SimpleLocation>(SimpleLocation::class.java.classLoader),
            source.readString(),
            source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(location, 0)
        writeString(name)
        writeLong(id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SimpleFacebookLocation> = object : Parcelable.Creator<SimpleFacebookLocation> {
            override fun createFromParcel(source: Parcel): SimpleFacebookLocation = SimpleFacebookLocation(source)
            override fun newArray(size: Int): Array<SimpleFacebookLocation?> = arrayOfNulls(size)
        }
    }
}
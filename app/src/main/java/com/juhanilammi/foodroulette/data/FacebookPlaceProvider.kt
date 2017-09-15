package com.juhanilammi.foodroulette.data

import android.location.Location
import com.facebook.places.PlaceManager
import com.facebook.places.model.PlaceFields
import com.facebook.places.model.PlaceSearchRequestParams
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.juhanilammi.foodroulette.data.models.SimpleFacebookLocation
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.json.JSONArray
import org.json.JSONObject


class FacebookPlaceProvider {

    companion object {
        val JSON = jacksonObjectMapper()
        var places = mutableListOf<SimpleFacebookLocation>()
        var placesSubject: PublishSubject<MutableList<SimpleFacebookLocation>> = PublishSubject.create()

        operator fun JSONArray.iterator(): Iterator<JSONObject> = (0 until length()).asSequence().map { get(it) as JSONObject }.iterator()

        fun retrievePlaces(location: Location): Observable<MutableList<SimpleFacebookLocation>> {
            val builder = PlaceSearchRequestParams.Builder()
            builder.setSearchText("Restaurant")
            builder.setDistance(2000)
            builder.setLimit(20)
            builder.addField(PlaceFields.LOCATION)
            builder.addField(PlaceFields.NAME)

            val request = PlaceManager.newPlaceSearchRequestForLocation(builder.build(), location)

            request.setCallback { response ->
                if (response != null) {
                    for (item in response.jsonObject.getJSONArray("data").iterator()) {
                        val parsedObject = JSON.readValue<SimpleFacebookLocation>(item.toString())
                        places.add(parsedObject)
                    }
                    placesSubject.onNext(places)
                }
            }
            request.executeAsync()

            return placesSubject
        }
    }
}
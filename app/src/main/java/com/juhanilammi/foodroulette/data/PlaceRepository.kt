package com.juhanilammi.foodroulette.data

import android.location.Location
import android.util.Log
import com.juhanilammi.foodroulette.data.models.SimpleFacebookLocation
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*

/**
 * Todos: caching
 */
class PlaceRepository {

    companion object {

        fun retrievePlace(location: Location): Observable<SimpleFacebookLocation>{
            val publisher: PublishSubject<SimpleFacebookLocation> = PublishSubject.create()
            FacebookPlaceProvider.retrievePlaces(location).subscribe({ next ->
                Log.i("TESTING", next.toString())
                if (next.size >0) {
                    publisher.onNext(next.get(Random().nextInt(next.size)))
                    publisher.onComplete()
                }
            })
            return publisher
        }
    }
}

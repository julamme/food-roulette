package com.juhanilammi.foodroulette.data

import android.location.Location
import android.util.Log
import com.juhanilammi.foodroulette.data.models.SimpleFacebookLocation
import com.juhanilammi.foodroulette.utils.Randomizer
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.Callable

/**
 * Todos: caching
 */
class PlaceRepository {

    companion object {

        fun retrievePlace(location: Location): Observable<SimpleFacebookLocation> {

            var age = 0
            var publisher: PublishSubject<SimpleFacebookLocation> = PublishSubject.create()
            FacebookPlaceProvider.retrievePlaces(location).subscribe({ next ->
                Log.i("TESTING", next.toString())
                if (next.size >0) {
                    publisher.onNext(next.get(Random().nextInt(next.size)))
                }
            })
            return publisher
        }
    }
}

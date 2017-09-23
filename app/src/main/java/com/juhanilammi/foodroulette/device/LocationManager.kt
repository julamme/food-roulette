package com.juhanilammi.foodroulette.device

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.juhanilammi.foodroulette.R
import com.juhanilammi.foodroulette.utils.LocationNotAvailableException
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.io.IOError
import java.io.IOException

public class LocationManager private constructor()  {


    private object Holder {
        val INSTANCE = LocationManager()
    }

    companion object {
        val instance: LocationManager by lazy { Holder.INSTANCE }
    }

    lateinit var mGoogleApiClient: GoogleApiClient
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var context: Context
    var isConnected: Boolean = false
    val locationPublisher: BehaviorSubject<Location?> = BehaviorSubject.create()
    var currentLocation: Location? = null

    fun addContext(context: Context) {
        this.context = context
    }

    fun build() {
        mGoogleApiClient = GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build()

    }

    @SuppressLint("MissingPermission")
    fun retrieveLocationObservable(): Observable<Location?> {


        return Observable.create { e ->
            if (isConnected) {
                e.onNext(mFusedLocationClient.lastLocation.result)
            } else {
                mGoogleApiClient.registerConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(args: Bundle?) {
                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                        isConnected = true
                        mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                e.onNext(task.result)
                            }
                        }
                    }

                    override fun onConnectionSuspended(code: Int) {
                        e.onError(throw IOException("Connection suspended"))
                    }

                })
                mGoogleApiClient.registerConnectionFailedListener({ e.onError(throw IOException("connection failed")) })
                mGoogleApiClient.connect()
            }
        }
    }

}
package com.juhanilammi.foodroulette.device

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.juhanilammi.foodroulette.R
import com.juhanilammi.foodroulette.utils.LocationNotAvailableException
import io.reactivex.Observable

public class LocationManager private constructor() : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    override fun onConnected(p0: Bundle?) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        isConnected = true
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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

    /*
     * Check permission when calling instance of LocationManager
     */
    @SuppressLint("MissingPermission")
    val mLocationObservable: Observable<Location> = Observable.create({ e ->
        e.serialize()
        mFusedLocationClient.locationAvailability.addOnCompleteListener({
            availabilityTask ->
            if (availabilityTask.isSuccessful && availabilityTask.result.isLocationAvailable) {
                mFusedLocationClient.lastLocation.addOnCompleteListener({
                    lastLocationTask ->
                    if (lastLocationTask.isSuccessful) {
                        e.onNext(lastLocationTask.result)
                    } else {
                        e.onError(LocationNotAvailableException(context.getString(R.string.location_not_available)))
                    }
                })
            } else {
                e.onError(LocationNotAvailableException(context.getString(R.string.location_not_available)))
            }
        })
    })

    fun addContext(context: Context) {
        this.context = context
    }

    fun connect() {
        mGoogleApiClient = GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener { this }
                .build()
        mGoogleApiClient.connect()
    }

    @SuppressLint("MissingPermission")
    fun retrievLocationObservable(): Observable<Location> {
        return mLocationObservable
    }

}
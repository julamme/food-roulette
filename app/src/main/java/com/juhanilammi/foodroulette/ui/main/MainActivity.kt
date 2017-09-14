package com.juhanilammi.foodroulette.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.juhanilammi.foodroulette.R
import com.juhanilammi.foodroulette.data.FacebookPlaceProvider
import com.juhanilammi.foodroulette.device.LocationManager
import com.juhanilammi.foodroulette.ui.base.BaseActivity
import com.juhanilammi.foodroulette.ui.base.MvpView
import com.juhanilammi.foodroulette.utils.PermissionUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

public class MainActivity : BaseActivity<MainView, MainPresenter>(), MainView {
    override var mPresenter: MainPresenter = MainPresenter(this)


    lateinit var mMap: GoogleMap
    val permissionUtils: PermissionUtils = PermissionUtils()
    val LOCATION_PERMISSION_REQUEST_CODE: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val supportFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportFragment.getMapAsync({ googleMap: GoogleMap ->
            mMap = googleMap
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, true)
            } else {
                LocationManager.instance.connect()

            }
            mMap.setOnMyLocationButtonClickListener({ ->
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    permissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, true)
                } else {
                    LocationManager.instance.retrievLocationObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ next ->
                                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(next, 15f))
                                Log.i("main", ""+next)
                                FacebookPlaceProvider.retrievePlaces(next).subscribe({nextt -> Log.i("mainres", ""+nextt)})
                            })

                }
                true})
            enableMyLocation()
        })
    }

    fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, true)
        } else {
            mMap.setMyLocationEnabled(true);
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

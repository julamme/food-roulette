package com.juhanilammi.foodroulette.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.juhanilammi.foodroulette.R
import com.juhanilammi.foodroulette.const.Constants
import com.juhanilammi.foodroulette.data.PlaceRepository
import com.juhanilammi.foodroulette.data.models.SimpleFacebookLocation
import com.juhanilammi.foodroulette.device.LocationManager
import com.juhanilammi.foodroulette.ui.base.BaseActivity
import com.juhanilammi.foodroulette.utils.PermissionUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<MainView, MainPresenter>(), MainView, LocationFragment.LocationDialogListener {
    override var mPresenter: MainPresenter = MainPresenter(this)
    lateinit var mMap: GoogleMap
    val permissionUtils: PermissionUtils = PermissionUtils()
    val LOCATION_PERMISSION_REQUEST_CODE: Int = 1
    var initialStart: Boolean = true

    override fun onShowOnMapClicked(dialog: DialogFragment, lat: Double?, lon: Double?) {
        mMap.addMarker(MarkerOptions().position(LatLng(lat ?: 0.0, lon ?: 0.0)))
        dialog.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val supportFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportFragment.getMapAsync({ googleMap: GoogleMap ->
            mMap = googleMap
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, true)
            } else {
                LocationManager.instance.build()
            }
            LocationManager.instance.retrieveLocationObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { next ->
                        if (initialStart) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(next!!.latitude, next!!.longitude), 10f))
                            initialStart = false

                        }


                    }


            mMap.setOnMyLocationButtonClickListener({ ->
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    permissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, true)
                } else {
                    progressBar.visibility = View.VISIBLE
                    LocationManager.instance.retrieveLocationObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ next ->
                                PlaceRepository.retrievePlace(next!!)
                                        .subscribeBy(onNext = { item ->
                                            startLocationFragment(item)
                                        })
                            })
                }
                true
            })
            enableMyLocation()
        })
    }

    private fun onAnimationDone() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, true)
        } else {
            mMap.isMyLocationEnabled = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun startLocationFragment(item: SimpleFacebookLocation) {
        val fragment = LocationFragment()
        val args = Bundle()
        args.putParcelable(Constants.ArgumentKeys.LOCATION_ARG_KEY, item)
        fragment.arguments = args
        fragment.show(supportFragmentManager, LocationFragment::class.simpleName)
        progressBar.visibility = View.GONE
    }

}

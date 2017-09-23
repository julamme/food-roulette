package com.juhanilammi.foodroulette.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.provider.SyncStateContract
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.juhanilammi.foodroulette.R
import com.juhanilammi.foodroulette.const.Constants
import com.juhanilammi.foodroulette.data.models.SimpleFacebookLocation
import kotlinx.android.synthetic.main.location_fragment.*

class LocationFragment(): DialogFragment() {
    lateinit var location: SimpleFacebookLocation
    lateinit var mListener: LocationDialogListener
    interface LocationDialogListener {
        fun onShowOnMapClicked(dialog: DialogFragment, lat: Double?, lon: Double?)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mListener = context as LocationDialogListener
        } catch (ex:ClassCastException) {
            throw ClassCastException(context.toString())
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        location = arguments.getParcelable<SimpleFacebookLocation>(Constants.ArgumentKeys.LOCATION_ARG_KEY)
        Log.i("TAG", ""+location.name)
        val view = View.inflate(activity, R.layout.location_fragment, null)
        view.findViewById<TextView>(R.id.locationTitle).text = location.name
        view.findViewById<TextView>(R.id.locationAddress).text = location.location.street
        view.findViewById<Button>(R.id.showInMap).setOnClickListener { view ->
            mListener.onShowOnMapClicked(this, location.location.latitude, location.location.longitude)
        }
        val builder = AlertDialog.Builder(activity)
        builder.setView(view)
        return builder.create()
    }
}
package com.spell.fotopoc.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.orm.SugarRecord
import com.spell.fotopoc.R
import com.spell.fotopoc.activities.EXTRA_POI_ID
import com.spell.fotopoc.activities.POIDetailActivity
import com.spell.fotopoc.adapters.POIAdapter
import com.spell.fotopoc.bindView
import com.spell.fotopoc.model.POI

/**
 * Created by Spellbound on 03.04.2017.
 */
class HomePOIListFragment: Fragment(), GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private val list by bindView<ListView>(R.id.list)

    private val adapter = POIAdapter()

    private var userLocation = LatLng(0.0, 0.0)

    private lateinit var googleAPI: GoogleApiClient

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_poi_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.adapter = adapter
        list.setOnItemClickListener { _, _, i, _ -> onItemClick(i) }

        googleAPI = GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        if (ContextCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }
    }

    override fun onStart() {
        super.onStart()
        refreshData()
        googleAPI.connect()
    }

    override fun onStop() {
        super.onStop()
        googleAPI.disconnect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnected(p0: Bundle?) {
        val location = LocationServices.FusedLocationApi.getLastLocation(googleAPI)
        if (location != null) {
            userLocation = LatLng(location.latitude, location.longitude)
            refreshData()
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    private fun refreshData() {
        adapter.refresh(SugarRecord.listAll(POI::class.java), userLocation)
    }

    private fun onItemClick(pos: Int) {
        val intent = Intent(context, POIDetailActivity::class.java)
        intent.putExtra(EXTRA_POI_ID, adapter.getItem(pos).id)
        context.startActivity(intent)
    }
}
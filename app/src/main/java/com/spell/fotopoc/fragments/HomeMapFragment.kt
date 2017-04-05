package com.spell.fotopoc.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.orm.SugarRecord
import com.spell.fotopoc.activities.EXTRA_POI_ID
import com.spell.fotopoc.activities.EXTRA_POI_LAT_LNG
import com.spell.fotopoc.activities.POIDetailActivity
import com.spell.fotopoc.model.POI


/**
 * Created by Spellbound on 03.04.2017.
 */
class HomeMapFragment : SupportMapFragment() {

    private val MAP_OFFSET = 0.01

    private var gMap: GoogleMap? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMapAsync(this::onMapReady)
    }

    override fun onStart() {
        super.onStart()
        populateLocations()
    }

    fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap?.setOnMapLongClickListener { onMapLongclick(it) }
        gMap?.setOnMarkerClickListener { onMarkerClick(it) }
        if (ContextCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            gMap?.isMyLocationEnabled = true
        }
        gMap?.uiSettings?.isMapToolbarEnabled = false
        populateLocations()
    }

    private fun populateLocations() {
        if (gMap != null) {
            gMap?.clear()
            val locations = SugarRecord.listAll(POI::class.java)
            var minLat = Double.MAX_VALUE
            var maxLat = -Double.MAX_VALUE
            var minLng = Double.MAX_VALUE
            var maxLng = -Double.MAX_VALUE

            for (poi in locations) {
                val point = LatLng(poi.lat, poi.lng)
                gMap?.addMarker(MarkerOptions().position(point).title(poi.name))?.tag = poi

                minLat = Math.min(minLat, poi.lat)
                maxLat = Math.max(maxLat, poi.lat)
                minLng = Math.min(minLng, poi.lng)
                maxLng = Math.max(maxLng, poi.lng)
            }

            val bound = LatLngBounds(LatLng(minLat - MAP_OFFSET, minLng - MAP_OFFSET), LatLng(maxLat + MAP_OFFSET, maxLng + MAP_OFFSET))
            gMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bound, 0))
        }
    }

    private fun onMapLongclick(loc: LatLng) {
        val poi = POI()
        poi.lat = loc.latitude
        poi.lng = loc.longitude

        val intent = Intent(context, POIDetailActivity::class.java)
        intent.putExtra(EXTRA_POI_LAT_LNG, loc)
        context.startActivity(intent)
    }

    private fun onMarkerClick(marker: Marker): Boolean {
        val poi = marker.tag as POI
        val intent = Intent(context, POIDetailActivity::class.java)
        intent.putExtra(EXTRA_POI_ID, poi.id)
        context.startActivity(intent)

        return true
    }


}
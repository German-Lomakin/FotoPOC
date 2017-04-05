package com.spell.fotopoc.fragments

import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.orm.SugarRecord
import com.spell.fotopoc.activities.EXTRA_POI_ID
import com.spell.fotopoc.activities.EXTRA_POI_LAT_LNG
import com.spell.fotopoc.model.POI

/**
 * Created by Spellbound on 03.04.2017.
 */
class POIDetailMapFragment: SupportMapFragment() {

    private lateinit var gMap: GoogleMap
    private var poiID: Long = 0
    private lateinit var loc: LatLng

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMapAsync(this::onMapReady)

        if (arguments.containsKey(EXTRA_POI_ID)) {
            poiID = arguments.getLong(EXTRA_POI_ID)
        } else {
            loc = arguments.getParcelable(EXTRA_POI_LAT_LNG)
        }
    }

    fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.uiSettings.isMapToolbarEnabled = false
        gMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(p0: Marker?) {
            }

            override fun onMarkerDrag(p0: Marker?) {
            }

            override fun onMarkerDragEnd(marker: Marker?) {
                loc = marker?.position!!
            }
        })
        populateLocations()
    }

    private fun populateLocations() {
        val poi: POI
        if (poiID != 0L) {
            poi = SugarRecord.findById(POI::class.java, poiID)
        } else {
            poi = POI()
            poi.lat = loc.latitude
            poi.lng = loc.longitude
        }

        loc = LatLng(poi.lat, poi.lng)
        val marker = gMap.addMarker(MarkerOptions().position(loc).title(poi.name))
        marker.isDraggable = true

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f))
    }

    fun getPOILocation(): LatLng {
        return loc
    }

}
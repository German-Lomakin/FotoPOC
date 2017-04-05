package com.spell.fotopoc.adapters

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import com.spell.fotopoc.R
import com.spell.fotopoc.model.POI

/**
 * Created by Spellbound on 03.04.2017.
 */
class POIAdapter: BaseAdapter() {

    private val items = ArrayList<POI>()
    private var userLocation = LatLng(0.0, 0.0)

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val v: View
        val h: Holder

        if (view == null) {
            v = LayoutInflater.from(parent?.context).inflate(R.layout.item_poi, parent, false)
            h = Holder(
                    v.findViewById(R.id.image) as ImageView,
                    v.findViewById(R.id.txt_name) as TextView,
                    v.findViewById(R.id.txt_distance) as TextView
            )
            v.tag = h
        } else {
            v = view
            h = v.tag as Holder
        }

        val poi = getItem(position)

        h.image.setImageResource(R.drawable.ic_poi)
        h.txtName.text = poi.name

        h.txtDistance.text = getDistanceString(poi.lat, poi.lng)

        return v
    }

    override fun getItem(position: Int): POI {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

    fun refresh(list: List<POI>, location: LatLng) {
        userLocation = location
        items.clear()
        items.addAll(list)
        items.sortBy { getDistance(it.lat, it.lng) }
        notifyDataSetChanged()
    }

    private fun getDistanceString(lat: Double, lng: Double): String {
        val array: FloatArray = kotlin.FloatArray(3)
        Location.distanceBetween(userLocation.latitude, userLocation.longitude, lat, lng, array)
        val distance = array[0]

        var result = if (distance < 1000.0) {
            (Math.round(distance)).toString() + "m"
        } else {
            (distance / 1000).toInt().toString() + "km"
        }

        return result
    }

    private fun getDistance(lat: Double, lng: Double): Float {
        val array: FloatArray = kotlin.FloatArray(3)
        Location.distanceBetween(userLocation.latitude, userLocation.longitude, lat, lng, array)
        return array[0]
    }

    private data class Holder(val image: ImageView, val txtName: TextView, val txtDistance: TextView)
}
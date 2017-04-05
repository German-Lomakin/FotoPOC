package com.spell.fotopoc.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.orm.SugarRecord
import com.spell.fotopoc.R
import com.spell.fotopoc.bindView
import com.spell.fotopoc.fragments.POIDetailMapFragment
import com.spell.fotopoc.model.POI


const val EXTRA_POI_ID = "poi_id"
const val EXTRA_POI_LAT_LNG = "lat_lng"

/**
 * Created by Spellbound on 04.04.2017.
 */
class POIDetailActivity: AppCompatActivity() {

    private var poiID: Long = 0
    private lateinit var loc: LatLng
    private lateinit var poi: POI

    private val toolbar by bindView<Toolbar>(R.id.toolbar)
    private val etName by bindView<EditText>(R.id.txt_name)
    private val btnSave by bindView<Button>(R.id.btn_save)
    private val etNotes by bindView<EditText>(R.id.et_notes)
    private val btnRemove by bindView<ImageView>(R.id.btn_remove)

    private val map = POIDetailMapFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poi_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val bundle = Bundle()

        if(intent.hasExtra(EXTRA_POI_ID)) {
            poiID = intent.getLongExtra(EXTRA_POI_ID, 0)
            bundle.putLong(EXTRA_POI_ID, poiID)
        } else {
            loc = intent.getParcelableExtra(EXTRA_POI_LAT_LNG)
            bundle.putParcelable(EXTRA_POI_LAT_LNG, loc)
        }

        map.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.map_root, map).commit()

        initPOI()
        btnSave.setOnClickListener { onSaveClick() }
        if (poiID != 0L) {
            btnRemove.visibility = View.VISIBLE
            btnRemove.setOnClickListener { onRemoveClick() }
        } else {
            btnRemove.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initPOI() {
        if (poiID == 0L) {
            poi = POI()
            poi.lat = loc.latitude
            poi.lng = loc.longitude
        } else {
            poi = SugarRecord.findById(POI::class.java, poiID)
        }
        etName.setText(poi.name)
        etNotes.setText(poi.notes)
    }

    private fun onRemoveClick() {
        poi.delete()
        Toast.makeText(this, R.string.poi_removed, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun onSaveClick() {
        if (!TextUtils.isEmpty(etName.text)) {
            poi.name = etName.text.toString()
            poi.notes = etNotes.text.toString()
            poi.lat = map.getPOILocation().latitude
            poi.lng = map.getPOILocation().longitude
            poi.save()

            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, R.string.error_empty_name, Toast.LENGTH_SHORT).show()
        }
    }

}
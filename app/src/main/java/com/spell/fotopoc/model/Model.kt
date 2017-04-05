package com.spell.fotopoc.model

import com.orm.SugarRecord

/**
 * Created by Spellbound on 03.04.2017.
 */

data class POIResponse(val locations: List<POI>, val updated: String)

class POI: SugarRecord() {
        var name: String = ""
        var lat: Double = 0.0
        var lng: Double = 0.0
        var notes: String = ""
}





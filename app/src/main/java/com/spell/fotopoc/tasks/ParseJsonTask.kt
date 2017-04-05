package com.spell.fotopoc.tasks

import android.content.Context
import com.google.gson.Gson
import com.spell.fotopoc.model.POIResponse
import java.util.*
import java.util.concurrent.Callable

/**
 * Created by Spellbound on 03.04.2017.
 */
class ParseJsonTask(context: Context): Callable<POIResponse> {
    private val context: Context = context

    override fun call(): POIResponse {
        val gson = Gson()
        val builder = StringBuilder()
        val input = Scanner(context.assets.open("locations.json"))

        while (input.hasNext()) {
            builder.append(input.next())
        }

        input.close()

        val resp = gson.fromJson(builder.toString(), POIResponse::class.java)

//        for (poi in resp.locations) {
//            poi.save()
//        }

        return resp
    }
}
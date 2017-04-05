package com.spell.fotopoc.tasks

import android.content.Context
import android.content.res.AssetManager
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner
import java.io.ByteArrayInputStream

/**
 * Created by Spellbound on 04.04.2017.
 */
@RunWith(MockitoJUnitRunner::class)
class ParseJsonTaskTest {

    @Test
    fun homeInitTest() {
        val str = """
{
    "locations": [
        {
            "name": "Milsons Point",
            "lat": -33.850750,
            "lng": 151.212519
        },
        {
            "name": "Bondi Beach",
            "lat": -33.889967,
            "lng": 151.276440
        },
        {
            "name": "Circular Quay",
            "lat": -33.860178,
            "lng": 151.212706
        },
        {
            "name": "Manly Beach",
            "lat": -33.797151,
            "lng": 151.288784
        },
        {
            "name": "Darling Harbour",
            "lat": -33.873379,
            "lng": 151.200940
        }
    ],
    "updated": "2016-12-01T06:52:08Z"
}
"""

        val context = mock(Context::class.java)
        val assets = mock(AssetManager::class.java)
        `when`(context.assets).thenReturn(assets)
        `when`(assets.open(anyString())).thenReturn(ByteArrayInputStream(str.toByteArray()))
        val resp = ParseJsonTask(context).call()

        assert(resp.locations.size == 5)
    }

}
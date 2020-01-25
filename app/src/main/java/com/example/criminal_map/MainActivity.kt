package com.example.criminal_map

import android.content.res.Resources
import android.graphics.Canvas
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.drawToBitmap
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.gpkg.overlay.features.MarkerOptions
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(R.layout.activity_main)
        map.setTileSource(TileSourceFactory.MAPNIK)

        map.setMultiTouchControls(true)

        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        val mapController = map.controller
        mapController.setZoom(13.5)
        val startPoint = GeoPoint(47.003670, 28.907089)
        mapController.setCenter(startPoint)

        map.minZoomLevel = 13.5
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    fun addMarker(gp : GeoPoint) {
        val marker = Marker(map)
        marker.position = gp
        marker.icon = this@MainActivity.resources.getDrawable(R.drawable.circle, null)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.overlays.add(marker)
    }

    fun getDistrict(address: String): String {
        return JSONArray(URL("https://nominatim.openstreetmap.org/search/seatch" +
                "?city=Chisinau&format=json&street=$address").readText())
                .getJSONObject(1)
                .getJSONObject("display_name")
                .toString()
                .substringAfter("Sectorul ")
                .substringBefore(",")
    }
}

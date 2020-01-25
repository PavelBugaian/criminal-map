package com.example.criminal_map

import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.drawToBitmap
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.config.Configuration
import org.osmdroid.gpkg.overlay.features.MarkerOptions
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(R.layout.activity_main)
        map.setTileSource(TileSourceFactory.MAPNIK)

        map.setMultiTouchControls(true)

        val mapController = map.controller
        mapController.setZoom(14.0)
        val startPoint = GeoPoint(47.003670, 28.907089)
        mapController.setCenter(startPoint)

        map.minZoomLevel = 14.0

        val marker = Marker(map)
        marker.position = GeoPoint(47.003670, 28.907089)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.overlays.add(marker)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}

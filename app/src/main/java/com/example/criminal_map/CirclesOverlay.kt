package com.example.criminal_map

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import org.osmdroid.views.MapView

class CirclesOverlay : org.osmdroid.views.overlay.Overlay() {

    override fun draw(pCanvas: Canvas?, pMapView: MapView?, pShadow: Boolean) {
        if(!isEnabled) {
            return
        }

        if (pShadow) {
            return
        }

        val paint = Paint()

        pMapView!!.projection.save(pCanvas, false, false)
        pCanvas!!.drawCircle(pMapView.x, pMapView.y, 10f, paint)
        pMapView.projection.restore(pCanvas, false)
    }
}
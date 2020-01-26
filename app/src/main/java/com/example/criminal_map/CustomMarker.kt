package com.example.criminal_map

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class CustomMarker(context: Context, map: MapView) : Marker(map, context) {

    override fun setTextIcon(pText: String?) {
        val background = Paint()
        background.color = Color.TRANSPARENT
        val p = Paint()
        p.textSize = 40f
        p.color = Color.BLACK
        p.isAntiAlias = true
        p.typeface = Typeface.DEFAULT_BOLD
        p.textAlign = Paint.Align.LEFT
        val width = (p.measureText(pText) + 0.5f).toInt()
        val baseline: Float = (-p.ascent() + 0.5f)
        val height = (baseline + p.descent() + 0.5f).toInt()
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(image)
        c.drawPaint(background)
        c.drawText(pText!!, 0f, baseline, p)
        super.mIcon = BitmapDrawable(mResources, image)
        setAnchor(ANCHOR_CENTER, ANCHOR_CENTER)
    }
}
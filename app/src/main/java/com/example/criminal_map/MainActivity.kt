package com.example.criminal_map

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.criminal_map.model.CrimeRate
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import java.net.URL

class MainActivity : AppCompatActivity() {

    companion object {
        protected var mResources: Resources? = null
        protected var mTextLabelBackgroundColor = Color.WHITE
        protected var mTextLabelForegroundColor = Color.BLACK
        protected var mTextLabelFontSize = 40
        protected var mIcon: Drawable? = null
        lateinit var dbHelper: DBHelper
        private val gpBotanica = GeoPoint(46.98617, 28.85739)
        private val gpCentru = GeoPoint(47.01779, 28.83379)
        private val gpCiocana = GeoPoint(47.0362, 28.8894)
        private val gpRiscani = GeoPoint(47.0444, 28.8614)
        private val gpBuiucani = GeoPoint(47.0269, 28.7927)
    }

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
        dbHelper = DBHelper(this@MainActivity)

        async {
            val prefs = getSharedPreferences("main", 0)
            val mEditor = prefs.edit()
            var isUpdated = prefs.getBoolean("lastDownload", false)

            if (!isUpdated) {
                getCrimeRates()
                isUpdated = true
                mEditor.putBoolean("lastDownload", isUpdated)
                mEditor.apply()
            }
            uiThread {
                renderMap()
                for (crime in dbHelper.allCrimeRates) {
                    Log.d("DATABASE", crime.location!!)
                }
            }
        }
    }

    /*override fun onResume() {
        super.onResume()
        map.onResume()

        renderMap()
    }*/

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    fun renderMap() {
        val lstCrimes = dbHelper.allCrimeRates
        var botanicaTotal = 0
        var buiucaniTotal = 0
        var centruTotal = 0
        var ciocanaTotal = 0
        var riscaniTotal = 0

        lstCrimes.forEach {
            if (it.crimeType == "TOTAL")
            {
                if (it.location == "BOTANICA") {
                    botanicaTotal = it.number!!
                } else if (it.location == "BUIUCANI") {
                    buiucaniTotal = it.number!!
                } else if (it.location == "CENTRU") {
                    centruTotal = it.number!!
                } else if (it.location == "CIOCANA") {
                    ciocanaTotal = it.number!!
                } else if (it.location == "RISCANI") {
                    riscaniTotal = it.number!!
                }
            }
        }

            addMarker(gpBotanica, botanicaTotal)
            addMarker(gpBuiucani, buiucaniTotal)
            addMarker(gpCentru, centruTotal)
            addMarker(gpCiocana, ciocanaTotal)
            addMarker(gpRiscani, riscaniTotal)

    }

    fun addMarker(gp : GeoPoint, number: Int) {


        val marker = CustomMarker(this@MainActivity, map)
        marker.position = gp
        marker.icon = this@MainActivity.resources.getDrawable(R.drawable.circle, null)
        marker.setTextIcon(number.toString())
        marker.onTouchEvent(null, map)
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

    fun getCrimeRates() {
        val lstCities = ArrayList<String>()
        lstCities.add("BOTANICA")
        lstCities.add("RISCANI")
        lstCities.add("BUIUCANI")
        lstCities.add("CENTRU")
        lstCities.add("CIOCANA")

        val lstCrimeTypes = ArrayList<String>()
        lstCrimeTypes.add("ACCIDENTE RUTIERE")
        lstCrimeTypes.add("ABUZUL DE PUTERE")
        lstCrimeTypes.add("BANDITISMUL")
        lstCrimeTypes.add("CONT.BUN.IN SF. PUBL.")
        lstCrimeTypes.add("CONT.FAMIL.SI MINOR")
        lstCrimeTypes.add("CONT.SANATAT.PUBLIC")
        lstCrimeTypes.add("CONTR.VIET.SI SANAT")
        lstCrimeTypes.add("CONTRA AUTOR.PUBLIC")
        lstCrimeTypes.add("CONTRA DREPT.POLIT.")
        lstCrimeTypes.add("CONTRA PATRIMONIUL.")
        lstCrimeTypes.add("CONTRA SECUR.PUBLIC")
        lstCrimeTypes.add("CONTRABANDA")
        lstCrimeTypes.add("CORUPERE PASIVA")
        lstCrimeTypes.add("CORUPEREA ACTIVA")
        lstCrimeTypes.add("DEOSEBIT DE GRAVE")
        lstCrimeTypes.add("DIN APARTAMENTE")
        lstCrimeTypes.add("ESCHIV.PLAT.VAMALE")
        lstCrimeTypes.add("ESCROCHERII")
        lstCrimeTypes.add("EVAZIUNNEA FISCALA")
        lstCrimeTypes.add("EXCEPTION.DE GRAVE")
        lstCrimeTypes.add("EXCESUL DE PUTERE")
        lstCrimeTypes.add("FABR.BANESTI FALSE")
        lstCrimeTypes.add("FURTURI")
        lstCrimeTypes.add("GRAVE")
        lstCrimeTypes.add("HULIGANISMUL")
        lstCrimeTypes.add("INF.DE COR.IN SEC.PRIV")
        lstCrimeTypes.add("INF.LEG.CU DROGURI")
        lstCrimeTypes.add("INFR.CONT.JUSTITIEI")
        lstCrimeTypes.add("INFR.CONTRA LIBERT.")
        lstCrimeTypes.add("INFR.GRUPURI ORGAN.")
        lstCrimeTypes.add("INFR.IN DOM.TRANSP.")
        lstCrimeTypes.add("INFR.PRIV.VIAT.SEX.")
        lstCrimeTypes.add("INFRACT. ECOLOGICE")
        lstCrimeTypes.add("INFRACT. ECONOMICE")
        lstCrimeTypes.add("INFRACT.INFORMATICE")
        lstCrimeTypes.add("INFRACTIUN.MILITARE")
        lstCrimeTypes.add("JAFURI")
        lstCrimeTypes.add("LUAREA,DAREA DE MITA")
        lstCrimeTypes.add("MAI PUTIN GRAVE")
        lstCrimeTypes.add("NEGLIJENTA IN SERV")
        lstCrimeTypes.add("OMORURI")
        lstCrimeTypes.add("ORGAN.MIGR.ILEGALE")
        lstCrimeTypes.add("PASTR.ILEG.A ARMEI")
        lstCrimeTypes.add("PROXENETISMUL")
        lstCrimeTypes.add("PUNGASIE")
        lstCrimeTypes.add("RAPIRI DE TRANSP.")
        lstCrimeTypes.add("SANTAJ")
        lstCrimeTypes.add("SCOAT.ILEGAL.COPII")
        lstCrimeTypes.add("TILHARII")
        lstCrimeTypes.add("TORTURA")
        lstCrimeTypes.add("TOTAL")
        lstCrimeTypes.add("TRAF.DE FIINT.UMAN")
        lstCrimeTypes.add("TRAFICUL DE COPII")
        lstCrimeTypes.add("TREC.ILEG. A FRONT")
        lstCrimeTypes.add("USOARE")
        lstCrimeTypes.add("VATAM.INTENTIONATE")
        lstCrimeTypes.add("VIOLENTA IN FAMILIE")
        lstCrimeTypes.add("VIOLENTA SEXUALA")
        lstCrimeTypes.add("VIOLURI")

        val crimeCitiesJson = JSONObject(URL("http://172.31.124.7:5000").readText())
        lstCities.forEach {
            val crimeRate = CrimeRate()

            lstCrimeTypes.forEach {crimeType ->
                crimeRate.location = it
                crimeRate.crimeType = crimeType
                crimeRate.number = crimeCitiesJson.getJSONObject(it).getInt(crimeType)
                dbHelper.addCrimeRate(crimeRate)
            }

        }
    }
}

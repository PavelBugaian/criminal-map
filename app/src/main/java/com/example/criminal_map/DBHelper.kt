package com.example.criminal_map

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.criminal_map.model.CrimeRate

class DBHelper(context : Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null,
    DATABASE_VER
    ) {

    companion object{
        private val DATABASE_VER = 1
        private val DATABASE_NAME = "EDMTDB.db"

        private val CRIME_RATE_TABLE_NAME = "Crime_Rate"
        private val COL_CRIME_RATE_ID = "Id"
        private val COL_LOCATION = "Location"
        private val COL_CRIME_TYPE = "Cryme_Type"
        private val COL_NUMBER = "Number"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = ("CREATE TABLE $CRIME_RATE_TABLE_NAME ( " +
                "$COL_CRIME_RATE_ID INTEGER PRIMARY KEY, " +
                "$COL_LOCATION TEXT, " +
                "$COL_CRIME_TYPE TEXT, " +
                "$COL_NUMBER INT)")

        p0!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $CRIME_RATE_TABLE_NAME")
        onCreate(p0)
    }

    val allCrimeRates: ArrayList<CrimeRate>
        get() {
            val lstCrimeRates = ArrayList<CrimeRate>()
            val selectQuery = "SELECT * FROM $CRIME_RATE_TABLE_NAME"
            val db: SQLiteDatabase = this.writableDatabase
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val crimeRate = CrimeRate()
                    crimeRate.id = cursor.getInt(cursor.getColumnIndex(COL_CRIME_RATE_ID))
                    crimeRate.location = cursor.getString(cursor.getColumnIndex(COL_LOCATION))
                    crimeRate.crimeType = cursor.getString(cursor.getColumnIndex(COL_CRIME_TYPE))
                    crimeRate.number= cursor.getInt(cursor.getColumnIndex(COL_NUMBER))

                    lstCrimeRates.add(crimeRate)
                } while (cursor.moveToNext())
                cursor.close()
            }
            db.close()
            return lstCrimeRates
        }

    fun getCrimeRateById(crimeRateId : Int): CrimeRate {
        val db: SQLiteDatabase = this.writableDatabase
        val cursor : Cursor = db.rawQuery("SELECT * FROM $CRIME_RATE_TABLE_NAME WHERE $COL_CRIME_RATE_ID = " + crimeRateId, null)

        if (cursor.moveToNext()) {
            val crimeRate = CrimeRate()
            crimeRate.id = cursor.getInt(cursor.getColumnIndex(COL_CRIME_RATE_ID))
            crimeRate.location = cursor.getString(cursor.getColumnIndex(COL_LOCATION))
            crimeRate.crimeType = cursor.getString(cursor.getColumnIndex(COL_CRIME_TYPE))
            crimeRate.number = cursor.getInt(cursor.getColumnIndex(COL_NUMBER))

            cursor.close()
            return crimeRate
        }
        return CrimeRate()
    }

    fun addCrimeRate(crimeRate: CrimeRate) {
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        /*values.put(COL_ID, card.id)*/
        values.put(COL_LOCATION, crimeRate.location)
        values.put(COL_CRIME_TYPE, crimeRate.crimeType)
        values.put(COL_NUMBER, crimeRate.number)

        db.insert(CRIME_RATE_TABLE_NAME, null, values)
        db.close()
    }

    fun updateCard(crimeRate: CrimeRate): Int {
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(COL_CRIME_RATE_ID, crimeRate.id)
        values.put(COL_LOCATION, crimeRate.location)
        values.put(COL_CRIME_TYPE, crimeRate.crimeType)
        values.put(COL_NUMBER, crimeRate.number)

        return db.update(CRIME_RATE_TABLE_NAME, values, "$COL_CRIME_RATE_ID = ?", arrayOf(crimeRate.id.toString()))
    }

    fun deleteCard(crimeRate: CrimeRate) {
        val db: SQLiteDatabase = this.writableDatabase

        db.delete(CRIME_RATE_TABLE_NAME, "$COL_CRIME_RATE_ID" +
                "= ?", arrayOf(crimeRate.id.toString()))
        db.close()
    }

    fun getCrimeByDistrictAndCrimeType(crymeRateLocation : String, crimeType: String): CrimeRate {
        val db: SQLiteDatabase = this.writableDatabase
        val cursor : Cursor = db.rawQuery("SELECT * FROM $CRIME_RATE_TABLE_NAME WHERE $COL_LOCATION = $crymeRateLocation AND $COL_CRIME_TYPE = $crimeType", null)
        if (cursor.moveToNext()) {
            val crimeRate = CrimeRate()
            crimeRate.id = cursor.getInt(cursor.getColumnIndex(COL_CRIME_RATE_ID))
            crimeRate.location = cursor.getString(cursor.getColumnIndex(COL_LOCATION))
            crimeRate.crimeType = cursor.getString(cursor.getColumnIndex(COL_CRIME_TYPE))
            crimeRate.number = cursor.getInt(cursor.getColumnIndex(COL_NUMBER))

            cursor.close()
            return crimeRate
        }

        db.close()
        return CrimeRate()
    }
}
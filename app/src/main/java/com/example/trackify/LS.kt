package com.example.trackify

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices




class LS : Service() {
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null



    override fun onCreate() {
        super.onCreate()
        Log.d("LocationService", "Service Created") // Log message added
        initData()
    }

    //Location Callback
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val currentLocation: Location? = locationResult.lastLocation
            Log.d("LocationService", "Location update received: ${currentLocation?.latitude}, ${currentLocation?.longitude}")

            // Send location updates via broadcast
            val intent = Intent("location_update")
            intent.putExtra("latitude", currentLocation?.latitude)
            intent.putExtra("longitude", currentLocation?.longitude)
            sendBroadcast(intent)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("LocationService", "Service Started")
        startLocationUpdates()
        return START_STICKY
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        Log.d("hhhhh","Requested")
        mFusedLocationClient!!.requestLocationUpdates(
            locationRequest!!,
            locationCallback, Looper.myLooper()
        )
    }


    override fun onBind(intent: Intent): IBinder? {
        Log.d("LocationService", "Service Bound") // Log message added
        return null
    }

    private fun initData() {
        locationRequest = LocationRequest.create()
        locationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LocationService", "Service destroyed")
    }
    companion object {

        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 3000
    }
}
package com.example.trackify

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment

class MainActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_LOCATION = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is granted, create and add the MapsFragment
            addMapsFragment()
        } else {
            // Permission is not granted, request it from the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_LOCATION
            )
        }
    }

    private fun addMapsFragment() {
        // Create and add the MapsFragment
        val fragment: Fragment = Fragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.login, fragment)
            .commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults) // Call super method

        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("xxx", "xxx")
                addMapsFragment()
            } else {
            }
        }
    }







}

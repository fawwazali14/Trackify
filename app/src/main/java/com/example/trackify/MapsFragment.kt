package com.example.trackify


import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location

import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trackify.viewmodels.maps

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap
    private var isPermissionGranted = false
    private lateinit var viewModel: maps
    private lateinit var latLng : LatLng

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "location_update") {
                val latitude = intent.getDoubleExtra("latitude", 0.0)
                val longitude = intent.getDoubleExtra("longitude", 0.0)
                latLng = LatLng(latitude,longitude)
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(latLng).title("Fawwaz"))

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(maps::class.java)
        // Register the BroadcastReceiver
        val filter = IntentFilter("location_update")
        requireContext().registerReceiver(locationReceiver, filter)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        // Start the LocationService when the fragment is created
        startLocationService()
    }

    private fun startLocationService() {
        val serviceIntent = Intent(requireContext(), LS::class.java)
        Log.d("MapsFragment", "Starting LocationService") // Log message added
        requireContext().startService(serviceIntent)
    }

    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        // Check for location permissions
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isPermissionGranted = true
            // Permission is granted, get the last known location and update marker
            getLocation()
        }
    }

    private fun getLocation() {
        if (isPermissionGranted) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        // Create a LatLng object from the last known location
                        latLng = LatLng(location.latitude, location.longitude)

                        // Clear previous markers
                        googleMap.clear()

                        // Add a marker at the last known location
                        googleMap.addMarker(MarkerOptions().position(latLng).title("Marker"))

                        // Move the camera to the last known location
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    }
                }
        }
    }
}

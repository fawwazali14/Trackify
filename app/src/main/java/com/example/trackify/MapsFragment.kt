package com.example.trackify

import android.Manifest
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
                        val latLng = LatLng(location.latitude, location.longitude)

                        // Add a marker at the last known location
                        googleMap.addMarker(MarkerOptions().position(latLng).title("Marker"))

                        // Move the camera to the last known location
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    }
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    companion object {
        private const val PERMISSION_REQUEST_LOCATION = 123 // Any unique request code
    }
}

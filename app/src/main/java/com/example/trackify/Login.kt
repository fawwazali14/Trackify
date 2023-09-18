package com.example.trackify

import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController


class Login : Fragment() {
    private val PERMISSION_REQUEST_LOCATION = 456 // You can use any unique request code


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn = view.findViewById<Button>(R.id.loginbtn)
        btn.setOnClickListener {
            val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION
            val coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION

            val fineLocationPermissionGranted = PackageManager.PERMISSION_GRANTED ==
                    ContextCompat.checkSelfPermission(requireContext(), fineLocationPermission)
            val coarseLocationPermissionGranted = PackageManager.PERMISSION_GRANTED ==
                    ContextCompat.checkSelfPermission(requireContext(), coarseLocationPermission)

            if (!fineLocationPermissionGranted || !coarseLocationPermissionGranted) {
                // Request permissions
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(fineLocationPermission, coarseLocationPermission),
                    PERMISSION_REQUEST_LOCATION
                )
            } else {
                findNavController().navigate(R.id.action_login_to_mapsFragment)

            }

        }


    }
}
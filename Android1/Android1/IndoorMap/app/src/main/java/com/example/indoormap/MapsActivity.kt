package com.example.indoormap

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    // creating a location manager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    // creating variable for the user location
    private lateinit var lastLocation: Location

    companion object {
        // requesting user's permission
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //calling the location manager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // adding zoom button
        map.uiSettings.isZoomControlsEnabled = true
        // calling the OnMarker function
        map.setOnMarkerClickListener(this)
        // calling the setUpMap method
        setUpMap()
        }


    // implementing all the methods
    override fun onMarkerClick(p0: Marker?) = false

    private fun setUpMap() {
        // asking the user for permission to use the device location
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        // creating the blue dot
        map.isMyLocationEnabled = true
        // gets the most recent location that is currently available
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. move the camera to the user location.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }

    }


}

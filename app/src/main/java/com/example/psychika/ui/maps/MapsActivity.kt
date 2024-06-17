package com.example.psychika.ui.maps

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.psychika.R
import com.example.psychika.data.network.response.NearbyPlacesResponse
import com.example.psychika.data.network.retrofit.NearbyPlacesService

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.psychika.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13f))
                    fetchNearbyHospitals(it.latitude, it.longitude)
                }
            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun fetchNearbyHospitals(lat: Double, lng: Double) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(NearbyPlacesService::class.java)

        val call = service.getNearbyPlaces(
            "hospital",
            "$lat,$lng",
            10000,
            "AIzaSyC1fNwFxe1cmbkbOnaFeThzGglQlO7j6KU"
        )

        call.enqueue(object : Callback<NearbyPlacesResponse> {
            override fun onResponse(
                call: Call<NearbyPlacesResponse>,
                response: Response<NearbyPlacesResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.results?.forEach { place ->
                        val placeLatLng = LatLng(place.geometry.location.lat, place.geometry.location.lng)
                        mMap.addMarker(MarkerOptions().position(placeLatLng).title(place.name))
                        Log.d("MapsActivity", "Marker added for: ${place.name}")
                    }
                } else {
                    Log.e("MapsActivity", "Failed to get nearby places: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<NearbyPlacesResponse>, t: Throwable) {
                Log.e("MapsActivity", "Failed to make API call", t)
            }
        })
    }
}
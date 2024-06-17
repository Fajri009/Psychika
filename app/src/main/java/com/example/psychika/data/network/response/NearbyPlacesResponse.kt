package com.example.psychika.data.network.response

data class NearbyPlacesResponse(
    val results: List<Place>
)

data class Place(
    val name: String,
    val geometry: Geometry
)

data class Geometry(
    val location: LocationLatLng
)

data class LocationLatLng(
    val lat: Double,
    val lng: Double
)
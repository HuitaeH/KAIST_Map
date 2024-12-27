package com.example.kaistmap

data class MarkerData(
    val id: Int,
    val name: String,
    val lat: Double,
    val lng: Double,
    val address: String,
    val menu: String,
    val direction: String,
)
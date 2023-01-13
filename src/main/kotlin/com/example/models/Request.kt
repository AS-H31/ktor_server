package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Request(val gpsfix: String, val timestamp: String, val messages: String)
package com.example.psychika.data.network.firebase

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserGoogleAuth(
    val profilePic: Uri,
    val firstName: String,
    val lastName: String,
    val email: String
): Parcelable

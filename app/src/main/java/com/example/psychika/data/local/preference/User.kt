package com.example.psychika.data.local.preference

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var email: String? = ""
): Parcelable
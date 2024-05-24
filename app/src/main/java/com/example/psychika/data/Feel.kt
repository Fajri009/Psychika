package com.example.psychika.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Feel (
    val photo: Int,
    val desc: String
): Parcelable
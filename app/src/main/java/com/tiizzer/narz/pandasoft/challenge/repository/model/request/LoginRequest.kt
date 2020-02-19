package com.tiizzer.narz.pandasoft.challenge.repository.model.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginRequest(
    val username: String,
    val password: String
): Parcelable
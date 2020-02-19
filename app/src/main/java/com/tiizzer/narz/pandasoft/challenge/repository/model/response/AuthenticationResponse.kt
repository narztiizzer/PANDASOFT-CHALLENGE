package com.tiizzer.narz.pandasoft.challenge.repository.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthenticationResponse(
    val status: Int,
    val message: String,
    val data: String? = null,
    val access_token: String,
    val refresh_token: String,
    val expires_in: Long
): Parcelable
package com.tiizzer.narz.pandasoft.challenge.repository.model.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RefreshTokenRequest(
    val refresh_token: String
): Parcelable
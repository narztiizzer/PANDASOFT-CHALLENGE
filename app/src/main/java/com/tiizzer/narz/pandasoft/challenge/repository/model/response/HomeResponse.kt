package com.tiizzer.narz.pandasoft.challenge.repository.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeResponse(
    val status: Int,
    val message: String,
    val data: List<NewsItemResponse> = ArrayList()
): Parcelable
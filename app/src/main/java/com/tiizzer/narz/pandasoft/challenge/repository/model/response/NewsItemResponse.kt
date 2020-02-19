package com.tiizzer.narz.pandasoft.challenge.repository.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsItemResponse(
    val id: String,
    val uuid: String,
    val create: Long,
    val title: String,
    val image: String,
    val detail: String
): Parcelable
package com.tiizzer.narz.pandasoft.challenge.repository

import com.tiizzer.narz.pandasoft.challenge.utils.SharePreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response

class AppRequestInterceptor(private val preferencesHelper: SharePreferencesHelper): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = this.preferencesHelper.getAccessToken() ?: ""
        val request = chain.request()
        val requestBuilder = request.newBuilder().apply {
            addHeader("Content-Type", "application/json")
            if(token.isNotEmpty()) addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(requestBuilder.build())
    }
}
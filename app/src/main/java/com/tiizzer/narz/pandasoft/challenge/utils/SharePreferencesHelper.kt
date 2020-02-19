package com.tiizzer.narz.pandasoft.challenge.utils

import android.content.Context
import android.content.SharedPreferences
import com.tiizzer.narz.pandasoft.challenge.R

class SharePreferencesHelper(private val context: Context) {
    private val sharedPreferences: SharedPreferences = this.context.getSharedPreferences("${this.context.packageName}.pref", Context.MODE_PRIVATE)

    fun getAccessToken() = this.sharedPreferences.getString(this.context.getString(R.string.app_access_token_key), null)
    fun setAccessToken(token: String) = this.sharedPreferences.edit().putString(this.context.getString(R.string.app_access_token_key), token).commit()

    fun getRefreshToken() = this.sharedPreferences.getString(this.context.getString(R.string.app_refresh_token_key), null)
    fun setRefreshToken(token: String) = this.sharedPreferences.edit().putString(this.context.getString(R.string.app_refresh_token_key), token).commit()

    fun getTokenExpireTime() = this.sharedPreferences.getLong(this.context.getString(R.string.app_token_expire_key), 0)
    fun setTokenExpireTime(time: Long): Boolean {
        val computeTime = System.currentTimeMillis() + (time * 1000)
        return this.sharedPreferences.edit().putLong(this.context.getString(R.string.app_token_expire_key), computeTime).commit()
    }

    fun getLastInteractTime() = this.sharedPreferences.getLong(this.context.getString(R.string.interact_time_key), System.currentTimeMillis())
    fun setLastInteractTime(time: Long) = this.sharedPreferences.edit().putLong(this.context.getString(R.string.interact_time_key), time).commit()

    fun clear() = this.sharedPreferences.edit().clear().apply()
}
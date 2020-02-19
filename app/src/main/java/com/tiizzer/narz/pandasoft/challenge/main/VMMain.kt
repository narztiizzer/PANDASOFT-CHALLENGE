package com.tiizzer.narz.pandasoft.challenge.main

import android.content.Context
import android.content.Intent
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tiizzer.narz.pandasoft.challenge.BaseApplication
import com.tiizzer.narz.pandasoft.challenge.repository.AppApiRepository
import com.tiizzer.narz.pandasoft.challenge.utils.InteractionTimeoutHelper
import com.tiizzer.narz.pandasoft.challenge.utils.SharePreferencesHelper

class VMMain(
    private val context: Context,
    private val repository: AppApiRepository,
    private val sharePreferences: SharePreferencesHelper
): ViewModel() {
    private lateinit var interactionHelper: InteractionTimeoutHelper
    private val _showLoginPage = MutableLiveData<Void>()
    private val _showHomePage = MutableLiveData<Void>()
    private val _interactionTimeout = MutableLiveData<Void>()

    val showLoginPage: LiveData<Void> = this._showLoginPage
    val showHomePage: LiveData<Void> = this._showHomePage
    val interactionTimeout: LiveData<Void> = this._interactionTimeout

    init {
        if(this.context.applicationContext != null)
            this.interactionHelper = (this.context.applicationContext as BaseApplication).getInteractionHelper()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun isHasCacheUser(
        accessToken: String? = this.sharePreferences.getAccessToken(),
        refreshToken: String? = this.sharePreferences.getRefreshToken()
    ): Boolean {
        return !accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun isInteractionTimeout() = this.interactionHelper.isInteractionTimeout(this.context)

    fun prepare(){
        if(isHasCacheUser()){
            this._showHomePage.postValue(null)
        } else {
            this._showLoginPage.postValue(null)
        }
    }

    fun checkInteractionTime() {
        if(this.isInteractionTimeout()){ this._interactionTimeout.postValue(null) }
        else { setLastInteractionTime(System.currentTimeMillis()) }
    }

    fun clearSession(){ this.sharePreferences.clear() }

    private fun setLastInteractionTime(time: Long) = this.interactionHelper.setLastInteractionTime(this.context, time)
}
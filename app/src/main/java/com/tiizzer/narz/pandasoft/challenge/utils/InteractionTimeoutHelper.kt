package com.tiizzer.narz.pandasoft.challenge.utils

import android.content.Context
import androidx.annotation.VisibleForTesting
import java.util.*

class InteractionTimeoutHelper private constructor(){
    private val INTERACT_TIMEOUT = 10 * 60 * 1000
    private var sharePreferencesHelper: SharePreferencesHelper? = null

    companion object {
        fun getInstance() = InteractionTimeoutHelper()
    }

    fun setLastInteractionTime(
        context: Context,
        time: Long = Calendar.getInstance().timeInMillis
    ){
        SharePreferencesHelper(context).setLastInteractTime(time)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getLastInteractionTime(context: Context) = getSharePreferences(context).getLastInteractTime()


    private fun getSharePreferences(context: Context) : SharePreferencesHelper {
        if(this.sharePreferencesHelper == null){
            this.sharePreferencesHelper = SharePreferencesHelper(context)
        }
        return this.sharePreferencesHelper!!
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setSharePreferences(sharePreferencesHelper: SharePreferencesHelper)  {
        this.sharePreferencesHelper = sharePreferencesHelper
    }

    fun isInteractionTimeout(context: Context): Boolean {
        val lastInteraction = getLastInteractionTime(context)
        val current = Calendar.getInstance().timeInMillis
        val diff = current - lastInteraction
        return diff > INTERACT_TIMEOUT
    }
}
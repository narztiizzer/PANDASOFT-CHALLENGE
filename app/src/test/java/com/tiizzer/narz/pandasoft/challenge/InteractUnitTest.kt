package com.tiizzer.narz.pandasoft.challenge

import android.content.Context
import com.tiizzer.narz.pandasoft.challenge.utils.InteractionTimeoutHelper
import com.tiizzer.narz.pandasoft.challenge.utils.SharePreferencesHelper
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class InteractUnitTest {
    private lateinit var context: Context
    private lateinit var interact: InteractionTimeoutHelper
    private lateinit var sharedPref: SharePreferencesHelper
    @Before
    fun prepare(){
        this.interact = InteractionTimeoutHelper.getInstance()
        this.context = Mockito.mock(Context::class.java)
        this.sharedPref = Mockito.mock(SharePreferencesHelper::class.java)
        this.interact.setSharePreferences(this.sharedPref)
    }

    @Test
    fun testCheckTimeout(){
        val minutes = 10 * 60 * 1000
        Mockito.`when`(this.sharedPref.getLastInteractTime()).thenReturn(System.currentTimeMillis() - minutes)
        val isTimeout = this.interact.isInteractionTimeout(this.context)
        assert(isTimeout)
    }
}
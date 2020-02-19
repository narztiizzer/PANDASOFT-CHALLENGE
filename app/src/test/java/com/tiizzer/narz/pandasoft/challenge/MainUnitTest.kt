package com.tiizzer.narz.pandasoft.challenge

import android.content.Context
import com.tiizzer.narz.pandasoft.challenge.main.VMMain
import com.tiizzer.narz.pandasoft.challenge.repository.AppApiRepository
import com.tiizzer.narz.pandasoft.challenge.utils.InteractionTimeoutHelper
import com.tiizzer.narz.pandasoft.challenge.utils.SharePreferencesHelper
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MainUnitTest {
    private lateinit var vmMain: VMMain
    private lateinit var sharedPref: SharePreferencesHelper
    private lateinit var context: Context
    private lateinit var repository: AppApiRepository
    private lateinit var interactionHelper: InteractionTimeoutHelper
    @Before
    fun prepare(){
        this.context = Mockito.mock(Context::class.java)
        this.sharedPref = Mockito.mock(SharePreferencesHelper::class.java)
        this.repository = Mockito.mock(AppApiRepository::class.java)
        this.vmMain = VMMain(this.context, repository, sharedPref)
        this.interactionHelper = InteractionTimeoutHelper.getInstance()
    }

    @Test
    fun testCheckToken() {
        Mockito.`when`(this.sharedPref.getAccessToken()).thenReturn("qwertyuiopasdfgh")
        Mockito.`when`(this.sharedPref.getRefreshToken()).thenReturn("mnbvcxzkjhgf")

        assert(this.vmMain.isHasCacheUser())
    }

    @Test
    fun testClearSession() {
        this.vmMain.clearSession()

        val accessToken = this.sharedPref.getAccessToken() == null
        val refreshToken = this.sharedPref.getRefreshToken() == null
        val tokenExpire = this.sharedPref.getTokenExpireTime() == 0L

        assert(accessToken && refreshToken && tokenExpire)
    }
}
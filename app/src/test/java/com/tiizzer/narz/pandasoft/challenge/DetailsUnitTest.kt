package com.tiizzer.narz.pandasoft.challenge

import android.content.Context
import com.tiizzer.narz.pandasoft.challenge.details.VMDetails
import com.tiizzer.narz.pandasoft.challenge.repository.AppApiRepository
import com.tiizzer.narz.pandasoft.challenge.utils.InteractionTimeoutHelper
import com.tiizzer.narz.pandasoft.challenge.utils.SharePreferencesHelper
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class DetailsUnitTest {
    private lateinit var vmDetail: VMDetails
    private lateinit var sharedPref: SharePreferencesHelper
    private lateinit var context: Context
    private lateinit var repository: AppApiRepository
    private lateinit var interactionHelper: InteractionTimeoutHelper

    @Before
    fun prepare() {
        this.context = Mockito.mock(Context::class.java)
        this.sharedPref = Mockito.mock(SharePreferencesHelper::class.java)
        this.repository = Mockito.mock(AppApiRepository::class.java)
        this.vmDetail = VMDetails(this.context, repository, sharedPref)
        this.interactionHelper = InteractionTimeoutHelper.getInstance()
    }

    @Test
    fun testConvertTimestamp(){
        val timestamp = System.currentTimeMillis()
        val convert = this.vmDetail.convertTimestampToString(timestamp)
        assert(convert == "19/02/2020")
    }

    @Test
    fun testTokenExpire(){
        Mockito.`when`(this.sharedPref.getTokenExpireTime()).thenReturn(System.currentTimeMillis() - 10000)
        val timestamp = this.sharedPref.getTokenExpireTime()
        val isExpire = this.vmDetail.isTokenExpire(timestamp)
        assert(isExpire)
    }

}
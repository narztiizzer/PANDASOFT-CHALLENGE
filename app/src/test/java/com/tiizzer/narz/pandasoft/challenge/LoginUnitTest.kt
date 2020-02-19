package com.tiizzer.narz.pandasoft.challenge

import android.content.Context
import com.tiizzer.narz.pandasoft.challenge.authentication.VMLogin
import com.tiizzer.narz.pandasoft.challenge.repository.AppApiRepository
import com.tiizzer.narz.pandasoft.challenge.repository.model.request.LoginRequest
import com.tiizzer.narz.pandasoft.challenge.repository.model.response.AuthenticationResponse
import com.tiizzer.narz.pandasoft.challenge.utils.SharePreferencesHelper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Response
import java.net.HttpURLConnection

class LoginUnitTest {
    private lateinit var vmLogin: VMLogin
    private lateinit var sharedPref: SharePreferencesHelper
    private lateinit var context: Context
    private lateinit var repository: AppApiRepository

    @Before
    fun prepare(){
        this.context = Mockito.mock(Context::class.java)
        this.sharedPref = Mockito.mock(SharePreferencesHelper::class.java)
        this.repository = Mockito.mock(AppApiRepository::class.java)
        this.vmLogin = VMLogin(this.context, this.repository, this.sharedPref)
    }

    @Test
    fun testValidateInput() {
        val username = "usertest"
        val password = "123456"
        val result = this.vmLogin.isValidInput(username, password)
        assert(result)
    }

    @Test
    fun testCacheUserInput() {
        val auth = AuthenticationResponse(
            200,
            "success",
            null,
            "1234567890",
            "abcdefghijkl",
            650
        )
        Mockito.`when`(this.sharedPref.setAccessToken(auth.access_token)).thenReturn(true)
        Mockito.`when`(this.sharedPref.setRefreshToken(auth.refresh_token)).thenReturn(true)
        Mockito.`when`(this.sharedPref.setTokenExpireTime(auth.expires_in)).thenReturn(true)

        val result = this.vmLogin.saveNewAccessToken(auth)

        assert(result)
    }

}
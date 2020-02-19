package com.tiizzer.narz.pandasoft.challenge.details

import android.content.Context
import android.content.Intent
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiizzer.narz.pandasoft.challenge.BaseApplication
import com.tiizzer.narz.pandasoft.challenge.R
import com.tiizzer.narz.pandasoft.challenge.repository.AppApiRepository
import com.tiizzer.narz.pandasoft.challenge.repository.model.request.LikeRequest
import com.tiizzer.narz.pandasoft.challenge.repository.model.request.RefreshTokenRequest
import com.tiizzer.narz.pandasoft.challenge.repository.model.response.AuthenticationResponse
import com.tiizzer.narz.pandasoft.challenge.repository.model.response.NewsItemResponse
import com.tiizzer.narz.pandasoft.challenge.utils.InteractionTimeoutHelper
import com.tiizzer.narz.pandasoft.challenge.utils.SharePreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class VMDetails(
    private val context: Context,
    private val repository: AppApiRepository,
    private val sharePreferences: SharePreferencesHelper
): ViewModel() {
    private val interactionHelper: InteractionTimeoutHelper by lazy {
        (this.context.applicationContext as BaseApplication).getInteractionHelper()
    }
    private var _rawData: NewsItemResponse? = null
    private val _image = MutableLiveData<String>()
    private val _title = MutableLiveData<String>()
    private val _details = MutableLiveData<String>()
    private val _date = MutableLiveData<String>()
    private val _showMessage = MutableLiveData<String>()
    private val _interactionTimeout = MutableLiveData<Void>()
    private val _formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val image: LiveData<String> = this._image
    val title: LiveData<String> = this._title
    val details: LiveData<String> = this._details
    val date: LiveData<String> = this._date
    val showMessage: LiveData<String> = this._showMessage
    val interactionTimeout: LiveData<Void> = this._interactionTimeout

    fun prepareData(intent: Intent){
        this._rawData = intent.getParcelableExtra(this.context.getString(R.string.details_parcel_key))

        this._image.postValue(this._rawData?.image)
        this._title.postValue(this._rawData?.title)
        this._details.postValue(this._rawData?.detail)
        this._date.postValue(
            if(this._rawData?.create == null) "--/--/----"
            else this.convertTimestampToString(this._rawData?.create!!)
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun prepareMockData(){
        this._image.postValue("")
        this._title.postValue("Hello, details page.")
        this._details.postValue("This is a content for detail page")
        this._date.postValue(this.convertTimestampToString(System.currentTimeMillis() / 1000))
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun isInteractionTimeout() = this.interactionHelper.isInteractionTimeout(this.context)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun convertTimestampToString(timestamp: Long): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp * 1000
        }
        return _formatter.format(calendar.time)
    }

    fun like(){
        viewModelScope.launch(Dispatchers.Default) {
            try {
                if(isTokenExpire(sharePreferences.getTokenExpireTime())){ getNewAccessToken() }
                val isSuccess = getResponseFromServer()
                if(isSuccess){
                    _showMessage.postValue(context.getString(R.string.details_like_success))
                } else {
                    _showMessage.postValue(context.getString(R.string.authentication_problem_message))
                }
            } catch (e: Exception){
                _showMessage.postValue(e.message ?: context.getString(R.string.authentication_problem_message))
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun isTokenExpire(tokenTime: Long) = tokenTime < System.currentTimeMillis()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNewAccessToken() {
        val refreshToken = sharePreferences.getRefreshToken()
        val response = repository.refreshToken(RefreshTokenRequest(refreshToken!!)).execute()
        if(response.body()?.status != 200){
            throw Exception(
                response.body()?.message ?:
                response.errorBody()?.string() ?:
                this.context.getString(R.string.authentication_problem_message)
            )
        } else {
            saveNewAccessToken(response.body())
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun saveNewAccessToken(response: AuthenticationResponse?){
        response?.let {
            sharePreferences.setAccessToken(it.access_token)
            sharePreferences.setTokenExpireTime(it.expires_in)
        }
    }

    fun checkInteractionTime() {
        if(this.isInteractionTimeout()){ this._interactionTimeout.postValue(null) }
        else { setLastInteractionTime(System.currentTimeMillis()) }
    }

    fun clearSession(){ this.sharePreferences.clear() }

    private fun setLastInteractionTime(time: Long) = this.interactionHelper.setLastInteractionTime(this.context, time)

    private fun getResponseFromServer(): Boolean {
        val response = repository.likeNews(LikeRequest(_rawData!!.id)).execute()
        if(!response.isSuccessful){
            throw Exception(
                response.errorBody()?.string() ?:
                context.getString(R.string.authentication_problem_message)
            )
        } else {
            return true
        }
    }
}
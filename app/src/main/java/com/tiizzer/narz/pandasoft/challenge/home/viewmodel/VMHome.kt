package com.tiizzer.narz.pandasoft.challenge.home.viewmodel

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiizzer.narz.pandasoft.challenge.BaseApplication
import com.tiizzer.narz.pandasoft.challenge.R
import com.tiizzer.narz.pandasoft.challenge.home.model.HomeListViewData
import com.tiizzer.narz.pandasoft.challenge.repository.AppApiRepository
import com.tiizzer.narz.pandasoft.challenge.repository.model.request.RefreshTokenRequest
import com.tiizzer.narz.pandasoft.challenge.repository.model.response.AuthenticationResponse
import com.tiizzer.narz.pandasoft.challenge.repository.model.response.HomeResponse
import com.tiizzer.narz.pandasoft.challenge.repository.model.response.NewsItemResponse
import com.tiizzer.narz.pandasoft.challenge.utils.InteractionTimeoutHelper
import com.tiizzer.narz.pandasoft.challenge.utils.SharePreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VMHome(
    private val context: Context,
    private val repository: AppApiRepository,
    private val sharePreferences: SharePreferencesHelper
): ViewModel() {
    private val _interactionHelper: InteractionTimeoutHelper by lazy {
        (this.context.applicationContext as BaseApplication).getInteractionHelper()
    }
    private val _retrieveSuccess = MutableLiveData<List<HomeListViewData>>()
    private val _errorMessage = MutableLiveData<String>()
    private val _showLoadingDialog = MutableLiveData<Void>()
    private val _hideLoadingDialog = MutableLiveData<Void>()
    private val _openDetailPage = MutableLiveData<NewsItemResponse>()
    private var _apiData: HomeResponse? = null

    val retrieveSuccess: LiveData<List<HomeListViewData>> = this._retrieveSuccess
    val errorMessage: LiveData<String> = this._errorMessage
    val openDetailPage: LiveData<NewsItemResponse> = this._openDetailPage
    val showLoadingDialog: LiveData<Void> = this._showLoadingDialog
    val hideLoadingDialog: LiveData<Void> = this._hideLoadingDialog

    fun getNewsItems(){
        viewModelScope.launch(Dispatchers.Default) {
            try {
                if(isTokenExpire(sharePreferences.getTokenExpireTime())){ getNewAccessToken() }
                val response = repository.getHomeListData().execute()
                if(response.body()?.status != 200){
                    _errorMessage.postValue(
                        response.body()?.message ?:
                        response.errorBody()?.string() ?:
                        this@VMHome.context.getString(R.string.authentication_problem_message)
                    )
                } else {
                    _apiData = response.body()
                    _retrieveSuccess.postValue(
                        response.body()?.data?.map {
                            HomeListViewData(
                                it.image,
                                it.title
                            )
                        }
                    )
                }
            } catch (e: Exception){
                _errorMessage.postValue(e.message ?: this@VMHome.context.getString(R.string.authentication_problem_message))
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

    fun openDetailPageFromPosition(position: Int){
        val item = this._apiData?.data?.get(position)
        this._openDetailPage.postValue(item)
    }

    fun getParcelKey() = this.context.getString(R.string.details_parcel_key)

    fun isInteractionTimeout() = this._interactionHelper.isInteractionTimeout(this.context)

}
package com.tiizzer.narz.pandasoft.challenge.authentication

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiizzer.narz.pandasoft.challenge.R
import com.tiizzer.narz.pandasoft.challenge.repository.AppApiRepository
import com.tiizzer.narz.pandasoft.challenge.repository.model.request.LoginRequest
import com.tiizzer.narz.pandasoft.challenge.repository.model.response.AuthenticationResponse
import com.tiizzer.narz.pandasoft.challenge.utils.SharePreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VMLogin(
    private val context: Context,
    private val repository: AppApiRepository,
    private val sharePreferences: SharePreferencesHelper
): ViewModel(){
    private val _authenticationSuccess = MutableLiveData<AuthenticationResponse>()
    private val _errorMessage = MutableLiveData<String>()
    private val _showLoadingDialog = MutableLiveData<Void>()
    private val _hideLoadingDialog = MutableLiveData<Void>()

    val authenticationSuccess: LiveData<AuthenticationResponse> = this._authenticationSuccess
    val errorMessage: LiveData<String> = this._errorMessage
    val showLoadingDialog: LiveData<Void> = this._showLoadingDialog
    val hideLoadingDialog: LiveData<Void> = this._hideLoadingDialog

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun isValidInput(username: String, password: String): Boolean {
        return when{
            username.isEmpty() -> {
                this@VMLogin._errorMessage
                    .postValue(this@VMLogin.context.getString(R.string.username_problem_message))
                false
            }
            password.isEmpty() -> {
                this@VMLogin._errorMessage
                    .postValue(this@VMLogin.context.getString(R.string.password_problem_message))
                false
            }
            else -> true
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun validateToken(response: AuthenticationResponse?): Boolean {
        return response?.access_token != null && response.refresh_token.isNotEmpty() && response.expires_in > 0
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun saveNewAccessToken(response: AuthenticationResponse?): Boolean {
        return if(this.validateToken(response)){
            (sharePreferences.setAccessToken(response!!.access_token)
                    && sharePreferences.setRefreshToken(response.refresh_token)
                    && sharePreferences.setTokenExpireTime(response.expires_in))
        } else {
            false
        }
    }

    private fun getResponseFromServer(username: String, password: String): AuthenticationResponse? {
        val response = repository.authenticate(LoginRequest(username, password)).execute()
        if(response.body()?.status != 200){
            throw Exception(
                response.body()?.message ?:
                response.errorBody()?.string() ?:
                this@VMLogin.context.getString(R.string.authentication_problem_message)
            )
        } else {
            return response.body()
        }
    }

    fun authenticate(username: String, password: String){
        if(isValidInput(username, password)){
            viewModelScope.launch(Dispatchers.Default) {
                try {
                    val response = getResponseFromServer(username, password)
                    saveNewAccessToken(response)
                    _authenticationSuccess.postValue(response)
                } catch (e: Exception){
                    _errorMessage.postValue(e.message ?: this@VMLogin.context.getString(R.string.authentication_problem_message))
                }
            }
        }
    }

}
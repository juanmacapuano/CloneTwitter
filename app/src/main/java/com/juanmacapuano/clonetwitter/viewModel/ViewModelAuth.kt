package com.juanmacapuano.clonetwitter.viewModel

import androidx.lifecycle.*
import com.juanmacapuano.clonetwitter.service.api.Resource
import com.juanmacapuano.clonetwitter.service.api.StatusResponseAPI
import com.juanmacapuano.clonetwitter.service.data.auth.RequestLogin
import com.juanmacapuano.clonetwitter.service.data.auth.RequestSignup
import com.juanmacapuano.clonetwitter.service.data.auth.ResponseLogin
import com.juanmacapuano.clonetwitter.service.data.auth.ResponseSignup
import com.juanmacapuano.clonetwitter.service.repository.Repository
import com.juanmacapuano.clonetwitter.tools.Event
import kotlinx.coroutines.launch
import java.net.UnknownHostException

private val TAG = ViewModelAuth::class.java.simpleName

class ViewModelAuth(
    private val repository: Repository
) : ViewModel() {


    private val _loginResponse = MutableLiveData<Resource<ResponseLogin>>()
    val loginResponse: LiveData<Resource<ResponseLogin>>
        get() = _loginResponse

    private val _registerResponse = MutableLiveData<Resource<ResponseSignup>>()
    val registerResponse: LiveData<Resource<ResponseSignup>>
        get() = _registerResponse

    private val _statusLoading = MutableLiveData<StatusResponseAPI>()
    val statusResponseAPI: LiveData<StatusResponseAPI>
        get() = _statusLoading

    private val _statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = _statusMessage

    fun doLogin(requestLogin: RequestLogin) {
        viewModelScope.launch {
            try {
                _statusLoading.value = StatusResponseAPI.LOADING
                _loginResponse.value = repository.loginUser(requestLogin)
                _statusLoading.value = StatusResponseAPI.DONE
            } catch (e: UnknownHostException) {
                _statusLoading.value = StatusResponseAPI.ERROR
            }

        }
    }

    fun doRegister(requestSignup: RequestSignup) {
        viewModelScope.launch {
            try {
                _statusLoading.value = StatusResponseAPI.LOADING
                _registerResponse.value = repository.registerUser(requestSignup)
                _statusLoading.value = StatusResponseAPI.DONE
            } catch (e: UnknownHostException) {
                _statusLoading.value = StatusResponseAPI.ERROR
            }
        }
    }

    suspend fun saveAuthToken(token: String) {
        repository.saveAuth(token)
    }

    suspend fun saveUserName(userName: String) {
        repository.saveUserName(userName)
    }
}


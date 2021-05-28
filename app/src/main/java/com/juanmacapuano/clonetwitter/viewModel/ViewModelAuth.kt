package com.juanmacapuano.clonetwitter.viewModel

import androidx.lifecycle.*
import com.juanmacapuano.clonetwitter.service.api.Resource
import com.juanmacapuano.clonetwitter.service.api.StatusResponseAPI
import com.juanmacapuano.clonetwitter.service.data.RequestLogin
import com.juanmacapuano.clonetwitter.service.data.RequestSignup
import com.juanmacapuano.clonetwitter.service.data.ResponseLogin
import com.juanmacapuano.clonetwitter.service.data.ResponseSignup
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

    fun checkEmptyFieldsLogin(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                _statusMessage.value = Event("Ingrese un Email")
                false
            }
            password.isEmpty() -> {
                _statusMessage.value = Event("Ingrese un Password")
                false
            }
            else -> {
                true
            }
        }
    }

    fun checkEmptyFieldsRegister(name: String, email: String, password: String): Boolean {
        return when {
            name.isEmpty() -> {
                _statusMessage.value = Event("Ingrese un nombre")
                false
            }
            email.isEmpty() -> {
                _statusMessage.value = Event("Ingrese un Email")
                false
            }
            password.isEmpty() -> {
                _statusMessage.value = Event("Ingrese un Password")
                false
            }
            else -> {
                true
            }
        }
    }

    suspend fun saveAuthToken(token: String) {
        repository.saveAuth(token)
    }
}


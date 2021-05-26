package com.juanmacapuano.clonetwitter.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.juanmacapuano.clonetwitter.service.api.Resource
import com.juanmacapuano.clonetwitter.service.api.StatusResponseAPI
import com.juanmacapuano.clonetwitter.service.data.RequestLogin
import com.juanmacapuano.clonetwitter.service.data.ResponseLogin
import com.juanmacapuano.clonetwitter.service.repository.Repository
import kotlinx.coroutines.launch
import java.net.UnknownHostException

private val TAG = ViewModelAuth::class.java.simpleName

class ViewModelAuth(application: Application): AndroidViewModel(application) {

    private val repository = Repository()

    private val _loginResponse = MutableLiveData<Resource<ResponseLogin>>()
    val loginResponse: LiveData<Resource<ResponseLogin>>
        get() = _loginResponse

    private val _statusLoading = MutableLiveData<StatusResponseAPI>()
    val statusResponseAPI: LiveData<StatusResponseAPI>
        get() = _statusLoading

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
}


package com.juanmacapuano.clonetwitter.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanmacapuano.clonetwitter.service.api.Resource
import com.juanmacapuano.clonetwitter.service.api.StatusResponseAPI
import com.juanmacapuano.clonetwitter.service.data.tweets.RequestNewTweet
import com.juanmacapuano.clonetwitter.service.data.tweets.Tweet
import com.juanmacapuano.clonetwitter.service.repository.Repository
import com.juanmacapuano.clonetwitter.tools.Event
import kotlinx.coroutines.launch

private val TAG = ViewModelTweets::class.java.simpleName

class ViewModelTweets(
    private val repository: Repository
) : ViewModel() {

    private val _responseCreateTweet = MutableLiveData<Resource<Tweet>>()
    val responseCreateTweet: LiveData<Resource<Tweet>>
        get() = _responseCreateTweet

    private val _responseLikeTweet = MutableLiveData<Resource<Tweet>>()
    val responseLikeTweet: LiveData<Resource<Tweet>>
        get() = _responseLikeTweet

    private val _responseTweet = MutableLiveData<Resource<List<Tweet>>>()
    val responseTweet: LiveData<Resource<List<Tweet>>>
        get() = _responseTweet

    private val _statusLoading = MutableLiveData<StatusResponseAPI>()
    val statusResponseAPI: LiveData<StatusResponseAPI>
        get() = _statusLoading

    init {
        viewModelScope.launch {
            try {
                _statusLoading.value = StatusResponseAPI.LOADING
                _responseTweet.value = repository.getAllTweets()
                _statusLoading.value = StatusResponseAPI.DONE
            } catch (e: Exception) {
                _statusLoading.value = StatusResponseAPI.ERROR
                // _statusMessage.value = Event("Hubo un error al recuperar los Tweets")

            }
        }
    }


    fun setRefreshData() {
        viewModelScope.launch {
            try {
                _responseTweet.value = repository.getAllTweets()
            } catch (e: Exception) {
                _statusLoading.value = StatusResponseAPI.ERROR
                // _statusMessage.value = Event("Hubo un error al recuperar los Tweets")

            }
        }
    }

    fun createTweet(message: String) {
        val requestNewTweet: RequestNewTweet = RequestNewTweet(message)
        viewModelScope.launch {
            _responseCreateTweet.value = repository.createTweet(requestNewTweet)
        }
    }
}
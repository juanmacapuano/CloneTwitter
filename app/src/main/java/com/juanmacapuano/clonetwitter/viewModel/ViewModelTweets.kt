package com.juanmacapuano.clonetwitter.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.juanmacapuano.clonetwitter.R
import com.juanmacapuano.clonetwitter.service.api.Resource
import com.juanmacapuano.clonetwitter.service.api.StatusResponseAPI
import com.juanmacapuano.clonetwitter.service.data.tweets.Like
import com.juanmacapuano.clonetwitter.service.data.tweets.RequestNewTweet
import com.juanmacapuano.clonetwitter.service.data.tweets.Tweet
import com.juanmacapuano.clonetwitter.service.data.tweets.User
import com.juanmacapuano.clonetwitter.service.repository.Repository
import com.juanmacapuano.clonetwitter.tools.handleApiError
import com.juanmacapuano.clonetwitter.tools.Event
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll

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

    private val _listTweets = MutableLiveData<List<Tweet>>()
    val listTweets: LiveData<List<Tweet>>
        get() = _listTweets

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

    fun setChange(selectedItem: Tweet) {
        viewModelScope.launch {
            val tweetLike = repository.setLike(selectedItem.id)
            val listAllTweet = _responseTweet.value
            when (tweetLike) {
                is Resource.Success -> {
                    val cloneList: ArrayList<Tweet> = arrayListOf()
                    when (listAllTweet) {
                        is Resource.Success -> {
                            val listAll = listAllTweet.value

                            for (item in listAll) {
                                if (item.id == selectedItem.id) {
                                    Log.d("clone: ", item.id.toString())
                                    cloneList.add(tweetLike.value)
                                } else {
                                    cloneList.add(item)
                                }
                            }
                        }
                    }
                    _listTweets.value = cloneList
                }
                else -> {
                    Log.d("setChange", "failure")
                }
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
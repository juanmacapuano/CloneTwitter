package com.juanmacapuano.clonetwitter.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.juanmacapuano.clonetwitter.service.repository.BaseRepository
import com.juanmacapuano.clonetwitter.service.repository.Repository
import com.juanmacapuano.clonetwitter.viewModel.ViewModelAuth
import com.juanmacapuano.clonetwitter.viewModel.ViewModelTweets
import java.lang.IllegalArgumentException

open class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ViewModelAuth::class.java) -> ViewModelAuth(repository as Repository) as T
            modelClass.isAssignableFrom(ViewModelTweets::class.java) -> ViewModelTweets(repository as Repository) as T
            else -> throw IllegalArgumentException("ViewModelClass not found")
        }
    }
}
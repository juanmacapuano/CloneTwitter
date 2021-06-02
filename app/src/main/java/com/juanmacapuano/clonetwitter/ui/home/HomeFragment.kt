package com.juanmacapuano.clonetwitter.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.juanmacapuano.clonetwitter.adapter.TweetListAdapter
import com.juanmacapuano.clonetwitter.databinding.FragmentHomeBinding
import com.juanmacapuano.clonetwitter.service.api.ApiSwagger
import com.juanmacapuano.clonetwitter.service.api.Resource
import com.juanmacapuano.clonetwitter.service.data.tweets.Tweet
import com.juanmacapuano.clonetwitter.service.repository.Repository
import com.juanmacapuano.clonetwitter.tools.handleApiError
import com.juanmacapuano.clonetwitter.ui.base.BaseFragment
import com.juanmacapuano.clonetwitter.viewModel.ViewModelTweets
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class HomeFragment : BaseFragment<ViewModelTweets, FragmentHomeBinding, Repository>() {

    lateinit var mAdapterTweetList: TweetListAdapter

    override fun getViewModel() = ViewModelTweets::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): Repository {
        val token = runBlocking {
            userPreferences.authToken.first()
        }
        return Repository(remoteDataSource.buildAPI(ApiSwagger::class.java, token), userPreferences)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapterTweetList = TweetListAdapter(
            { selectedItem: Tweet -> itemClicked(selectedItem) },
            requireContext()
        )

        //fragment's view lifecycle
        binding.lifecycleOwner = viewLifecycleOwner

        binding.rvTweetList.apply {
            adapter = mAdapterTweetList
            layoutManager = LinearLayoutManager(requireContext())
        }


        subscribeUI()

    }

    private fun subscribeUI() {
        viewModel.responseTweet.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    mAdapterTweetList.setListTweet(it.value)
                }
                is Resource.Failure -> handleApiError(it)
            }
        })
    }

    private fun itemClicked(selectedItem: Tweet) {
        val bundle = Bundle()
        //TODO falta ir al twwet seleccionado
    }
}
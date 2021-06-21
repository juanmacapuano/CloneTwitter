package com.juanmacapuano.clonetwitter.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.juanmacapuano.clonetwitter.R
import com.juanmacapuano.clonetwitter.adapter.TweetListAdapter
import com.juanmacapuano.clonetwitter.databinding.FragmentHomeBinding
import com.juanmacapuano.clonetwitter.service.api.ApiSwagger
import com.juanmacapuano.clonetwitter.service.api.Resource
import com.juanmacapuano.clonetwitter.service.api.StatusResponseAPI
import com.juanmacapuano.clonetwitter.service.data.tweets.Tweet
import com.juanmacapuano.clonetwitter.service.repository.Repository
import com.juanmacapuano.clonetwitter.tools.handleApiError
import com.juanmacapuano.clonetwitter.tools.visible
import com.juanmacapuano.clonetwitter.ui.base.BaseFragment
import com.juanmacapuano.clonetwitter.viewModel.ViewModelTweets
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class HomeFragment : BaseFragment<ViewModelTweets, FragmentHomeBinding, Repository>() {

    lateinit var mAdapterTweetList: TweetListAdapter
    lateinit var userNameString: String

    override fun getViewModel() = ViewModelTweets::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): Repository {
        val token = runBlocking {
            userPreferences.authToken.first()
        }
        userNameString = runBlocking {
            userPreferences.userName.first()
        } ?: "-"
        return Repository(remoteDataSource.buildAPI(ApiSwagger::class.java, token), userPreferences)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapterTweetList = TweetListAdapter(
            { selectedItem: Tweet -> itemClicked(selectedItem) },
            requireContext(), userNameString
        )

        //fragment's view lifecycle
        binding.lifecycleOwner = viewLifecycleOwner

        binding.rvTweetList.apply {
            adapter = mAdapterTweetList
            layoutManager = LinearLayoutManager(requireContext())
        }

        events()
        subscribeUI()
    }

    private fun events() {
        binding.faNewTweet.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_newTweetFragment)
        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.setRefreshData()
        }
    }

    private fun subscribeUI() {
        viewModel.responseTweet.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    mAdapterTweetList.setListTweet(it.value)
                    binding.swiperefresh.isRefreshing = false
                    binding.faNewTweet.visible(true)
                }
                is Resource.Failure -> handleApiError(it)
            }
        })

        viewModel.listTweets.observe(viewLifecycleOwner, Observer {
            mAdapterTweetList.setListTweet(it)
        })

        viewModel.statusResponseAPI.observe(viewLifecycleOwner, Observer {
            when (it) {
                StatusResponseAPI.LOADING -> {
                    binding.lpbStatusTweetList.visible(true)
                    binding.faNewTweet.visible(false)
                }
                StatusResponseAPI.DONE -> {
                    binding.lpbStatusTweetList.visible(false)
                    binding.faNewTweet.visible(true)
                }
                StatusResponseAPI.ERROR -> {
                    binding.lpbStatusTweetList.visible(false)
                    binding.faNewTweet.visible(false)
                }
            }
        })

        viewModel.responseLikeTweet.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Log.i("Like", it.value.user.userName)
                }
                is Resource.Failure -> handleApiError(it)
            }
        })
    }

    private fun itemClicked(selectedItem: Tweet) {
        val bundle = Bundle()
        viewModel.setChange(selectedItem)

        //TODO falta ir al twwet seleccionado
    }
}
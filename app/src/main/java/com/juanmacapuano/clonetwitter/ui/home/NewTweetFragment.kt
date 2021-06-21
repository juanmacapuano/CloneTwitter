package com.juanmacapuano.clonetwitter.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.juanmacapuano.clonetwitter.R
import com.juanmacapuano.clonetwitter.adapter.TweetListAdapter
import com.juanmacapuano.clonetwitter.databinding.FragmentHomeBinding
import com.juanmacapuano.clonetwitter.databinding.FragmentNewTweetBinding
import com.juanmacapuano.clonetwitter.service.api.ApiSwagger
import com.juanmacapuano.clonetwitter.service.api.Resource
import com.juanmacapuano.clonetwitter.service.data.tweets.Tweet
import com.juanmacapuano.clonetwitter.service.repository.Repository
import com.juanmacapuano.clonetwitter.tools.handleApiError
import com.juanmacapuano.clonetwitter.ui.base.BaseFragment
import com.juanmacapuano.clonetwitter.viewModel.ViewModelTweets
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class NewTweetFragment : BaseFragment<ViewModelTweets, FragmentNewTweetBinding, Repository>() {


    override fun getViewModel() = ViewModelTweets::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNewTweetBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): Repository {
        val token = runBlocking {
            userPreferences.authToken.first()
        }
        return Repository(remoteDataSource.buildAPI(ApiSwagger::class.java, token), userPreferences)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        events()
        subscribeUI()

        //fragment's view lifecycle
        binding.lifecycleOwner = viewLifecycleOwner

    }

    private fun subscribeUI() {
        viewModel.responseCreateTweet.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    findNavController().navigate(R.id.action_newTweetFragment_to_homeFragment)
                }
                is Resource.Failure -> {
                    handleApiError(it)
                }
            }
        })

    }

    private fun events() {
        binding.ivCloseNewTweet.setOnClickListener {
            findNavController().navigate(R.id.action_newTweetFragment_to_homeFragment)
        }

        binding.btnNewTweet.setOnClickListener {
            val textTweet = binding.etMessageNewTweet.text.toString()
            if (textTweet.isNotEmpty()) {
                viewModel.createTweet(textTweet)
            } else {
                Toast.makeText(context, "Debe ingresar un texto en el mensaje", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
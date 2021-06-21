package com.juanmacapuano.clonetwitter.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.juanmacapuano.clonetwitter.R
import com.juanmacapuano.clonetwitter.databinding.FragmentLoginBinding
import com.juanmacapuano.clonetwitter.service.api.ApiSwagger
import com.juanmacapuano.clonetwitter.service.api.Resource
import com.juanmacapuano.clonetwitter.service.api.StatusResponseAPI
import com.juanmacapuano.clonetwitter.service.data.auth.RequestLogin
import com.juanmacapuano.clonetwitter.service.repository.Repository
import com.juanmacapuano.clonetwitter.tools.enable
import com.juanmacapuano.clonetwitter.tools.handleApiError
import com.juanmacapuano.clonetwitter.tools.startNewActivity
import com.juanmacapuano.clonetwitter.tools.visible
import com.juanmacapuano.clonetwitter.ui.base.BaseFragment
import com.juanmacapuano.clonetwitter.ui.home.HomeActivity
import com.juanmacapuano.clonetwitter.viewModel.ViewModelAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private val viewModel: ViewModelAuth by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        events()
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.statusResponseAPI.observe(viewLifecycleOwner, { status: StatusResponseAPI ->
            when (status) {
                StatusResponseAPI.LOADING -> {
                    binding.pbLogin.visible(true)
                }
                StatusResponseAPI.DONE -> {
                    binding.pbLogin.visible(false)
                }
                StatusResponseAPI.ERROR -> {
                    binding.pbLogin.visible(false)
                }
            }
        })

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value.token)
                        viewModel.saveUserName(it.value.username)
                        requireActivity().startNewActivity(HomeActivity::class.java)
                    }
                }
                is Resource.Failure -> handleApiError(it)
            }
        })

        viewModel.message.observe(viewLifecycleOwner, Observer { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun events() {
        binding.tvRegistration.setOnClickListener(View.OnClickListener {
            goToRegister()
        })

        binding.btLogin.setOnClickListener(View.OnClickListener {
            doLogin(
                binding.etLayoutEmailLogin.editText?.text.toString().trim(),
                binding.etLayoutPasswordLogin.editText?.text.toString().trim()
            )
        })

        binding.btLogin.enable(false)

        binding.etPasswordLogin.addTextChangedListener {
            val email = binding.etLayoutEmailLogin.editText?.text.toString().trim()
            binding.btLogin.enable(email.isNotEmpty() && it.toString().isNotEmpty())
        }
    }

    private fun goToRegister() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun doLogin(email: String, password: String) {
        val requestLogin = RequestLogin(email, password)
        viewModel.doLogin(requestLogin)
    }


}
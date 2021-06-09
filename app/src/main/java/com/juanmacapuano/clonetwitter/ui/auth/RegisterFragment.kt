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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.juanmacapuano.clonetwitter.R
import com.juanmacapuano.clonetwitter.databinding.FragmentRegisterBinding
import com.juanmacapuano.clonetwitter.service.api.ApiSwagger
import com.juanmacapuano.clonetwitter.service.api.Resource
import com.juanmacapuano.clonetwitter.service.api.StatusResponseAPI
import com.juanmacapuano.clonetwitter.service.data.auth.RequestSignup
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

private val TAG = RegisterFragment::class.java.simpleName
private val CODE_REGISTER = "UDEMYANDROID"

@AndroidEntryPoint
class RegisterFragment: Fragment() {

    lateinit var binding : FragmentRegisterBinding
    private val viewModel : ViewModelAuth by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        events()
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.statusResponseAPI.observe(viewLifecycleOwner, { status: StatusResponseAPI ->
            when (status) {
                StatusResponseAPI.LOADING -> {
                    binding.pbRegister.visible(true)
                }
                StatusResponseAPI.DONE -> {
                    binding.pbRegister.visible(false)
                }
                StatusResponseAPI.ERROR -> {
                    binding.pbRegister.visible(false)
                }
            }
        })

        viewModel.registerResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value.token)
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
        binding.tvRegistrationRegister.setOnClickListener(View.OnClickListener {
            goToLogin()
        })

        binding.btRegister.setOnClickListener(View.OnClickListener {
            doRegister(binding.etLayoutNameRegister.editText?.text.toString().trim(),
                binding.etLayoutEmailRegister.editText?.text.toString().trim(),
                binding.etLayoutPasswordRegister.editText?.text.toString().trim())
        })

        binding.etPasswordRegister.addTextChangedListener {
            val name = binding.etLayoutNameRegister.editText?.text.toString().trim()
            val email = binding.etLayoutEmailRegister.editText?.text.toString().trim()
            binding.btRegister.enable(name.isNotEmpty() && email.isNotEmpty() && it.toString().isNotEmpty())
        }
    }

    private fun doRegister(name: String, email: String, password: String) {
        val requestSignup = RequestSignup(name, email, password, CODE_REGISTER)
        viewModel.doRegister(requestSignup)
    }

    private fun goToLogin() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }
}
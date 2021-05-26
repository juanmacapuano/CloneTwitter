package com.juanmacapuano.clonetwitter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.juanmacapuano.clonetwitter.R
import com.juanmacapuano.clonetwitter.databinding.FragmentLoginBinding
import com.juanmacapuano.clonetwitter.service.api.Resource
import com.juanmacapuano.clonetwitter.service.api.StatusResponseAPI
import com.juanmacapuano.clonetwitter.service.data.RequestLogin
import com.juanmacapuano.clonetwitter.viewModel.ViewModelAuth

class LoginFragment: Fragment() {

    lateinit var binding: FragmentLoginBinding
    private val viewModelAuth: ViewModelAuth by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        events()
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModelAuth.statusResponseAPI.observe(viewLifecycleOwner, { status: StatusResponseAPI ->
            when (status) {
                StatusResponseAPI.LOADING -> {
                    binding.pbLogin.visibility = View.VISIBLE
                }
                StatusResponseAPI.DONE -> {
                    binding.pbLogin.visibility = View.GONE
                }
                StatusResponseAPI.ERROR -> {
                    binding.pbLogin.visibility = View.GONE
                }
            }
        })

        viewModelAuth.loginResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Login Failure", Toast.LENGTH_SHORT).show()
                }
            }


        })
    }

    private fun events() {
        binding.tvRegistration.setOnClickListener (View.OnClickListener {
            goToRegister()
        })

        binding.btLogin.setOnClickListener(View.OnClickListener {
            doLogin(binding.etLayoutEmailLogin.editText?.text.toString().trim(), binding.etLayoutPasswordLogin.editText?.text.toString().trim())
        })
    }

    private fun goToRegister() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun doLogin(email: String, password: String) {
        val requestLogin = RequestLogin(email, password)
        viewModelAuth.doLogin(requestLogin)

    }
}
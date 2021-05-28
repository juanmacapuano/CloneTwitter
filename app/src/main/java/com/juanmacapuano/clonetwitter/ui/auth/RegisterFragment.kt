package com.juanmacapuano.clonetwitter.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.juanmacapuano.clonetwitter.R
import com.juanmacapuano.clonetwitter.databinding.FragmentLoginBinding
import com.juanmacapuano.clonetwitter.databinding.FragmentRegisterBinding
import com.juanmacapuano.clonetwitter.service.api.ApiSwagger
import com.juanmacapuano.clonetwitter.service.api.Resource
import com.juanmacapuano.clonetwitter.service.api.StatusResponseAPI
import com.juanmacapuano.clonetwitter.service.data.RequestSignup
import com.juanmacapuano.clonetwitter.service.repository.Repository
import com.juanmacapuano.clonetwitter.tools.enable
import com.juanmacapuano.clonetwitter.tools.visible
import com.juanmacapuano.clonetwitter.ui.base.BaseFragment
import com.juanmacapuano.clonetwitter.viewModel.ViewModelAuth

private val TAG = RegisterFragment::class.java.simpleName
private val CODE_REGISTER = "UDEMYANDROID"

class RegisterFragment: BaseFragment<ViewModelAuth, FragmentRegisterBinding, Repository>() {

    override fun getViewModel() = ViewModelAuth::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = Repository(remoteDataSource.buildAPI(ApiSwagger::class.java), userPreferences)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), R.string.register_error, Toast.LENGTH_SHORT).show()
                }
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
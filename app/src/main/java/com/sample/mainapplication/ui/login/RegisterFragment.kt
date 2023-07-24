package com.sample.mainapplication.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.sample.mainapplication.R
import com.sample.mainapplication.networking.LoginResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var loginText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usernameEditText = view.findViewById(R.id.username)
        passwordEditText = view.findViewById(R.id.password)
        submitButton = view.findViewById(R.id.register)
        loginText = view.findViewById(R.id.login_textview)
        setRegistrationObserver()
        setUpGoToLoginButton()
        setUpRegisterButton()
    }

    private fun setUpRegisterButton() {
        submitButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            viewModel.onRegister(
                email = username,
                password = password,
            )
        }
    }

    private fun setRegistrationObserver() {
        viewModel.registrationResult.observe(viewLifecycleOwner) { registrationResult ->
            when (registrationResult) {
                is LoginResult.MISSING_USERNAME -> {
                    Toast.makeText(this.context, registrationResult.text, Toast.LENGTH_LONG).show()
                }
                is LoginResult.MISSING_PASSWORD -> {
                    Toast.makeText(this.context, registrationResult.text, Toast.LENGTH_LONG).show()
                }
                is LoginResult.ERROR -> {
                    Toast.makeText(this.context, registrationResult.text, Toast.LENGTH_LONG).show()
                }
                LoginResult.SUCCESS -> {
                    val action = RegisterFragmentDirections.actionRegisterFragmentToFirstFragment()
                    view?.findNavController()?.navigate(action)
                }
            }
        }
    }

    private fun setUpGoToLoginButton() {
        loginText.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            view?.findNavController()?.navigate(action)
        }
    }
}
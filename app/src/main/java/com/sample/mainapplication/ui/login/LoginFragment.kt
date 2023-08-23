package com.sample.mainapplication.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.sample.mainapplication.R
import com.sample.mainapplication.networking.LoginResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usernameEditText = view.findViewById(R.id.username)
        passwordEditText = view.findViewById(R.id.password)
        loginButton = view.findViewById(R.id.login_button)
        setLoginObserver()
        setupLoginButton()
    }

    private fun setLoginObserver() {
        viewModel.loginResult.observe(viewLifecycleOwner) { loginResult ->
            when (loginResult) {
                is LoginResult.MISSING_USERNAME -> {
                    Toast.makeText(this.context, loginResult.text, Toast.LENGTH_LONG).show()
                }
                is LoginResult.MISSING_PASSWORD -> {
                    Toast.makeText(this.context, loginResult.text, Toast.LENGTH_LONG).show()
                }
                is LoginResult.ERROR -> {
                    Toast.makeText(this.context, loginResult.text, Toast.LENGTH_LONG).show()
                }
                LoginResult.SUCCESS -> {
                    val action = LoginFragmentDirections.actionLoginFragmentToChatroomFragment()
                    view?.findNavController()?.navigate(action)
                }
            }
        }
    }

    private fun setupLoginButton() {
        loginButton.setOnClickListener {
            val email = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            viewModel.onLogin(email, password)
        }
    }
}
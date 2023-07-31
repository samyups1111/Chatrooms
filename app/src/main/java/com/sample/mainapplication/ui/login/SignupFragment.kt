package com.sample.mainapplication.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.sample.mainapplication.R
import com.sample.mainapplication.networking.LoginResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private lateinit var nameTextView : TextView
    private lateinit var submitButton : Button
    private val vm : SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameTextView = view.findViewById(R.id.name_edit_text)
        submitButton = view.findViewById(R.id.submit_button)

        submitButton.setOnClickListener {
            val name = nameTextView.text.toString()
            vm.onSignup(name)
        }

        vm.state.observe(viewLifecycleOwner) { result ->
            when (result) {
                is LoginResult.SUCCESS -> {
                    val action = SignupFragmentDirections.actionSignupFragmentToFirstFragment()
                    view.findNavController().navigate(action)
                }
                is LoginResult.ERROR -> {
                    Toast.makeText(this.context, result.text, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }
}
package com.sample.mainapplication.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sample.mainapplication.R

class SplashScreenFragment : Fragment() {

    val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val action = if (auth.currentUser == null) {
            SplashScreenFragmentDirections.actionSplashScreenFragmentToRegisterFragment()
        } else {
            SplashScreenFragmentDirections.actionSplashScreenFragmentToFirstFragment()
        }
        view.findNavController().navigate(action)
    }
}
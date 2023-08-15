package com.sample.mainapplication.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.sample.mainapplication.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var signoutButton : Button
    private lateinit var nameEditText : EditText
    private lateinit var saveButton : Button
    private val vm: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signoutButton = view.findViewById(R.id.sign_out_button)
        nameEditText = view.findViewById(R.id.name_edit_text)
        saveButton = view.findViewById(R.id.save_button)
        signoutButton.setOnClickListener {
            vm.signOut()
            val action = ProfileFragmentDirections.actionProfileFragmentToRegisterFragment()
            view.findNavController().navigate(action)
        }
        nameEditText.setOnClickListener {
            saveButton.visibility = View.VISIBLE
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            vm.updateUser(name)
            it.visibility = View.GONE
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.userFlow.collect{user ->
                    nameEditText.setText(user.userName)
                }
            }
        }
    }
}
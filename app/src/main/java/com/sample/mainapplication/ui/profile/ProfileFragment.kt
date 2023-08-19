package com.sample.mainapplication.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sample.mainapplication.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var signoutButton : Button
    private lateinit var nameEditText : EditText
    private lateinit var saveButton : Button
    private lateinit var userProfileImageView: ImageView
    private val vm: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userProfileImageView = view.findViewById(R.id.user_icon_imageview)
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
        userProfileImageView.setOnClickListener {
            selectImageFromGallery()
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.userFlow.collect{ user ->
                    nameEditText.setText(user.userName)
                    user.profileImgUri?.let { profileImgUri ->
                        Glide
                            .with(requireContext())
                            .load(profileImgUri)
                            .centerCrop()
                            .apply(RequestOptions.circleCropTransform())
                            .into(userProfileImageView)
                    }
                }
            }
        }
    }


    private fun selectImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Please select..."
            ),
            GALLERY_REQUEST_CODE
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == GALLERY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data != null
            && data.data != null
        ) {

            // Get the Uri of data
            val localProfileImgUri = data.data
            vm.savelocalProfileImgUriToFirebase(localProfileImgUri!!)
        }
    }

    companion object {
        const val GALLERY_REQUEST_CODE = 0
    }
}
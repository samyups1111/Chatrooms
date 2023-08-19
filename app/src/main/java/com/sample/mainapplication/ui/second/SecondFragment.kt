package com.sample.mainapplication.ui.second

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.sample.mainapplication.R

class SecondFragment : Fragment() {

    private val viewModel: SecondFragmentViewModel by viewModels()
    lateinit var nameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameTextView = view.findViewById(R.id.name)
        val args: SecondFragmentArgs by navArgs()
        nameTextView.text = args.name
        nameTextView.setOnClickListener {
            val action = SecondFragmentDirections.actionSecondFragmentToMessageFragment(args.name)
            view.findNavController().navigate(action)
        }
    }
}
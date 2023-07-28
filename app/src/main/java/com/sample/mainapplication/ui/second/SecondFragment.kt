package com.sample.mainapplication.ui.second

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.sample.mainapplication.R
import com.sample.mainapplication.ui.main.MainViewModel

class SecondFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    lateinit var nameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameTextView = view.findViewById(R.id.name)
        val args: SecondFragmentArgs by navArgs()
        val mainData = viewModel.getMainData(args.name)
        nameTextView.text = mainData.name

        nameTextView.setOnClickListener {
            val action = SecondFragmentDirections.actionSecondFragmentToChatFragment(mainData.name)
            view.findNavController().navigate(action)
        }
    }
}
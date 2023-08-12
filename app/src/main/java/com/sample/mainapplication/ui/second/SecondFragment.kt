package com.sample.mainapplication.ui.second

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
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
        //setSelectedPokemonObserver(view)
        val args: SecondFragmentArgs by navArgs()
        nameTextView.text = args.name
        nameTextView.setOnClickListener {
            val action = SecondFragmentDirections.actionSecondFragmentToChatFragment(args.name)
            view.findNavController().navigate(action)
        }
        //viewModel.getPokemonByName(args.name)
        //viewModel.pokemonSelected(args.name)

    }

    private fun setSelectedPokemonObserver(view: View) {

        viewModel.selectedPokemon.observe(viewLifecycleOwner) { mainData ->
            nameTextView.text = mainData.name

            nameTextView.setOnClickListener {
                val action = SecondFragmentDirections.actionSecondFragmentToChatFragment(mainData.name)
                view.findNavController().navigate(action)
            }
        }

    }

//    private fun setSelectedPokemonObserver(view: View) {
//        nameTextView = view.findViewById(R.id.name)
//
//        viewModel.selectedPokemon.observe(viewLifecycleOwner) { mainData ->
//            nameTextView.text = mainData.name
//
//            nameTextView.setOnClickListener {
//                val action = SecondFragmentDirections.actionSecondFragmentToChatFragment(mainData.name)
//                view.findNavController().navigate(action)
//            }
//        }
//        //nameTextView.text = viewModel.currPoke.name
//    }

//    private fun setPokemon() {
//        val poke = viewModel.currPoke
//        nameTextView.textSize = poke.name
//    }
}
package com.sample.mainapplication.ui.second

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.mainapplication.model.MainData
import com.sample.mainapplication.model.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecondFragmentViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
): ViewModel() {

    private val _selectedPokemon = MutableLiveData<MainData>()
    val selectedPokemon: LiveData<MainData> = _selectedPokemon

    fun getPokemonByName(name: String) {
        viewModelScope.launch {
            _selectedPokemon.value = pokemonRepository.getPokemonByName(name)
        }
    }
}
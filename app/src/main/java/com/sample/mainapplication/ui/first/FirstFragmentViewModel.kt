package com.sample.mainapplication.ui.first

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.mainapplication.model.MainData
import com.sample.mainapplication.networking.NetworkResult
import com.sample.mainapplication.model.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FirstFragmentViewModel @Inject constructor(
    private val repository: PokemonRepository
): ViewModel() {

    private val _mainDataList = MutableLiveData<List<MainData>>()
    val mainDataList: LiveData<List<MainData>> = _mainDataList

    init {
        getMainDataList()
    }

    @VisibleForTesting
    fun getMainDataList() {

        viewModelScope.launch {
            var result : NetworkResult<List<MainData>>
            withContext(Dispatchers.IO) { result = repository.getMainDataList() }

            _mainDataList.value =  when (result) {
                is NetworkResult.Success -> (result as NetworkResult.Success<List<MainData>>).data
                is NetworkResult.Error -> emptyList()
                is NetworkResult.Exception -> emptyList()
            }
        }
    }

    fun getFilteredList(newText: String?): List<MainData> {
        return mainDataList.value!!.filter {
            newText.toString() in it.name
        }
    }
}
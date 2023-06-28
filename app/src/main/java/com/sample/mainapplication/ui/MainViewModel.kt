package com.sample.mainapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.mainapplication.model.MainData
import com.sample.mainapplication.networking.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    private val _mainDataList = MutableLiveData<List<MainData>>()
    val mainDataList: LiveData<List<MainData>> = _mainDataList

    init {
        viewModelScope.launch {
            getMainDataList()
        }
    }

    private suspend fun getMainDataList() {

        val result = repository.getMainDataList()

        _mainDataList.value =  when (result) {
            is NetworkResult.Success -> result.data
            is NetworkResult.Error -> emptyList()
            is NetworkResult.Exception -> emptyList()
        }
    }

    fun getMainData(index: Int): MainData {
        return mainDataList.value!![index]
    }
}
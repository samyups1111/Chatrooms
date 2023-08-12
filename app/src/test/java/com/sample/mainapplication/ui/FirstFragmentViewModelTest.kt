package com.sample.mainapplication.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sample.mainapplication.model.MainData
import com.sample.mainapplication.networking.NetworkResult
import com.sample.mainapplication.model.PokemonRepository
import com.sample.mainapplication.ui.first.FirstFragmentViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
internal class FirstFragmentViewModelTest {

    private lateinit var pokemonRepository: PokemonRepository
    private lateinit var vm: FirstFragmentViewModel

    @Before
    fun setup() {
        pokemonRepository = mockk()
        coEvery { pokemonRepository.getMainDataList() } returns NetworkResult.Success(mockMainDataList)
        vm = FirstFragmentViewModel(pokemonRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun getMainDataList_SUCCESS() = runTest {
        vm.getMainDataList()
        Assert.assertEquals(mockMainDataList, vm.mainDataList.value)
    }

    @Test
    fun getMainData() {
        val expected = "first"
        val actual = vm.getMainData(expected).name
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun getFilteredList() {
        val expected = listOf(
            MainData("first"),
            MainData("third"),
        )
        val actual = vm.getFilteredList("ir")
        Assert.assertEquals(expected, actual)
    }

    companion object {

        val mockMainDataList = listOf(
            MainData("first"),
            MainData("second"),
            MainData("third"),
        )
    }
}
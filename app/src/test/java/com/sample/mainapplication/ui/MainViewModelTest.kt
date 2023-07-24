package com.sample.mainapplication.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sample.mainapplication.model.MainData
import com.sample.mainapplication.networking.NetworkResult
import com.sample.mainapplication.ui.main.MainRepository
import com.sample.mainapplication.ui.main.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
internal class MainViewModelTest {

    private lateinit var mainRepository: MainRepository
    private lateinit var vm: MainViewModel

    @Before
    fun setup() {
        mainRepository = mockk()
        coEvery { mainRepository.getMainDataList() } returns NetworkResult.Success(mockMainDataList)
        vm = MainViewModel(mainRepository)
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
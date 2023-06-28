package com.sample.mainapplication.ui

import com.sample.mainapplication.model.MainData
import com.sample.mainapplication.networking.MainService
import com.sample.mainapplication.networking.NetworkResult
import retrofit2.HttpException
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val mainService: MainService,
){

    suspend fun getMainDataList(): NetworkResult<List<MainData>> {

        return try {
            val response = mainService.getMyDataList()
            val body = response.body()

            if (response.isSuccessful && body != null) {
                NetworkResult.Success(body.results)
            } else {
                NetworkResult.Error(
                    code = response.code(),
                    message = response.message(),
                )
            }
        } catch (e: HttpException) {
            NetworkResult.Error(
                code = e.code(),
                message = e.message,
            )
        } catch (e: Throwable) {
            NetworkResult.Exception(e)
        }
    }
}
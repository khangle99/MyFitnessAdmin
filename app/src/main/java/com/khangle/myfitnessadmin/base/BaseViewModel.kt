package com.khangle.myfitnessadmin.base

import androidx.lifecycle.ViewModel
import com.khangle.myfitnessadmin.model.ResponseMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

open class BaseViewModel: ViewModel() {

     suspend fun handleResponse(handle: (ResponseMessage) -> Unit, body:suspend ()  -> Unit) {
        try {
            body()
        } catch (error: HttpException) {
            val string = error.response()?.errorBody()?.string()
            withContext(Dispatchers.Main) {
                handle(ResponseMessage(error = string ?: "", id = null))
            }
        }
    }
}
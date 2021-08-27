package com.khangle.myfitnessadmin.nutrition.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.myfitnessadmin.base.BaseViewModel
import com.khangle.myfitnessadmin.data.MyFitnessRepository
import com.khangle.myfitnessadmin.model.Excercise
import com.khangle.myfitnessadmin.model.Menu
import com.khangle.myfitnessadmin.model.ResponseMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MenuDetailVM @Inject constructor(private val repository: MyFitnessRepository) : BaseViewModel() {
    fun createMenu(
        nutriId: String,
        menu: Menu,
        uriStringList: List<String>,
        handle: (ResponseMessage) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(handle) {
                val res = repository.postMenu(nutriId, menu, uriStringList)
                withContext(Dispatchers.Main) {
                    handle(res)
                }
            }
        }
    }

    fun updateMenu(
        nutriId: String,
        id: String,
        menu: Menu,
        uriStringList: List<String>?,
        handle: (ResponseMessage) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(handle) {
                val res = repository.updateMenu(id, nutriId, menu, uriStringList)
                withContext(Dispatchers.Main) {
                    handle(res)
                }
            }
        }
    }

    fun deleteMenu(nutriId: String, id: String, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(handle) {
                val res = repository.deleteMenu(id, nutriId)
                withContext(Dispatchers.Main) {
                    handle(res)
                }
            }
        }
    }
}
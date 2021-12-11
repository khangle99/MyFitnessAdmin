package com.khangle.myfitnessadmin.excercise.excdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.myfitnessadmin.base.BaseViewModel
import com.khangle.myfitnessadmin.data.MyFitnessRepository
import com.khangle.myfitnessadmin.model.BodyStat
import com.khangle.myfitnessadmin.model.Excercise
import com.khangle.myfitnessadmin.model.ResponseMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ExcerciseDetailVM @Inject constructor(private val repository: MyFitnessRepository) :
    BaseViewModel() {

    private var _bodyStatList =  MutableLiveData<List<BodyStat>>()
    val bodyStatList: LiveData<List<BodyStat>> = _bodyStatList
    fun getBodyStatList() {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse {
                _bodyStatList.postValue(repository.getBodyStat())
            }
        }
    }

    fun createExcercise(
        catId: String,
        excercise: Excercise,
        uriStringList: List<String>,
        handle: (ResponseMessage) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
          handleResponse(handle) {
              val res = repository.postExcercise(catId, excercise, uriStringList)
              withContext(Dispatchers.Main) {
                  handle(res)
              }
          }
        }
    }

    fun updateExcercise(
        catId: String,
        id: String,
        excercise: Excercise,
        uriStringList: List<String>?,
        handle: (ResponseMessage) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(handle) {
                val res = repository.updateExcercise(id, catId, excercise, uriStringList)
                withContext(Dispatchers.Main) {
                    handle(res)
                }
            }
        }
    }

    fun deleteExcercise(catId: String, id: String, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
           handleResponse(handle) {
               val res = repository.deleteExcercise(id, catId)
               withContext(Dispatchers.Main) {
                   handle(res)
               }
           }
        }
    }
}
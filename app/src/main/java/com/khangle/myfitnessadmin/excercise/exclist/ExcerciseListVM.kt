package com.khangle.myfitnessadmin.excercise.exclist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.khangle.myfitnessadmin.base.BaseViewModel
import com.khangle.myfitnessadmin.data.MyFitnessRepository
import com.khangle.myfitnessadmin.model.Excercise
import com.khangle.myfitnessadmin.model.ExcerciseCategory
import com.khangle.myfitnessadmin.model.ResponseMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ExcerciseListVM @Inject constructor(private val myFitnessRepository: MyFitnessRepository) :
    BaseViewModel() {
    private var _excList = MutableLiveData<List<Excercise>>()
    val excerciseList: LiveData<List<Excercise>> = _excList
    fun loadList(categoryId: String) {
        viewModelScope.launch(Dispatchers.IO) {
         handleResponse {
             val list = myFitnessRepository.loadExcerciseList(categoryId)

             val excercises = list.map { exc ->
                 val a = async {
                     val ensure =  myFitnessRepository.getStatEnsureList(exc.id,categoryId)
                     //   exc.achieveEnsure =  ensure
                     exc.achieveEnsure = Gson().toJson(ensure)
                     exc
                 }
                 a
             }.awaitAll()
             _excList.postValue(excercises)
             excercises.forEach {
                 val a = it.achieveEnsure
                 print("")
             }
         }
        }
    }

    fun createExcerciseCategory(
        name: String,
        uriString: String,
        handle: (ResponseMessage) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(handle) {
                val res = myFitnessRepository.postExcerciseCategory(name, uriString)
                withContext(Dispatchers.Main) {
                    handle(res)
                }
            }
        }
    }

    fun updateCategory(
        id: String,
        name: String,
        uriString: String?,
        handle: (ResponseMessage) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {

           handleResponse(handle) {
               val res = myFitnessRepository.updateExcerciseCategory(id, name, uriString)
               withContext(Dispatchers.Main) {
                   handle(res)
               }
           }

        }
    }

    fun deleteCategory(id: String, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
           handleResponse(handle) {
               val res = myFitnessRepository.deleteExcerciseCategory(id)
               withContext(Dispatchers.Main) {
                   handle(res)
               }
           }
        }
    }
}
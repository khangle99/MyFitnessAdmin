package com.khangle.myfitnessadmin.suggestpack.plandetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.khangle.myfitnessadmin.base.BaseViewModel
import com.khangle.myfitnessadmin.data.MyFitnessRepository
import com.khangle.myfitnessadmin.model.Excercise
import com.khangle.myfitnessadmin.model.PlanDay
import com.khangle.myfitnessadmin.model.ResponseMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class PlanDetailViewModel @Inject constructor(private val repository: MyFitnessRepository): BaseViewModel() {
    private var _dayList = MutableLiveData<List<PlanDay>>()
    val dayList: LiveData<List<PlanDay>> = _dayList
    fun loadList(sugId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.loadDayList(sugId).map {
                async {
                    it.exc = repository.getExcercise(it.categoryId, it.excId)
                    val ensure =  repository.getStatEnsureList(it.excId,it.categoryId)
                    it.exc!!.achieveEnsure = Gson().toJson(ensure)
                    it.day = it.id // do cung value
                    it
                }
            }.awaitAll()
            _dayList.postValue(list)
        }
    }

    fun createPlan(
        name: String,
        handle: (ResponseMessage) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(handle) {
                val res = repository.postPlan(name)
                withContext(Dispatchers.Main) {
                    handle(res)
                }
            }
        }
    }

    fun updatePlan(
        id: String,
        name: String,
        handle: (ResponseMessage) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            handleResponse(handle) {
                val res = repository.updatePlan(id, name)
                withContext(Dispatchers.Main) {
                    handle(res)
                }
            }
        }
    }

    fun deletePlan(id: String, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(handle) {
                val res = repository.deletePlan(id)
                withContext(Dispatchers.Main) {
                    handle(res)
                }
            }
        }
    }

    // plan day

}
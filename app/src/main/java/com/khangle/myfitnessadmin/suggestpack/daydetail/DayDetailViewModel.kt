package com.khangle.myfitnessadmin.suggestpack.daydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.khangle.myfitnessadmin.base.BaseViewModel
import com.khangle.myfitnessadmin.data.MyFitnessRepository
import com.khangle.myfitnessadmin.model.Plan
import com.khangle.myfitnessadmin.model.PlanDay
import com.khangle.myfitnessadmin.model.ResponseMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class DayDetailViewModel @Inject constructor(private val repository: MyFitnessRepository): BaseViewModel(){
    private var _dayPlan = MutableLiveData<PlanDay>()
    var dayPlan: LiveData<PlanDay> = _dayPlan
    var notExistDayString = mutableListOf<String>()

    private var _planList =  MutableLiveData<List<Plan>>()
    val planList: LiveData<List<Plan>> = _planList
    fun getPlanList() {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse {
                _planList.postValue(repository.getSuggestPlans())
            }
        }

    }

    fun updatePlanDay(sugId: String, categoryId: String, excId: String, day: String, oldDay: String, handle: (ResponseMessage) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(handle) {
                val res = repository.updatePlanDay(sugId,categoryId,excId,day,oldDay)
                withContext(Dispatchers.Main) {
                    handle(res)
                }
            }
        }
    }

    fun deletePlanDay(sugId: String,day: String, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(handle) {
                val res = repository.deletePlanDay(sugId, day)
                withContext(Dispatchers.Main) {
                    handle(res)
                }
            }
        }
    }



}
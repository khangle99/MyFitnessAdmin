package com.khangle.myfitnessadmin.nutrition.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.myfitnessadmin.data.MyFitnessRepository
import com.khangle.myfitnessadmin.model.BodyStat
import com.khangle.myfitnessadmin.model.ResponseMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BodyStatVM @Inject constructor(private val repository: MyFitnessRepository): ViewModel() {

    private var _bodyStatList =  MutableLiveData<List<BodyStat>>()
    val bodyStatList: LiveData<List<BodyStat>> = _bodyStatList
    fun getBodyStatList() {
        viewModelScope.launch(Dispatchers.IO) {
            _bodyStatList.postValue(repository.getBodyStat())
        }
    }

    fun deleteBodyStat(bodyStat: BodyStat, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.deleteBodyStat(bodyStat.id)
            withContext(Dispatchers.Main) {
                handle(res)
            }
        }
    }

    fun postBodyStat(name: String, dataType: String, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.postBodyStat(name, dataType)
            withContext(Dispatchers.Main) {
                handle(res)
            }
        }
    }
}
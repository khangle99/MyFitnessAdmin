package com.khangle.myfitnessadmin.excercise.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.myfitnessadmin.base.BaseViewModel
import com.khangle.myfitnessadmin.data.MyFitnessRepository
import com.khangle.myfitnessadmin.model.ExcerciseCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExcerciseCategoryVM @Inject constructor(private val repository: MyFitnessRepository): BaseViewModel() {
    private var _categoryList =  MutableLiveData<List<ExcerciseCategory>>()
    val categoryList: LiveData<List<ExcerciseCategory>> = _categoryList
    fun getCategoryList() {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse {
                _categoryList.postValue(repository.getExcerciseCategory())
            }
        }
    }
}
package com.khangle.myfitnessadmin.nutrition.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.myfitnessadmin.data.MyFitnessRepository
import com.khangle.myfitnessadmin.model.ExcerciseCategory
import com.khangle.myfitnessadmin.model.NutritionCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutritionCategoryVM @Inject constructor(private val repository: MyFitnessRepository): ViewModel() {
    private var _categoryList =  MutableLiveData<List<NutritionCategory>>()
    val categoryList: LiveData<List<NutritionCategory>> = _categoryList
    fun getCategoryList() {
        viewModelScope.launch(Dispatchers.IO) {
            _categoryList.postValue(repository.getNutritionCategory())
        }
    }
}
package com.khangle.myfitnessadmin.report.mostusedexcercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.myfitnessadmin.base.BaseViewModel
import com.khangle.myfitnessadmin.data.MyFitnessRepository
import com.khangle.myfitnessadmin.model.Excercise
import com.khangle.myfitnessadmin.model.ExcerciseCategory
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MostUseExcerciewViewModel @Inject constructor(private val repository: MyFitnessRepository): BaseViewModel() {

    private var _allExcercise = MutableLiveData<List<Excercise>>()
    val allExcercise: LiveData<List<Excercise>> = _allExcercise

    fun fetchAllExcercise() {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse {
                val allExc = repository.getAllExcercise()
                _allExcercise.postValue( allExc.sortedByDescending { it.addedCount })
            }
        }
    }


}
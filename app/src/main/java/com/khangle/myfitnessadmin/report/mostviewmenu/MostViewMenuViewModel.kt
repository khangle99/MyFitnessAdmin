package com.khangle.myfitnessadmin.report.mostviewmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.myfitnessadmin.data.MyFitnessRepository
import com.khangle.myfitnessadmin.model.Excercise
import com.khangle.myfitnessadmin.model.Menu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MostViewMenuViewModel @Inject constructor(private val repository: MyFitnessRepository): ViewModel() {
    private var _allMenu = MutableLiveData<List<Menu>>()
    val allMenu: LiveData<List<Menu>> = _allMenu

    fun fetchAllMenu() {
        viewModelScope.launch(Dispatchers.IO) {
            val allExc = repository.getAllMenu()
            _allMenu.postValue( allExc.sortedByDescending { it.view })
        }
    }
}
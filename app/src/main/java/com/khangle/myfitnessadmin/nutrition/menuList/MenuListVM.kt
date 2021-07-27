package com.khangle.myfitnessadmin.nutrition.menuList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.myfitnessadmin.data.MyFitnessRepository
import com.khangle.myfitnessadmin.model.Menu
import com.khangle.myfitnessadmin.model.ResponseMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MenuListVM @Inject constructor(private val repository: MyFitnessRepository) : ViewModel() {
    private var _menuList = MutableLiveData<List<Menu>>()
    val menuList: LiveData<List<Menu>> = _menuList

    fun loadList(nutriId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.loadMenuList(nutriId)
            _menuList.postValue(list)
        }
    }

    fun createNutritionCategory(
        name: String,
        uriString: String,
        handle: (ResponseMessage) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.postNutritionCategory(name, uriString)
            withContext(Dispatchers.Main) {
                handle(res)
            }
        }
    }

    fun updateNutritionCategory(
        id: String,
        name: String,
        uriString: String?,
        handle: (ResponseMessage) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.updateNutritionCategory(id, name, uriString)
            withContext(Dispatchers.Main) {
                handle(res)
            }
        }
    }

    fun deleteNutritionCategory(id: String, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.deleteNutritionCategory(id)
            withContext(Dispatchers.Main) {
                handle(res)
            }
        }
    }
}
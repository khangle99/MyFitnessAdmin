package com.khangle.myfitnessadmin.data

import android.content.Context
import androidx.core.net.toUri
import com.khangle.myfitnessadmin.data.network.MyFitnessService
import com.khangle.myfitnessadmin.model.*
import com.khangle.myfitnessadmin.util.FileUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


class MyFitnessRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val myFitnessService: MyFitnessService
) {
    ///////// excercise

    suspend fun getAllExcercise(): List<Excercise> {
        return myFitnessService.fetchAllExcercise()
    }

    suspend fun getExcerciseCategory(): List<ExcerciseCategory> {
        return myFitnessService.fetchExcerciseCategory()
    }

    suspend fun postExcerciseCategory(name: String, photoPath: String): ResponseMessage {
        val uri = photoPath.toUri()
        val file = FileUtil.from(context, uri)
        val fileRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("photo", file.name, fileRequestBody)
        val catName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        return myFitnessService.postExcerciseCategory(part, catName)
    }

    suspend fun updateExcerciseCategory(
        id: String,
        name: String,
        photoPath: String?
    ): ResponseMessage {
        val photoList = mutableListOf<MultipartBody.Part>()
        if (photoPath != null) {
            val uri = photoPath.toUri()
            val file = FileUtil.from(context, uri)
            val fileRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("photo", file.name, fileRequestBody)
            photoList.add(part)
        }
        val catName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val catId = id.toRequestBody("text/plain".toMediaTypeOrNull())
        return myFitnessService.updateExcerciseCategory(photoList, catName, catId)
    }

    suspend fun deleteExcerciseCategory(id: String): ResponseMessage {
        return myFitnessService.deleteExcerciseCategory(id)
    }

    suspend fun loadExcerciseList(catId: String): List<Excercise> {
        return myFitnessService.fetchExcerciseList(catId)
    }

    suspend fun postExcercise(
        catId: String,
        excercise: Excercise,
        photoPathList: List<String>
    ): ResponseMessage {
        val photoList = mutableListOf<MultipartBody.Part>()
        photoPathList.forEach {
            val uri = it.toUri()
            val file = FileUtil.from(context, uri)
            val fileRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("picSteps", file.name, fileRequestBody)
            photoList.add(part)
        }
        val excName = excercise.name.toRequestBody("text/plain".toMediaTypeOrNull())
        val excDiff = excercise.difficulty.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val excEquip = excercise.equipment.toRequestBody("text/plain".toMediaTypeOrNull())
        val excTutor = excercise.tutorial.toRequestBody("text/plain".toMediaTypeOrNull())
        val catIdRequestBody = catId.toRequestBody("text/plain".toMediaTypeOrNull())
        return myFitnessService.postExcercise(
            photoList,
            excName,
            excDiff,
            excEquip,
            excTutor,
            catIdRequestBody
        )
    }

    suspend fun updateExcercise(
        id: String,
        catId: String,
        excercise: Excercise,
        photoPathList: List<String>?
    ): ResponseMessage {
        val photoList = mutableListOf<MultipartBody.Part>()
        photoPathList?.forEach {
            val uri = it.toUri()
            val file = FileUtil.from(context, uri)
            val fileRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("picSteps", file.name, fileRequestBody)
            photoList.add(part)
        }
        val excName = excercise.name.toRequestBody("text/plain".toMediaTypeOrNull())
        val excDiff = excercise.difficulty.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val excEquip = excercise.equipment.toRequestBody("text/plain".toMediaTypeOrNull())
        val excTutor = excercise.tutorial.toRequestBody("text/plain".toMediaTypeOrNull())
        val catIdRequestBody = catId.toRequestBody("text/plain".toMediaTypeOrNull())
        val idRequestBody = id.toRequestBody("text/plain".toMediaTypeOrNull())
        return myFitnessService.updateExcercise(
            photoList,
            excName,
            excDiff,
            excEquip,
            excTutor,
            catIdRequestBody,
            idRequestBody
        )
    }

    suspend fun deleteExcercise(id: String, catId: String): ResponseMessage {
        return myFitnessService.deleteExcercise(id, catId)
    }

////////////////// nutrition

    suspend fun getAllMenu(): List<Menu> {
        return myFitnessService.fetchAllMenu()
    }

    suspend fun getNutritionCategory(): List<NutritionCategory> {
        return myFitnessService.fetchNutritionCategory()
    }

    suspend fun postNutritionCategory(name: String, photoPath: String): ResponseMessage {
        val uri = photoPath.toUri()
        val file = FileUtil.from(context, uri)
        val fileRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("photo", file.name, fileRequestBody)
        val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        return myFitnessService.postNutrionCategory(part, nameRequestBody)
    }

    suspend fun updateNutritionCategory(
        id: String,
        name: String,
        photoPath: String?
    ): ResponseMessage {
        val photoList = mutableListOf<MultipartBody.Part>()
        if (photoPath != null) {
            val uri = photoPath.toUri()
            val file = FileUtil.from(context, uri)
            val fileRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("photo", file.name, fileRequestBody)
            photoList.add(part)
        }
        val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val idRequestBody = id.toRequestBody("text/plain".toMediaTypeOrNull())
        return myFitnessService.updateNutrionCategory(photoList, nameRequestBody, idRequestBody)
    }

    suspend fun deleteNutritionCategory(id: String): ResponseMessage {
        return myFitnessService.deleteNutritionCategory(id)
    }

    suspend fun loadMenuList(nutriId: String): List<Menu> {
        return myFitnessService.fetchMenuList(nutriId)
    }

    suspend fun postMenu(
        nutriId: String,
        menu: Menu,
        photoPathList: List<String>
    ): ResponseMessage {
        val photoList = mutableListOf<MultipartBody.Part>()
        photoPathList.forEach {
            val uri = it.toUri()
            val file = FileUtil.from(context, uri)
            val fileRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("pics", file.name, fileRequestBody)
            photoList.add(part)
        }
        val name = menu.name.toRequestBody("text/plain".toMediaTypeOrNull())
        val breakfast = menu.breakfast.toRequestBody("text/plain".toMediaTypeOrNull())
        val lunch = menu.lunch.toRequestBody("text/plain".toMediaTypeOrNull())
        val dinner = menu.dinner.toRequestBody("text/plain".toMediaTypeOrNull())
        val snack = menu.snack.toRequestBody("text/plain".toMediaTypeOrNull())
        val other = menu.other.toRequestBody("text/plain".toMediaTypeOrNull())
        val nutriIdRequestBody = nutriId.toRequestBody("text/plain".toMediaTypeOrNull())
        return myFitnessService.postMenu(
            photoList,
            name,
            breakfast,
            lunch,
            dinner,
            snack,
            other,
            nutriIdRequestBody
        )
    }

    suspend fun updateMenu(
        id: String,
        nutriId: String,
        menu: Menu,
        photoPathList: List<String>?
    ): ResponseMessage {
        val photoList = mutableListOf<MultipartBody.Part>()
        photoPathList?.forEach {
            val uri = it.toUri()
            val file = FileUtil.from(context, uri)
            val fileRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("pics", file.name, fileRequestBody)
            photoList.add(part)
        }
        val name = menu.name.toRequestBody("text/plain".toMediaTypeOrNull())
        val breakfast = menu.breakfast.toRequestBody("text/plain".toMediaTypeOrNull())
        val lunch = menu.lunch.toRequestBody("text/plain".toMediaTypeOrNull())
        val dinner = menu.dinner.toRequestBody("text/plain".toMediaTypeOrNull())
        val snack = menu.snack.toRequestBody("text/plain".toMediaTypeOrNull())
        val other = menu.other.toRequestBody("text/plain".toMediaTypeOrNull())
        val nutriIdRequestBody = nutriId.toRequestBody("text/plain".toMediaTypeOrNull())
        val idRequestBody = id.toRequestBody("text/plain".toMediaTypeOrNull())
        return myFitnessService.updateMenu(
            photoList,
            name,
            breakfast,
            lunch,
            dinner,
            snack,
            other,
            nutriIdRequestBody,
            idRequestBody
        )
    }

    suspend fun deleteMenu(id: String, nutriId: String): ResponseMessage {
        return myFitnessService.deleteMenu(id, nutriId)
    }

}
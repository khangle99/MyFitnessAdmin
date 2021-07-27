package com.khangle.myfitnessadmin.data

import android.content.Context
import androidx.core.net.toUri
import com.khangle.myfitnessadmin.data.network.MyFitnessService
import com.khangle.myfitnessadmin.model.Excercise
import com.khangle.myfitnessadmin.model.ExcerciseCategory
import com.khangle.myfitnessadmin.model.ResponseMessage
import com.khangle.myfitnessadmin.util.FileUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


class MyFitnessRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val myFitnessService: MyFitnessService
) {
    suspend fun getExcerciseCategory(): List<ExcerciseCategory> {
        return myFitnessService.fetchExcerciseCategory()
    }

    suspend fun loadExcerciseList(catId: String): List<Excercise> {
        return myFitnessService.fetchExcerciseList(catId)
    }

    suspend fun postExcerciseCategory(name: String, photoPath: String): ResponseMessage {
        val uri = photoPath.toUri()
        val file = FileUtil.from(context, uri)
        val fileRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("photo", file.name, fileRequestBody)
        val catName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        return myFitnessService.postExcerciseCategory(part, catName)
    }

    suspend fun updateExcerciseCategory(id: String, name: String, photoPath: String): ResponseMessage {
        val uri = photoPath.toUri()
        val file = FileUtil.from(context, uri)
        val fileRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("photo", file.name, fileRequestBody)
        val catName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val catId = id.toRequestBody("text/plain".toMediaTypeOrNull())
        return myFitnessService.updateExcerciseCategory(part, catName, catId)
    }

    suspend fun deleteCategory(id: String): ResponseMessage {
        return myFitnessService.deleteCategory(id)
    }
    suspend fun postExcercise(catId: String,excercise: Excercise, photoPathList: List<String>): ResponseMessage {
        val photoList = mutableListOf<MultipartBody.Part>()
        photoPathList.forEach {
            val uri = it.toUri()
            val file = FileUtil.from(context, uri)
            val fileRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("picSteps", file.name, fileRequestBody)
            photoList.add(part)
        }
        val excName = excercise.name.toRequestBody("text/plain".toMediaTypeOrNull())
        val excDiff = excercise.difficulty.toRequestBody("text/plain".toMediaTypeOrNull())
        val excEquip = excercise.equipment.toRequestBody("text/plain".toMediaTypeOrNull())
        val excTutor = excercise.tutorial.toRequestBody("text/plain".toMediaTypeOrNull())
        val catIdRequestBody = catId.toRequestBody("text/plain".toMediaTypeOrNull())
        return myFitnessService.postExcercise(photoList,excName,excDiff,excEquip, excTutor, catIdRequestBody)
    }

    suspend fun updateExcercise(id: String, catId: String,excercise: Excercise, photoPathList: List<String>?): ResponseMessage {
        val photoList = mutableListOf<MultipartBody.Part>()
        photoPathList?.forEach {
            val uri = it.toUri()
            val file = FileUtil.from(context, uri)
            val fileRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("picSteps", file.name, fileRequestBody)
            photoList.add(part)
        }
        val excName = excercise.name.toRequestBody("text/plain".toMediaTypeOrNull())
        val excDiff = excercise.difficulty.toRequestBody("text/plain".toMediaTypeOrNull())
        val excEquip = excercise.equipment.toRequestBody("text/plain".toMediaTypeOrNull())
        val excTutor = excercise.tutorial.toRequestBody("text/plain".toMediaTypeOrNull())
        val catIdRequestBody = catId.toRequestBody("text/plain".toMediaTypeOrNull())
        val idRequestBody = id.toRequestBody("text/plain".toMediaTypeOrNull())
        return myFitnessService.updateExcercise(photoList,excName,excDiff,excEquip,excTutor,catIdRequestBody,idRequestBody)
    }

    suspend fun deleteExcercise(id: String,catId: String): ResponseMessage {
        return myFitnessService.deleteExcercise(id,catId)
    }




}
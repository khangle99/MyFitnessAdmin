package com.khangle.myfitnessadmin.data

import android.content.Context
import androidx.core.net.toUri
import com.khangle.myfitnessadmin.data.network.MyFitnessService
import com.khangle.myfitnessadmin.model.*
import com.khangle.myfitnessadmin.util.FileUtility
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


class MyFitnessRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val myFitnessService: MyFitnessService
) {
    ///////// excercise

    suspend fun getStatEnsureList(id: String, catId: String): Map<String,Any> {
        return myFitnessService.getStatEnsureList(id, catId)
    }

    suspend fun getAllExcercise(): List<Excercise> {
        return myFitnessService.fetchAllExcercise()
    }

    suspend fun getExcerciseCategory(): List<ExcerciseCategory> {
        return myFitnessService.fetchExcerciseCategory()
    }

    suspend fun postExcerciseCategory(name: String, photoPath: String): ResponseMessage {
        val uri = photoPath.toUri()

        val file = FileUtility.from(context, uri)
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
            val file = FileUtility.from(context, uri)
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
            val file = FileUtility.from(context, uri)
            val fileRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("picSteps", file.name, fileRequestBody)
            photoList.add(part)
        }
        val excName = excercise.name.toRequestBody("text/plain".toMediaTypeOrNull())
        val excDiff = excercise.difficulty.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val excEquip = excercise.equipment.toRequestBody("text/plain".toMediaTypeOrNull())
        val noTurn = excercise.noTurn.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val noSec = excercise.noSec.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val noGap = excercise.noGap.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val caloFactor = excercise.caloFactor.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val catIdRequestBody = catId.toRequestBody("text/plain".toMediaTypeOrNull())
        val achievement = excercise.achieveEnsure.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        // huong 2
        val list = mutableListOf<MultipartBody.Part>()
        excercise.tutorial.forEach {
            val part = MultipartBody.Part.createFormData("tutorial", it)
            list.add(part)
        }

        return myFitnessService.postExcercise(
            photoList,
            excName,
            excDiff,
            excEquip,
            noTurn,
            noSec,
            noGap,
            achievement,
            caloFactor,
            excercise.tutorial,
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
            val file = FileUtility.from(context, uri)
            val fileRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("picSteps", file.name, fileRequestBody)
            photoList.add(part)
        }
        val noTurn = excercise.noTurn.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val noSec = excercise.noSec.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val noGap = excercise.noGap.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val caloFactor = excercise.caloFactor.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val achievement = excercise.achieveEnsure.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val excName = excercise.name.toRequestBody("text/plain".toMediaTypeOrNull())
        val excDiff = excercise.difficulty.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val excEquip = excercise.equipment.toRequestBody("text/plain".toMediaTypeOrNull())
        val catIdRequestBody = catId.toRequestBody("text/plain".toMediaTypeOrNull())
        val idRequestBody = id.toRequestBody("text/plain".toMediaTypeOrNull())
        val list = mutableListOf<MultipartBody.Part>()

        excercise.tutorial.forEach {
            val part = MultipartBody.Part.createFormData("tutorial", it)
            list.add(part)
        }

        return myFitnessService.updateExcercise(
            photoList,
            excName,
            excDiff,
            excEquip,
            noTurn,
            noSec,
            noGap,
            achievement,
            excercise.tutorial,
            caloFactor,
            catIdRequestBody,
            idRequestBody
        )
    }

    suspend fun deleteExcercise(id: String, catId: String): ResponseMessage {
        return myFitnessService.deleteExcercise(id, catId)
    }

////////////////// body stat


    suspend fun getBodyStat(): List<BodyStat> {
        return myFitnessService.fetchBodyStat()
    }

    suspend fun postBodyStat(name: String, dataType: String): ResponseMessage {
        return myFitnessService.postBodyStat(name, dataType)
    }

    suspend fun updateBodyStat(id: String, name: String, dataType: String): ResponseMessage {
        return myFitnessService.updateBodyStat(id,name, dataType)
    }

    suspend fun deleteBodyStat(id: String): ResponseMessage {
        return myFitnessService.deleteBodyStat(id)
    }

}
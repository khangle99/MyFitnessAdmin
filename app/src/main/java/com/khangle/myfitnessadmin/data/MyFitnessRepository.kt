package com.khangle.myfitnessadmin.data

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import com.khangle.myfitnessadmin.data.network.MyFitnessService
import com.khangle.myfitnessadmin.model.ExcerciseCategory
import com.khangle.myfitnessadmin.model.ResponseMessage
import com.khangle.myfitnessadmin.util.FileUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


class MyFitnessRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val myFitnessService: MyFitnessService
) {
    suspend fun getExcerciseCategory(): List<ExcerciseCategory> {
        return myFitnessService.fetchExcerciseCategory()
    }

    suspend fun postExcerciseCategory(name: String, photoPath: String): ResponseMessage {
        val uri = photoPath.toUri()

        val file = FileUtil.from(context, uri)
        val b=  "File...:::: uti - "+file .getPath()+" file -" + file + " : " + file .exists()
        val fileRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("photo", file.name, fileRequestBody)
        val catName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        return myFitnessService.postExcerciseCategory(part, catName)
    }


}
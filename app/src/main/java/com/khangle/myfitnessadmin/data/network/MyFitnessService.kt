package com.khangle.myfitnessadmin.data.network


import com.khangle.myfitnessadmin.model.ExcerciseCategory
import com.khangle.myfitnessadmin.model.ResponseMessage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MyFitnessService {

    //////// excercise
    @GET("excercisesCatalog")
    suspend fun fetchExcerciseCategory(): List<ExcerciseCategory>

    @Multipart
    @POST("newCatalog")
    suspend fun postExcerciseCategory(@Part photo: MultipartBody.Part, @Part("name") name: RequestBody): ResponseMessage

/////////// nutrition


}
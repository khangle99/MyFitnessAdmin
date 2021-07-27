package com.khangle.myfitnessadmin.data.network


import com.khangle.myfitnessadmin.model.Excercise
import com.khangle.myfitnessadmin.model.ExcerciseCategory
import com.khangle.myfitnessadmin.model.ResponseMessage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MyFitnessService {

    //////// excercise
    @GET("excercisesCategory")
    suspend fun fetchExcerciseCategory(): List<ExcerciseCategory>

    @GET("excercises")
    suspend fun fetchExcerciseList(@Query("categoryId") catId: String): List<Excercise>

    @Multipart
    @POST("newCategory")
    suspend fun postExcerciseCategory(
        @Part photo: MultipartBody.Part,
        @Part("name") name: RequestBody
    ): ResponseMessage

    @Multipart
    @PUT("updateCategory")
    suspend fun updateExcerciseCategory(
        @Part photo: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("id") cateId: RequestBody
    ): ResponseMessage

    @DELETE("deleteCategory")
    suspend fun deleteCategory(@Query("id") id: String): ResponseMessage

    @Multipart
    @POST("newExcercise")
    suspend fun postExcercise(
        @Part photos: List<MultipartBody.Part>,
        @Part("name") name: RequestBody,
        @Part("difficulty") diff: RequestBody,
        @Part("equipment") equip: RequestBody,
        @Part("tutorial") tutor: RequestBody,
        @Part("catId") catId: RequestBody
    ): ResponseMessage

    @Multipart
    @PUT("updateExcercise")
    suspend fun updateExcercise(
        @Part photos: List<MultipartBody.Part>,
        @Part("name") name: RequestBody,
        @Part("difficulty") diff: RequestBody,
        @Part("equipment") equip: RequestBody,
        @Part("tutorial") tutor: RequestBody,
        @Part("categoryId") catId: RequestBody,
        @Part("id") id: RequestBody
    ): ResponseMessage

    @DELETE("deleteExcercise")
    suspend fun deleteExcercise(@Query("id") id: String, @Query("categoryId") catId: String): ResponseMessage

/////////// nutrition


}
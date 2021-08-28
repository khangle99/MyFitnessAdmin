package com.khangle.myfitnessadmin.data.network


import com.khangle.myfitnessadmin.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MyFitnessService {

    //////// excercise

    @GET("allexcercises")
    suspend fun fetchAllExcercise(): List<Excercise>

    @GET("excercisesCategory")
    suspend fun fetchExcerciseCategory(): List<ExcerciseCategory>

    @GET("excercises")
    suspend fun fetchExcerciseList(@Query("categoryId") catId: String): List<Excercise>

    @Multipart
    @POST("newExcerciseCategory")
    suspend fun postExcerciseCategory(
        @Part photo: MultipartBody.Part,
        @Part("name") name: RequestBody
    ): ResponseMessage

    @Multipart
    @PUT("updateExcerciseCategory")
    suspend fun updateExcerciseCategory(
        @Part photo: List<MultipartBody.Part>,
        @Part("name") name: RequestBody,
        @Part("id") cateId: RequestBody
    ): ResponseMessage

    @DELETE("deleteExcerciseCategory")
    suspend fun deleteExcerciseCategory(@Query("id") id: String): ResponseMessage

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
    suspend fun deleteExcercise(
        @Query("id") id: String,
        @Query("categoryId") catId: String
    ): ResponseMessage

/////////// nutrition

    @GET("allmenu")
    suspend fun fetchAllMenu(): List<Menu>

    @GET("nutritionCategory")
    suspend fun fetchNutritionCategory(): List<NutritionCategory>

    @GET("menuList")
    suspend fun fetchMenuList(@Query("nutriId") nutriId: String): List<Menu>

    @Multipart
    @POST("newNutritionCategory")
    suspend fun postNutrionCategory(
        @Part photo: MultipartBody.Part,
        @Part("name") name: RequestBody
    ): ResponseMessage

    @Multipart
    @PUT("updateNutritionCategory")
    suspend fun updateNutrionCategory(
        @Part photo: List<MultipartBody.Part>,
        @Part("name") name: RequestBody,
        @Part("id") nutriId: RequestBody
    ): ResponseMessage

    @DELETE("deleteNutritionCategory")
    suspend fun deleteNutritionCategory(@Query("id") id: String): ResponseMessage

    @Multipart
    @POST("newMenu")
    suspend fun postMenu(
        @Part photos: List<MultipartBody.Part>,
        @Part("name") name: RequestBody,
        @Part("breakfast") breakfast: RequestBody,
        @Part("lunch") lunch: RequestBody,
        @Part("dinner") dinner: RequestBody,
        @Part("snack") snack: RequestBody,
        @Part("other") other: RequestBody,
        @Part("nutriId") nutriId: RequestBody
    ): ResponseMessage

    @Multipart
    @PUT("updateMenu")
    suspend fun updateMenu(
        @Part photos: List<MultipartBody.Part>,
        @Part("name") name: RequestBody,
        @Part("breakfast") breakfast: RequestBody,
        @Part("lunch") lunch: RequestBody,
        @Part("dinner") dinner: RequestBody,
        @Part("snack") snack: RequestBody,
        @Part("other") other: RequestBody,
        @Part("nutriId") nutriId: RequestBody,
        @Part("id") id: RequestBody
    ): ResponseMessage

    @DELETE("deleteMenu")
    suspend fun deleteMenu(
        @Query("id") id: String,
        @Query("nutriId") nutriId: String
    ): ResponseMessage

}
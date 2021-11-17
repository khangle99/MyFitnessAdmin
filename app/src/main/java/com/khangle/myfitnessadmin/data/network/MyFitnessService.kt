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
        @Part("noTurn") noTurn: RequestBody,
        @Part("noSec") noSec: RequestBody,
        @Part("noGap") noGap: RequestBody,
        @Part("achievementMap") achievementString: RequestBody,
        @Part("caloFactor") caloFactor: RequestBody,
        @Part("tutorial[]") tutorial: List<String>,
        @Part("catId") catId: RequestBody
    ): ResponseMessage

    @Multipart
    @PUT("updateExcercise")
    suspend fun updateExcercise(
        @Part photos: List<MultipartBody.Part>,
        @Part("name") name: RequestBody,
        @Part("difficulty") diff: RequestBody,
        @Part("equipment") equip: RequestBody,
        @Part("noTurn") noTurn: RequestBody,
        @Part("noSec") noSec: RequestBody,
        @Part("noGap") noGap: RequestBody,
        @Part("achievementMap") achievementString: RequestBody,
        @Part("tutorial[]") tutorial: List<String>,
        @Part("caloFactor") caloFactor: RequestBody,
        @Part("categoryId") catId: RequestBody,
        @Part("id") id: RequestBody
    ): ResponseMessage

    @DELETE("deleteExcercise")
    suspend fun deleteExcercise(
        @Query("id") id: String,
        @Query("categoryId") catId: String
    ): ResponseMessage

    @GET("statEnsure")
    suspend fun getStatEnsureList(
        @Query("excId") id: String,
        @Query("catId") catId: String
    ): Map<String, Any> // moi string la json map chua cac statName va value

/////////// body stat

    @GET("bodyStats")
    suspend fun fetchBodyStat(): List<BodyStat>

    @FormUrlEncoded
    @POST("newBodyStat")
    suspend fun postBodyStat(
        @Field("name") name: String,
        @Field("dataType") dataType: String
    ): ResponseMessage

    @FormUrlEncoded
    @PUT("updateBodyStat")
    suspend fun updateBodyStat(
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("dataType") dataType: String
    ): ResponseMessage

    @DELETE("deleteBodyStat")
    suspend fun deleteBodyStat(@Query("id") id: String): ResponseMessage

}
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

    @GET("excercisesDetail")
    suspend fun fetchExcercise(@Query("categoryId") catId: String, @Query("id") id: String): Excercise

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
        @Field("dataType") dataType: String,
        @Field("unit") unit: String
    ): ResponseMessage

    @FormUrlEncoded
    @PUT("updateBodyStat")
    suspend fun updateBodyStat(
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("dataType") dataType: String,
        @Field("unit") unit: String
    ): ResponseMessage

    @DELETE("deleteBodyStat")
    suspend fun deleteBodyStat(@Query("id") id: String): ResponseMessage


    /////////// suggest plan
    @GET("allSuggestPlan")
    suspend fun fetchSuggestPlans(): List<Plan>

    @FormUrlEncoded
    @POST("newSuggestPlan")
    suspend fun postPlan(
        @Field("description") name: String
    ): ResponseMessage

    @FormUrlEncoded
    @PUT("updateSuggestPlan")
    suspend fun updatePlan(
        @Field("id") id: String,
        @Field("description") name: String
    ): ResponseMessage

    @DELETE("deleteSuggestPlan")
    suspend fun deletePlan(@Query("id") id: String): ResponseMessage

    /////////// suggest plan detail (PlanDay)
    @GET("suggestPlanDetail")
    suspend fun fetchDayList(@Query("sugId") sugId: String): List<PlanDay>

    @FormUrlEncoded
    @PUT("updateSuggestPlanDetail")
    suspend fun updatePlanDay(
        @Field("sugId") id: String,
        @Field("categoryId") categoryId: String,
        @Field("excId") excId: String,
        @Field("day") day: String,
        @Field("oldDay") oldDay: String
    ): ResponseMessage

    @DELETE("deleteSuggestPlanDetail")
    suspend fun deletePlanDay(@Query("sugId") id: String,
                              @Query("day") day: String
    ): ResponseMessage


}
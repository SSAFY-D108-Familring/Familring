package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.calendar.DailyLife
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface DailyApi {
    @GET("calendars/dailies/date")
    suspend fun getDayDailies(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int,
    ): BaseResponse<List<DailyLife>>

    @Multipart
    @POST("calendars/dailies")
    suspend fun createDaily(
        @Part("content") content: RequestBody,
        @Part image: MultipartBody.Part?,
    ): BaseResponse<Unit>

    @Multipart
    @PATCH("calendars/dailies/{daily_id}")
    suspend fun modifyDaily(
        @Path("daily_id") dailyId: Long,
        @Part("content") content: RequestBody,
        @Part image: MultipartBody.Part?,
    ): BaseResponse<Unit>

    @DELETE("calendars/dailies/{daily_id}")
    suspend fun deleteDaily(
        @Path("daily_id") dailyId: Long,
    ): BaseResponse<Unit>
}

package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.MonthSchedulesDailies
import com.familring.domain.model.Schedule
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CalendarApi {
    @GET("calendars")
    suspend fun getMonthData(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): BaseResponse<MonthSchedulesDailies>

    @GET("calendars/schedules")
    suspend fun getDaySchedules(
        @Query("schedule_id") scheduleIds: List<Long>,
    ): BaseResponse<List<Schedule>>

    @POST("calendars/schedules")
    suspend fun createSchedule(
        @Body schedule: Schedule,
    ): BaseResponse<Unit>

    @POST("calendars/schedules/{schedule_id}")
    suspend fun updateSchedule(
        @Path("schedule_id") id: Long,
        @Body schedule: Schedule,
    ): BaseResponse<Unit>

    @DELETE("calendars/schedules/{schedule_id}")
    suspend fun deleteSchedule(
        @Path("schedule_id") id: Long,
    ): BaseResponse<Unit>
}

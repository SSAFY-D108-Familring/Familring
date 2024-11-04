package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.FamilyMake
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FamilyApi {
//    @GET("family/member")
//    suspend fun getFamily(): BaseResponse<Family>

    @GET("family/code")
    suspend fun getFamilyCode(): BaseResponse<String>

    @POST("family")
    suspend fun makeFamily(): BaseResponse<FamilyMake>

    @POST("family/join")
    suspend fun joinFamily(
        @Body familyCode: String,
    ): BaseResponse<String>
}

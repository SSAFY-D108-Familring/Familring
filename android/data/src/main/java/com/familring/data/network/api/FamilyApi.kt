package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.FamilyInfo
import com.familring.domain.model.FamilyMake
import com.familring.domain.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FamilyApi {
    @GET("family/code")
    suspend fun getFamilyCode(): BaseResponse<String>

    @POST("family")
    suspend fun makeFamily(): BaseResponse<FamilyMake>

    @POST("family/join")
    suspend fun joinFamily(
        @Body familyCode: String,
    ): BaseResponse<String>

    @GET("family/member")
    suspend fun getFamilyMembers(): BaseResponse<List<User>>

    @GET("family")
    suspend fun getFamilyInfo(): BaseResponse<FamilyInfo>
}

package com.familring.data.network.api

import com.familring.data.network.response.BaseResponse
import com.familring.domain.model.FamilyInfo
import com.familring.domain.model.User
import retrofit2.http.GET

interface FamilyApi {
    @GET("family/member")
    suspend fun getFamilyMembers(): BaseResponse<List<User>>

    @GET("family")
    suspend fun getFamilyInfo(): BaseResponse<FamilyInfo>
}
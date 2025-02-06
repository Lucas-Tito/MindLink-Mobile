package com.titolucas.mindlink.network

import com.titolucas.mindlink.generalData.Appointment
import com.titolucas.mindlink.generalData.UserResponse
import com.titolucas.mindlink.profile.data.UserRequest
import com.titolucas.mindlink.service_hours.data.AvailabilityRequest
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Body

interface ApiService {

    @GET("users")
    suspend fun getAllUsers(): List<UserResponse>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: String): UserResponse

    @GET("appointments/currentmonth/{id}")
    suspend fun getAppointmentsByProfessionalIdInCurrentMonth(@Path("id") professionalId: String): List<Appointment>

    @GET("users/checkIfIsProfessional/{id}")
    suspend fun checkIfUserIsProfessional(@Path("id") userId: String): Boolean

    @GET("professionalUsers")
    suspend fun getProfessionalUsers(): List<UserResponse>

    @POST("users")
    suspend fun createUser(@Body userRequest: UserRequest): Map<String, String>

    @GET("searchPsyco")
    suspend fun searchPsychologists(@Query("q") query: String): List<UserResponse>

    @POST("availability")
    fun postAvailability(@Body body: AvailabilityRequest): Call<Void>

}

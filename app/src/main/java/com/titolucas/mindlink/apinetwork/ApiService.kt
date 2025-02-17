package com.titolucas.mindlink.network

import com.titolucas.mindlink.generalData.Appointment
import com.titolucas.mindlink.generalData.AppointmentRequest
import com.titolucas.mindlink.generalData.UpdateUserResponse
import com.titolucas.mindlink.generalData.AvailabilityResponse
import com.titolucas.mindlink.generalData.UserResponse
import com.titolucas.mindlink.messages.data.ConversationsRequest
import com.titolucas.mindlink.messages.data.LastMessageRequest
import com.titolucas.mindlink.messages.data.MessageRequest
import com.titolucas.mindlink.profile.data.UserRequest
import com.titolucas.mindlink.service_hours.data.AvailabilityRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Body

interface ApiService {

    //Users
    @GET("users")
    suspend fun getAllUsers(): List<UserResponse>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: String): UserResponse

    @GET("users/checkIfIsProfessional/{id}")
    suspend fun checkIfUserIsProfessional(@Path("id") userId: String): Boolean

    @GET("professionalUsers")
    suspend fun getProfessionalUsers(): List<UserResponse>

    @POST("users")
    suspend fun createUser(@Body userRequest: UserRequest): Map<String, String>

    @GET("searchPsyco")
    suspend fun searchPsychologists(@Query("q") query: String): List<UserResponse>

    //appointments

    @GET("appointments/currentmonthprofessional/{id}")
    suspend fun getAppointmentsByProfessionalIdInCurrentMonth(@Path("id") professionalId: String): List<Appointment>

    @GET("appointments/currentmonthpatient/{id}")
    suspend fun getAppointmentsByPatientIdInCurrentMonth(@Path("id") patientId: String): List<Appointment>

    @POST("appointment")
    suspend fun createAppointment(@Body appointmentRequest: AppointmentRequest): List<AvailabilityResponse>

    @GET("appointments/getByIdAndMonth/{id}")
    suspend fun getAppointmentsByUserIdInMonth(
        @Path("id") userId: String,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): List<Appointment>


    //availability
    @POST("availability")
    fun postAvailability(@Body body: AvailabilityRequest): Call<Void>

    @GET("availability/{userId}")
    suspend fun getAvailability(@Path("userId") userId: String): List<AvailabilityResponse>

    @GET("availability/{id}")
    suspend fun getAvailabilityByUserId(@Path("id") userId: String): List<AvailabilityRequest>


    //messages
    @GET("messages/conversations/{userId}/{contactId}")
    suspend fun getConversationsByUserId(@Path("userId") userId: String, @Path("contactId") contactId: String): List<ConversationsRequest>

    @GET("messages/lastMessage/{id}")
    suspend fun getLastMessagesByUserId(@Path("id") userId: String): List<LastMessageRequest>

    @POST("messages")
    suspend fun createMessage(@Body messageRequest: MessageRequest): Response<Void>

    @PATCH("users/{id}")
    suspend fun updateUser(
        @Path("id") userId: String,
        @Body updates: @JvmSuppressWildcards Map<String, Any>
    ): UpdateUserResponse
}

package com.titolucas.mindlink.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.titolucas.mindlink.generalData.Appointment
import com.titolucas.mindlink.generalData.UserResponse
import com.titolucas.mindlink.home.repository.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<List<UserResponse>>()
    val searchResults: LiveData<List<UserResponse>> get() = _searchResults

    private val _professionalUsers = MutableLiveData<List<UserResponse>>()
    val professionalUsers: LiveData<List<UserResponse>> get() = _professionalUsers

    private val _appointmentCurrentMonth = MutableLiveData<List<Appointment>>()
    val appointmentCurrentMonth: LiveData<List<Appointment>> get() = _appointmentCurrentMonth

    private val _updateStatusResult = MutableLiveData<Boolean>()
    val updateStatusResult: LiveData<Boolean> get() = _updateStatusResult

    fun loadProfessionalUsers() {
        viewModelScope.launch {
            val results = repository.getProfessionalUsers()
            _professionalUsers.postValue(results)
        }
    }

    fun searchPsychologists(query: String) {
        viewModelScope.launch {
            val results = repository.searchPsychologists(query)
            _searchResults.postValue(results)
        }
    }
    fun getAppointmentsByProfessionalIdInCurrentMonth(professionalId: String) {
        viewModelScope.launch {
            try {
                val appointments = repository.getAppointmentsByProfessionalIdInCurrentMonth(professionalId)
                println("appointments viewmodel: "+appointments)
                val updatedAppointments = appointments.map {
                    it.copy(appointmentId = it.appointmentId)
                }

                println("🔵 Atualizando LiveData com: $updatedAppointments")
                _appointmentCurrentMonth.postValue(updatedAppointments)
                println("appnt current viewmodel"+appointmentCurrentMonth.value)
                _appointmentCurrentMonth.value?.forEach { println("📌 $it") }// Atualiza a UI com os resultados
            } catch (e: retrofit2.HttpException) {
                if (e.code() == 404) {
                    println("Nenhum compromisso encontrado (Erro 404)")
                    _appointmentCurrentMonth.postValue(emptyList()) // Mostra o calendário sem eventos
                } else {
                    println("Erro na requisição: ${e.message()}")
                }
            } catch (e: Exception) {
                println("Erro inesperado: ${e.message}")
                _appointmentCurrentMonth.postValue(emptyList()) // Garante que o calendário não fique sem atualização
            }


        }
    }
    fun getAppointmentsByPatientIdInCurrentMonth(patientId: String) {
        viewModelScope.launch {
            try {
                val appointments = repository.getAppointmentsByPatientIdInCurrentMonth(patientId)
                _appointmentCurrentMonth.postValue(appointments) // Atualiza a UI com os resultados
            } catch (e: retrofit2.HttpException) {
                if (e.code() == 404) {
                    println("Nenhum compromisso encontrado (Erro 404)")
                    _appointmentCurrentMonth.postValue(emptyList()) // Mostra o calendário sem eventos
                } else {
                    println("Erro na requisição: ${e.message()}")
                }
            } catch (e: Exception) {
                println("Erro inesperado: ${e.message}")
                _appointmentCurrentMonth.postValue(emptyList()) // Garante que o calendário não fique sem atualização
            }
        }
    }

    fun getAppointmentsByUserIdInMonth(userId: String,selectedMonth: Int , selectedYear: Int) {
        viewModelScope.launch {
            try {
                val appointments = repository.getAppointmentsByUserIdInMonth(userId,selectedMonth ,selectedYear)
                _appointmentCurrentMonth.postValue(appointments) // Atualiza a UI com os resultados
            } catch (e: retrofit2.HttpException) {
                if (e.code() == 404) {
                    println("Nenhum compromisso encontrado (Erro 404)")
                    _appointmentCurrentMonth.postValue(emptyList()) // Mostra o calendário sem eventos
                } else {
                    println("Erro na requisição: ${e.message()}")
                }
            } catch (e: Exception) {
                println("Erro inesperado: ${e.message}")
                _appointmentCurrentMonth.postValue(emptyList()) // Garante que o calendário não fique sem atualização
            }
        }
    }

    fun updateAppointmentStatus(appointmentId: String, newStatus: String) {
        viewModelScope.launch {
            try {
                repository.updateAppointmentStatus(appointmentId,  mapOf("status" to newStatus))
                _updateStatusResult.postValue(true)

            } catch (e: Exception) {
                _updateStatusResult.postValue(false)
            }
        }
    }

}

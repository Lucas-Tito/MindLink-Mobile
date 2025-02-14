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

}

package com.titolucas.mindlink.profile.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.R
import com.titolucas.mindlink.generalData.AppointmentDateTime
import com.titolucas.mindlink.generalData.AppointmentRequest
import com.titolucas.mindlink.generalData.AvailabilityResponse
import com.titolucas.mindlink.generalData.EndTime
import com.titolucas.mindlink.generalData.StartTime
import com.titolucas.mindlink.generalData.UserResponse
import com.titolucas.mindlink.network.RetrofitInstance
import com.titolucas.mindlink.profile.repository.ProfileRepository
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModel
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModelFactory
import com.titolucas.mindlink.service_hours.data.AvailabilityRequest
import kotlinx.coroutines.launch
import java.util.Calendar
import com.applandeo.materialcalendarview.CalendarView as MaterialCalendarView

class PysichologistSchedulingActivity : AppCompatActivity() {
    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(ProfileRepository())
    }

    private lateinit var calendarView: MaterialCalendarView
    private val availableTimes: MutableMap<String, List<String>> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        println("onCreate Scheduling")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_psychologist_scheduling)

        val psychoId = intent.getStringExtra("USER_ID") ?: ""
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            finish()
            return
        }

        viewModel.fetchUserById(psychoId)
        viewModel.userDetails.observe(this) { user ->
            initializeViews(user)
            fetchAvailability(psychoId)
        }
    }

    private fun initializeViews(user: UserResponse) {
        val profileImage = findViewById<ShapeableImageView>(R.id.foto_perfil_psicologo)
        val profileName = findViewById<TextView>(R.id.nome_perfil_psicologo)
        val profileTitle = findViewById<TextView>(R.id.titulo_perfil_psicologo)
        val bioText = findViewById<TextView>(R.id.descricao_perfil)

        profileName.text = user.name
        profileTitle.text = "Psicólogo"
        bioText.text = user.bio ?: "Bio não disponível"

        Glide.with(this)
            .load(user.photoURL)
            .placeholder(R.drawable.ic_user_placeholder)
            .into(profileImage)

        findViewById<View>(R.id.seta_voltar).setOnClickListener { finish() }
    }

    private fun fetchAvailability(professionalId: String) {
        lifecycleScope.launch {
            try {
                val response: List<AvailabilityResponse> = RetrofitInstance.apiService.getAvailability(professionalId)
                println("availability response: "+ response)
                // Convertendo manualmente o JSON para AvailabilityResponse
                val availabilityList = response.mapNotNull { response ->
                    try {
                        AvailabilityResponse(
                            professionalId = response.professionalId.toString(),
                            dayOfWeek = response.dayOfWeek,
                            startTime = StartTime(
                                startHour = response.startTime.startHour  as? String ?: "00",
                                startMinute = response.startTime.startMinute as? String ?: "00"
                            ),
                            endTime = EndTime(
                                endHour = response.endTime.endHour  as? String ?: "00",
                                endMinute = response.endTime.endMinute as? String ?: "00"
                            ),
                            availabilityId = response.availabilityId as? String ?: ""
                        )
                    } catch (e: Exception) {
                        null // Se falhar, ignora essa entrada
                    }
                }

                updateAvailableTimes(availabilityList)
                setupCalendar()
            } catch (e: Exception) {
                Toast.makeText(this@PysichologistSchedulingActivity, "Erro ao buscar disponibilidade", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAvailableTimes(availabilities: List<AvailabilityResponse>) {
        availableTimes.clear()
        for (availability in availabilities) {
            val formattedDate = formatDayOfWeek(availability.dayOfWeek)
            val timeRange = "${availability.startTime.startHour}:${availability.startTime.startMinute}"

            availableTimes[formattedDate] = availableTimes.getOrDefault(formattedDate, listOf()) + timeRange
        }

        setupCalendar()
    }

    @SuppressLint("ResourceAsColor")
    private fun setupCalendar() {
        calendarView = findViewById(R.id.calendar_psychologist_custom)

        // Criar eventos visuais para os dias disponíveis
        val availableDates = availableTimes.keys.map { dateString ->
            val (day, month, year) = dateString.split("-").map { it.toInt() }
            Calendar.getInstance().apply { set(year, month - 1, day) }
        }

        val eventDays = availableDates.map { date ->
            CalendarDay(date).apply {
                labelColor = R.color.dia_disponivel
                imageResource = R.drawable.ic_calendar_event // Ícone para indicar disponibilidade
            }
        }

        calendarView.setCalendarDays(eventDays)

        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                handleDayClick(calendarDay)
            }
        })
    }

    private fun handleDayClick(calendarDay: CalendarDay) {
        val selectedDate = formatCalendarDate(calendarDay.calendar)
        val horarios = availableTimes[selectedDate]

        if (horarios != null) {
            showHorarios(horarios, selectedDate)
        } else {
            findViewById<LinearLayout>(R.id.horarios_container).removeAllViews()
            Toast.makeText(this, "Nenhum horário disponível para esse dia", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showHorarios(horarios: List<String>, selectedDate: String) {
        val horariosContainer = findViewById<LinearLayout>(R.id.horarios_container)
        horariosContainer.removeAllViews()

        horarios.forEach { horario ->
            val horarioButton = Button(this).apply {
                text = horario
                setTextColor(ContextCompat.getColor(context, android.R.color.white))
                textSize = 19f
                setBackgroundColor(ContextCompat.getColor(context, R.color.horarios_dia))
                setOnClickListener { sendAppointmentRequest(selectedDate, horario) }
            }
            horariosContainer.addView(horarioButton)
        }
    }

    private fun sendAppointmentRequest(selectedDate: String, selectedTime: String) {
        try {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val psychoId = intent.getStringExtra("USER_ID")

            if (userId.isNullOrEmpty() || psychoId.isNullOrEmpty()) {
                Toast.makeText(this, "Erro ao recuperar informações do usuário.", Toast.LENGTH_LONG).show()
                return
            }

            // Obtém os nomes reais do paciente e psicólogo
            val patientName = viewModel.userDetails.value?.name ?: "Paciente"
            val professionalName = viewModel.userDetails.value?.name ?: "Psicólogo"

            // Formatar a data corretamente
            val dateParts = selectedDate.split("-") // Formato esperado: dd-MM-yyyy
            val timeParts = selectedTime.split(":") // Formato esperado: HH:mm

            if (dateParts.size != 3 || timeParts.size != 2) {
                Toast.makeText(this, "Erro no formato da data ou horário", Toast.LENGTH_LONG).show()
                return
            }

            val day = dateParts[0].toInt()
            val month = dateParts[1].toInt()  // Sem ajuste, pois a API espera 1 a 12
            val year = dateParts[2].toInt()
            val hour = timeParts[0].toInt()
            val minutes = timeParts[1].toInt()

            // Criar o objeto correto para enviar ao Retrofit
            val appointmentRequest = AppointmentRequest(
                patientId = userId,
                patientName = patientName,
                professionalId = psychoId,
                professionalName = professionalName,
                appointmentDate = AppointmentDateTime(
                    year = year,
                    month = month,
                    day = day,
                    hour = hour,
                    minutes = minutes
                )
            )

            // Enviar a requisição para API
            lifecycleScope.launch {
                try {
                    val response = RetrofitInstance.apiService.createAppointment(appointmentRequest)

                    if (!response.isNullOrEmpty()) {
                        Toast.makeText(this@PysichologistSchedulingActivity, "Consulta Agendada!", Toast.LENGTH_LONG).show()
                        finish()
                    }
                } catch (e: Exception) {
                    println("Erro: ${e.message}, Caminho: ${e.stackTrace}")
                    Toast.makeText(this@PysichologistSchedulingActivity, "Erro ao se conectar com a API.", Toast.LENGTH_LONG).show()
                }
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao processar dados: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }



    private fun formatCalendarDate(calendar: Calendar): String {
        return String.format("%02d-%02d-%04d",
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.YEAR)
        )
    }

    private fun formatDayOfWeek(dayOfWeek: String): String {
        return when (dayOfWeek) {
            "Sunday" -> "25-02-2025"
            "Monday" -> "26-02-2025"
            "Tuesday" -> "27-02-2025"
            else -> ""
        }
    }


}

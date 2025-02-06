package com.titolucas.mindlink.profile.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.R
import com.titolucas.mindlink.generalData.UserResponse
import com.titolucas.mindlink.profile.repository.ProfileRepository
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModel
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModelFactory
import java.util.Calendar
import com.applandeo.materialcalendarview.CalendarView as MaterialCalendarView


class PysichologistSchedulingActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(ProfileRepository())
    }
    private lateinit var calendarView: MaterialCalendarView
    private val events: MutableMap<String, String> = mutableMapOf()

    // dados de horários para cada dia
    val horariosPorDia: MutableMap<String, List<String>> = mutableMapOf(
        "25-02-2025" to listOf("10:00", "14:00", "16:00", "10:00", "14:00", "16:00"),
        "20-02-2025" to listOf("09:00", "12:00", "15:00")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verifica autenticação
        val psychoId = intent.getStringExtra("USER_ID") ?: ""
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            finish()
            return
        } else {
            viewModel.fetchUserById(psychoId)
            println("Psyco ID: "+ psychoId)
        }

        // Observa os detalhes do usuário
        viewModel.userDetails.observe(this) { user ->
            setContentView(R.layout.activity_psychologist_scheduling)
            initializeViews(user)
            setupCalendar()


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

        // Adiciona o OnClickListener ao botão "seta_voltar"
        val setaVoltar = findViewById<View>(R.id.seta_voltar)
        setaVoltar.setOnClickListener {
            finish() // Fecha a Activity atual e volta para a anterior
        }
    }


    private fun setupCalendar() {
        calendarView = findViewById(R.id.calendar_psychologist_custom)

        // Configura eventos no calendário
        val calendars = createEventDays()
        calendarView.setCalendarDays(calendars)

        // Adiciona listener para cliques nos dias
        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                handleDayClick(calendarDay)
            }
        })
    }

    data class Event(
        val date: String, // Formato: "dd-MM-yyyy"
        val labelColor: Int,
        val imageResource: Int,
        val description: String
    )

    private fun createEventDays(): List<CalendarDay> {
        // Lista de eventos mockados, mas as imagens não estáo indicando, então coloquei verde por enquanto
        val mockEvents = listOf(
            Event(
                date = "25-02-2025",
                labelColor = R.color.horários_disponives,
                imageResource = R.drawable.favorite_star,
                description = "Consulta disponível"
            ),
            Event(
                date = "20-02-2025",
                labelColor = R.color.horários_disponives,
                imageResource = R.drawable.selected_day_background,
                description = "Consulta disponível"
            )
        )

        val calendars = mutableListOf<CalendarDay>()

        for (event in mockEvents) {
            val (day, month, year) = event.date.split("-").map { it.toInt() }

            val calendar = Calendar.getInstance().apply {
                set(year, month - 1, day) // Mês é zero-based
            }

            val calendarDay = CalendarDay(calendar).apply {
                labelColor = event.labelColor
                imageResource = event.imageResource
            }

            calendars.add(calendarDay)
            events[event.date] = event.description // Atualiza o mapa com o evento e descrição
        }

        return calendars
    }

    private fun handleDayClick(calendarDay: CalendarDay) {
        val day = String.format("%02d", calendarDay.calendar.get(Calendar.DAY_OF_MONTH))
        val month = String.format("%02d", calendarDay.calendar.get(Calendar.MONTH) + 1)
        val year = calendarDay.calendar.get(Calendar.YEAR)
        val dateKey = "$day-$month-$year"

        val horarios = horariosPorDia[dateKey]

        if (horarios != null && horarios.isNotEmpty()) {
            // Exibe os horários do dia
            showHorarios(horarios)
        } else {
            val horariosContainer = findViewById<LinearLayout>(R.id.horarios_container)
            horariosContainer.removeAllViews()
            Toast.makeText(this, "Nenhum horário disponível para esse dia", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showHorarios(horarios: List<String>) {
        val horariosContainer = findViewById<LinearLayout>(R.id.horarios_container)

        // Limpa os botões anteriores
        horariosContainer.removeAllViews()

        // Cria um botão para cada horário
        horarios.forEach { horario ->
            val horarioButton = Button(this).apply {
                text = horario
                setTextColor(ContextCompat.getColor(context, android.R.color.white)) // Texto branco
                textSize = 19f // Tamanho do texto
                setPadding(24, 12, 24, 12) // Padding interno do botão
                setBackgroundColor(ContextCompat.getColor(context, R.color.horarios_dia))
                setTextColor(ContextCompat.getColor(context, R.color.status_solicitado))

                // Ação ao clicar no horário
                setOnClickListener {
                    Toast.makeText(
                        this@PysichologistSchedulingActivity,
                        "Horário selecionado: $horario",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Define o layout params com margem
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 8, 16, 8) // Margem entre os botões
                }
                layoutParams = params
            }
            horariosContainer.addView(horarioButton)
        }
    }
}




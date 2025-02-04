package com.titolucas.mindlink.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.titolucas.mindlink.R
import com.titolucas.mindlink.home.repository.HomeRepository
import com.titolucas.mindlink.home.viewmodel.HomeViewModel
import com.titolucas.mindlink.home.viewmodel.HomeViewModelFactory
import androidx.fragment.app.viewModels
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.generalData.Appointment
import java.util.Calendar

class PsychoHomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(HomeRepository())
    }

    private lateinit var calendarView: CalendarView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home_psycho, container, false)

        // Referência para o CalendarView
        calendarView = view.findViewById(R.id.calendar_psychologist_custom)

        // Observa os dados e atualiza o calendário
        viewModel.appointmentCurrentMonth.observe(viewLifecycleOwner) { appointments ->
            markAppointmentsOnCalendar(appointments)
        }

        // Busca os compromissos do mês
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        viewModel.getAppointmentsByProfessionalIdInCurrentMonth(userId.toString())

        return view
    }

    private fun markAppointmentsOnCalendar(appointments: List<Appointment>?) {
        val events = mutableListOf<EventDay>()

        // Verifica se a lista está vazia ou nula
        if (appointments.isNullOrEmpty()) {
            println("Nenhum compromisso encontrado para este profissional.")
            calendarView.setEvents(emptyList()) // Garante que o calendário não tenha eventos antigos
            return
        }

        for (appointment in appointments) {
            val date = appointment.appointmentDate
            val calendar = Calendar.getInstance()
            calendar.set(date.year, date.month - 1, date.day) // Ajuste do mês

            println("Adicionando evento no dia: ${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.YEAR)}")

            // Adiciona um evento no dia do compromisso
            events.add(EventDay(calendar, R.drawable.ic_event_marker))
        }

        // Atualiza o calendário com os eventos
        calendarView.setEvents(events)
    }

}

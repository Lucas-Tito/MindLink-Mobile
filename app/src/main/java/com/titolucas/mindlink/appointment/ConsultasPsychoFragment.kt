package com.titolucas.mindlink.appointment

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.R
import com.titolucas.mindlink.home.repository.HomeRepository
import com.titolucas.mindlink.home.viewmodel.HomeViewModel
import com.titolucas.mindlink.home.viewmodel.HomeViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class ConsultasPsychoFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(HomeRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appointment_paciente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val container = view.findViewById<LinearLayout>(R.id.consultas_container)

        // Observa mudanças nos dados do ViewModel
        viewModel.appointmentCurrentMonth.observe(viewLifecycleOwner) { appointments ->
            container.removeAllViews() // Limpa os cards antes de adicionar novos

            appointments.forEach { consulta ->
                val card = LayoutInflater.from(context).inflate(R.layout.item_appointment_card, container, false)

                // Referências aos elementos do card
                val nomePatientTextView = card.findViewById<TextView>(R.id.nome_psico)
                val statusTextView = card.findViewById<TextView>(R.id.status_consulta)
                val dataTextView = card.findViewById<TextView>(R.id.data_consulta)
                val horaTextView = card.findViewById<TextView>(R.id.hora_consulta)

                // Preenchendo os dados no card
                nomePatientTextView.text = consulta.patientName
                dataTextView.text = "${consulta.appointmentDate.day}/${consulta.appointmentDate.month}/${consulta.appointmentDate.year}"
                horaTextView.text = String.format(
                    "%02d:%02d",
                    consulta.appointmentDate.hour.toIntOrNull() ?: 0,
                    consulta.appointmentDate.minutes.toIntOrNull() ?: 0
                )

                // Define status fixo ou dinâmico conforme lógica necessária
                val status = consulta.status // Defina um status conforme a necessidade
                statusTextView.text = status

                // Obtém o background do status
                val backgroundDrawable = statusTextView.background as? GradientDrawable

                // Define a cor do status
                when (status) {
                    "Cancelada" -> backgroundDrawable?.setColor(resources.getColor(R.color.status_cancelado, null))
                    "Agendada" -> backgroundDrawable?.setColor(resources.getColor(R.color.status_agendado, null))
                    "Solicitada" -> backgroundDrawable?.setColor(resources.getColor(R.color.status_solicitado, null))
                }

                // Adiciona o card ao container
                container.addView(card)
            }
        }

        // Busca os compromissos do mês do usuário atual
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { viewModel.getAppointmentsByProfessionalIdInCurrentMonth(it) }
    }
}

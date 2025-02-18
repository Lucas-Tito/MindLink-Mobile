package com.titolucas.mindlink.appointment

import android.app.AlertDialog
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.R
import com.titolucas.mindlink.generalData.Appointment
import com.titolucas.mindlink.home.repository.HomeRepository
import com.titolucas.mindlink.home.viewmodel.HomeViewModel
import com.titolucas.mindlink.home.viewmodel.HomeViewModelFactory
import java.util.*

class ConsultasPsychoFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(HomeRepository())
    }

    private lateinit var spinnerMonth: Spinner
    private lateinit var spinnerYear: Spinner
    private lateinit var consultasContainer: LinearLayout
    private var isPsychologist: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appointment_paciente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa os Spinners e o container dos cards
        spinnerMonth = view.findViewById(R.id.spinnerMonth)
        spinnerYear = view.findViewById(R.id.spinnerYear)
        consultasContainer = view.findViewById(R.id.consultas_container)

        setupMonthSpinner()
        setupYearSpinner()

        // Observa mudanças nos dados do ViewModel
        viewModel.appointmentCurrentMonth.observe(viewLifecycleOwner) { appointments ->
            println("📌 Antes de chamar carregarConsultas: $appointments")
            appointments.forEach { appointment ->
                if(appointment.appointmentId == null){
                    println("📌 AppointmentId recebido null: $appointment");
                    return@forEach
                }

            }
            carregarConsultas(appointments)
        }


        viewModel.updateStatusResult.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "Consulta atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                atualizarConsultas() // ✅ Atualiza a lista de consultas
            } else {
                Toast.makeText(requireContext(), "Erro ao atualizar consulta", Toast.LENGTH_SHORT).show()
            }
        }

        // Busca as consultas do mês atual
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { viewModel.getAppointmentsByProfessionalIdInCurrentMonth(it) }
    }

    /**
     * Configura o Spinner de Mês.
     */
    private fun setupMonthSpinner() {
        val meses = listOf(
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, meses)
        spinnerMonth.adapter = adapter

        // Define o mês atual como selecionado
        val mesAtual = Calendar.getInstance().get(Calendar.MONTH)
        spinnerMonth.setSelection(mesAtual)

        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                atualizarConsultas()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    /**
     * Configura o Spinner de Ano.
     */
    private fun setupYearSpinner() {
        val anoAtual = Calendar.getInstance().get(Calendar.YEAR)
        val anos = (anoAtual - 5..anoAtual + 5).toList().map { it.toString() } // Define um range de 5 anos antes e 5 anos depois

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, anos)
        spinnerYear.adapter = adapter

        // Define o ano atual como selecionado
        spinnerYear.setSelection(anos.indexOf(anoAtual.toString()))

        spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                atualizarConsultas()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    /**
     * Atualiza a lista de consultas com base na seleção do mês e ano.
     */
    private fun atualizarConsultas() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val mesSelecionado = spinnerMonth.selectedItemPosition + 1 // Janeiro = 0, precisamos somar 1
        val anoSelecionado = spinnerYear.selectedItem.toString().toInt()

        viewModel.getAppointmentsByUserIdInMonth(userId, mesSelecionado, anoSelecionado)
    }

    /**
     * Carrega os cards de consultas dinamicamente.
     */
    private fun carregarConsultas(consultas: List<Appointment>) {
        consultasContainer.removeAllViews() // Limpa os cards antes de adicionar novos

        consultas.forEach { consulta ->
            val cardView = LayoutInflater.from(context).inflate(R.layout.item_appointment_card, consultasContainer, false)
            if(consulta.appointmentId == null){
                println("📌 AppointmentId consulta carregada: $consulta");
                //return@forEach
            }
            // Preenchendo os dados no card
            cardView.findViewById<TextView>(R.id.nome_psico).text = consulta.professionalName ?: "Nome não disponível"
            cardView.findViewById<TextView>(R.id.data_consulta).text = "${consulta.appointmentDate.day}/${consulta.appointmentDate.month}/${consulta.appointmentDate.year}"
            cardView.findViewById<TextView>(R.id.hora_consulta).text = "${consulta.appointmentDate.hour}:${consulta.appointmentDate.minutes}"
            val statusTextView = cardView.findViewById<TextView>(R.id.status_consulta)
            statusTextView.text = consulta.status ?: "Status não disponível"

            // Verifica se o status não é nulo antes de definir a cor
            val backgroundDrawable = statusTextView.background as? GradientDrawable
            when (consulta.status) {
                "Cancelada" -> backgroundDrawable?.setColor(resources.getColor(R.color.status_cancelado, null))
                "Agendada" -> backgroundDrawable?.setColor(resources.getColor(R.color.status_agendado, null))
                "Solicitada" -> backgroundDrawable?.setColor(resources.getColor(R.color.status_solicitado, null))
            }

            val optionsButton = cardView.findViewById<ImageView>(R.id.imageView)
            optionsButton.setOnClickListener {
                showOptionsDialog(consulta)
            }

            // Adiciona o card ao container
            consultasContainer.addView(cardView)
        }
    }

    private fun showOptionsDialog(consulta: Appointment) {
        val options = mutableListOf<String>()
        println("Consulta: "+ consulta)
        // Adiciona "Aceitar Consulta" apenas se for psicólogo e status for "Solicitada"
        if (isPsychologist && consulta.status == "Solicitada") {
            options.add("Aceitar Consulta")
        }
        options.add("Cancelar Consulta")

        // Se a consulta já estiver cancelada, apenas informa o usuário
        if (consulta.status == "Cancelada") {
            AlertDialog.Builder(requireContext())
                .setTitle("Ação indisponível")
                .setMessage("Esta consulta já foi cancelada e não pode ser modificada.")
                .setPositiveButton("OK", null)
                .show()
            return // ✅ Sai do método para evitar execução desnecessária
        }

        // Exibe as opções disponíveis para o usuário
        AlertDialog.Builder(requireContext())
            .setTitle("Escolha uma ação")
            .setItems(options.toTypedArray()) { _, which ->
                when (options[which]) {
                    "Aceitar Consulta" -> {
                        consulta.appointmentId?.let {
                            viewModel.updateAppointmentStatus(it, "Agendada")
                        } ?: run {
                            Toast.makeText(requireContext(), "ID da consulta é nulo", Toast.LENGTH_SHORT).show()
                        }
                    }
                    "Cancelar Consulta" -> {
                        consulta.appointmentId?.let {
                            viewModel.updateAppointmentStatus(it, "Cancelada")
                        } ?: run {
                            Toast.makeText(requireContext(), "ID da consulta é nulo", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .show()
    }

}

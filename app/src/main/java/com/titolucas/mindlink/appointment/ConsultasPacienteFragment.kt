package com.titolucas.mindlink.appointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.titolucas.mindlink.R
import java.text.SimpleDateFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConsultasPacienteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConsultasPacienteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // Formato da data para exibição
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())


        // Utilizando o array de consultas mockadas
        consultasMockadas.forEach { consulta ->
            val dataHoraFormatada = dateFormat.format(consulta.dataHora)
            println("Consulta: ${consulta.nomePsico}, Status: ${consulta.status}, Data/Hora: $dataHoraFormatada")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consultas_paciente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val container = view.findViewById<LinearLayout>(R.id.consultas_container)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        consultasMockadas.forEach { consulta ->
            val card = LayoutInflater.from(context).inflate(R.layout.consulta_card, container, false)

            // Configure o card com os dados da consulta
            val nomePsicoTextView = card.findViewById<TextView>(R.id.nome_psico)
            val statusTextView = card.findViewById<TextView>(R.id.status_consulta)
            val dataTextView = card.findViewById<TextView>(R.id.data_consulta)
            val horaTextView = card.findViewById<TextView>(R.id.hora_consulta)

            nomePsicoTextView.text = consulta.nomePsico
            statusTextView.text = consulta.status
            dataTextView.text = dateFormat.format(consulta.dataHora)
            horaTextView.text = timeFormat.format(consulta.dataHora)

            // Adicione o card ao contêiner
            container.addView(card)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CalendarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConsultasPacienteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
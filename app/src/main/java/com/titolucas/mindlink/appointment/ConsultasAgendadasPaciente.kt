package com.titolucas.mindlink

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.titolucas.mindlink.appointment.consultasMockadas
import java.text.SimpleDateFormat
import java.util.Locale

class ConsultasAgendadasPaciente : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_consultas_agendadas_paciente)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Formato da data para exibição
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        // Utilizando o array de consultas mockadas
        consultasMockadas.forEach { consulta ->
            val dataHoraFormatada = dateFormat.format(consulta.dataHora)
            println("Consulta: ${consulta.nomePsico}, Status: ${consulta.status}, Data/Hora: $dataHoraFormatada")
        }
    }
}

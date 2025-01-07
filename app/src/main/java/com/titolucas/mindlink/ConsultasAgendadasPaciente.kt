package com.titolucas.mindlink

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ConsultasAgendadasPaciente : AppCompatActivity() {

    data class Consulta(
        val nomePsico: String,
        val status: String, // Exemplo: "Agendada", "Concluída", "Cancelada"
        val dataHora: Date // Usa a classe Date para representar data e hora
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_consultas_agendadas_paciente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Formato da data para exibição (dd/MM/yyyy HH:mm)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        // Criando instâncias de Date para as consultas
        val consultas = arrayOf(
            Consulta("João Silva", "Agendada", createDate(2025, 1, 10, 14, 0)),
            Consulta("Maria Oliveira", "Concluída", createDate(2025, 1, 5, 9, 30)),
            Consulta("Carlos Santos", "Cancelada", createDate(2025, 1, 7, 16, 0)),
            Consulta("Ana Costa", "Agendada", createDate(2025, 1, 12, 11, 15))
        )
    }



    // Função auxiliar para criar uma instância de Date
    private fun createDate(year: Int, month: Int, day: Int, hour: Int, minute: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1) // Janeiro é 0
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        return calendar.time
    }
}
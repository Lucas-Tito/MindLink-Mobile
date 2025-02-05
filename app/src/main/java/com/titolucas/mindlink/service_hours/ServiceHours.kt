package com.titolucas.mindlink.service_hours

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.titolucas.mindlink.R
import com.titolucas.mindlink.appointment.consultasMockadas
import java.text.SimpleDateFormat
import java.util.Locale

class ServiceHours : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_service_hours)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val container = findViewById<LinearLayout>(R.id.service_hours_container)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        horariosMockados.forEach { horario ->
            val card = LayoutInflater.from(this).inflate(R.layout.item_service_hours, container, false)

            // Configure o card com os dados da consulta
            val diaTextView = card.findViewById<TextView>(R.id.dia)
            val dataTextView = card.findViewById<TextView>(R.id.data)
            val horaTextView = card.findViewById<TextView>(R.id.hora)

            diaTextView.text = horario.dia
            dataTextView.text = dateFormat.format(horario.dataHora)
            horaTextView.text = timeFormat.format(horario.dataHora)

            // Adicione o card ao contêiner
            container.addView(card)
        }
    }
}

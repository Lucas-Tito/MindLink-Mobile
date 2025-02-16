package com.titolucas.mindlink.service_hours.view

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.R
import com.titolucas.mindlink.network.RetrofitInstance
import com.titolucas.mindlink.service_hours.data.AvailabilityRequest
import com.titolucas.mindlink.service_hours.data.EndTime
import com.titolucas.mindlink.service_hours.data.StartTime
import com.titolucas.mindlink.service_hours.data.horariosMockados
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import retrofit2.Callback
import retrofit2.Response

class ServiceHours : AppCompatActivity() {
    private val apiService = RetrofitInstance.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_service_hours)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val showBottomSheetButton = findViewById<ImageButton>(R.id.showBottomSheet)
        showBottomSheetButton.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)


            // Configurar o Spinner
            val dayInput = view.findViewById<Spinner>(R.id.day_input_bottom_sheet)
            ArrayAdapter.createFromResource(
                this,
                R.array.week_days,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                dayInput.adapter = adapter
            }

            val returnButton = findViewById<ImageButton>(R.id.service_hours_return_button)
            returnButton.setOnClickListener {
                finish()
            }

            // Declarar os inputs e botões
            val startingHourInput = view.findViewById<EditText>(R.id.starting_hour_bottom_sheet)
            val endingHourInput = view.findViewById<EditText>(R.id.ending_hour_bottom_sheet)
            val addButton = view.findViewById<Button>(R.id.add_service_hour_button)
            val cancelButton = view.findViewById<Button>(R.id.cancel_button_bottom_sheet)

            // Configurar o TimePicker para endingHourInput (opcional, se necessário)
            endingHourInput.setOnClickListener {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                    val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    endingHourInput.setText(formattedTime)
                }, hour, minute, true)

                timePickerDialog.show()
            }

            // Configurar o TimePicker para startingHourInput (opcional, se necessário)
            startingHourInput.setOnClickListener {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                    val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    startingHourInput.setText(formattedTime)
                }, hour, minute, true)

                timePickerDialog.show()
            }

            addButton.setOnClickListener {
                val dayOfWeek = dayInput.selectedItem.toString()
                val startTime = startingHourInput.text.toString().split(":")
                val endTime = endingHourInput.text.toString().split(":")

                val request = AvailabilityRequest(
                    dayOfWeek,
                    userId,
                    StartTime(startTime[0], startTime[1]),
                    EndTime(endTime[0], endTime[1])
                )

                apiService.postAvailability(request).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ServiceHours, "Horário adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                            println("Success")
                            bottomSheetDialog.dismiss()
                        } else {
                            Toast.makeText(this@ServiceHours, "Falha ao adicionar horário: ${response.code()}", Toast.LENGTH_SHORT).show()
                            println("Failed: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        println("Error: ${t.message}")
                    }
                })
            }


            cancelButton.setOnClickListener {
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }

        val container = findViewById<LinearLayout>(R.id.service_hours_container)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        if (userId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val availabilityList = apiService.getAvailability(userId)
                    withContext(Dispatchers.Main) {
                        availabilityList.forEach { availability ->
                            val card = LayoutInflater.from(this@ServiceHours).inflate(R.layout.item_service_hours, container, false)

                            // Configure o card com os dados da consulta
                            val diaTextView = card.findViewById<TextView>(R.id.dia)
                            val dataTextView = card.findViewById<TextView>(R.id.data)
                            val horaTextView = card.findViewById<TextView>(R.id.hora)

                            diaTextView.text = availability.dayOfWeek;
                            dataTextView.text = dateFormat.format(Calendar.getInstance().time) // Ajuste conforme necessário
                            horaTextView.text = "${availability.startTime.startHour}:${availability.startTime.startMinute} - ${availability.endTime.endHour}:${availability.endTime.endMinute}"

                            // Adicione o card ao contêiner
                            container.addView(card)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

package com.titolucas.mindlink.profile.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.R
import com.titolucas.mindlink.generalData.UserResponse
import com.titolucas.mindlink.profile.repository.ProfileRepository
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModel
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModelFactory

class ProfileEditActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(ProfileRepository())
    }

    private lateinit var user: UserResponse
    private var isProfessional: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            viewModel.fetchUserById(userId)// Caso ocorra uma exception de network security, adicione seu IPV4 em app\src\main\res\xml\network_security_config.xml
        } else {
            finish()
        }


        viewModel.userDetails.observe(this) { fetchedUser ->
            if (fetchedUser != null) {
                user = fetchedUser
                isProfessional = user.professionalType

                if (isProfessional) {
                    setContentView(R.layout.activity_profile_psychologist_edit)
                    setupProfessionalViewEdit(user)
                } else {
                    setContentView(R.layout.activity_profile_patient_edit)
                    setupPatienteViewEdit(user)
                }
            } else {
                Toast.makeText(this, "Erro ao carregar o perfil do usuário", Toast.LENGTH_SHORT).show()
            }
        }
    }


//simula a requisição pro back end
    private fun simulateApiCall(user: UserResponse, callback: (Boolean) -> Unit) {
        // Simula um atraso para imitar a latência da rede
        Handler(Looper.getMainLooper()).postDelayed({
            // Retorna um sucesso simulado
            callback(true)
        }, 2000) // 2 segundos de atraso
    }

    //Editar psicologo
    private fun setupProfessionalViewEdit(user: UserResponse) {

        val profileImage = findViewById<ShapeableImageView>(R.id.picture_profile_psichologist_edit)
        Glide.with(this)
            .load(user.photoURL) // URL da foto
            .placeholder(R.drawable.ic_user_placeholder) // Placeholder enquanto carrega
            .into(profileImage)
        val inputName = findViewById<EditText>(R.id.inputName)
        val inputLastName = findViewById<EditText>(R.id.inputLastName)
        val inputTitle = findViewById<EditText>(R.id.inputTitle)
        val inputEducation = findViewById<EditText>(R.id.inputEducation)
        val inputBio = findViewById<EditText>(R.id.inputBio)
        val inputEmail = findViewById<EditText>(R.id.inputEmail)
        val inputPassword = findViewById<EditText>(R.id.inputPassword)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Preencher os valores
        inputName.setText(user.name)
        inputLastName.setText(user.lastname)
        inputTitle.setText(user.title)
        inputEducation.setText(user.education)
        inputBio.setText(user.bio)
        inputEmail.setText(user.email)
        inputPassword.setText(user.password)

        //falta a Lógica para o botão de escolher foto no perfil

        btnSave.setOnClickListener {
            // Captura os dados dos campos
            val updatedUser = UserResponse(
                uid = user.uid,
                professionalType = user.professionalType,
                photoURL = user.photoURL, // Presume que a foto não foi alterada
                name = inputName.text.toString(),
                lastname = inputLastName.text.toString(),
                title = inputTitle.text.toString(),
                education = inputEducation.text.toString(),
                bio = inputBio.text.toString(),
                email = inputEmail.text.toString(),
                password = inputPassword.text.toString()
            )

            // Exibe um Toast indicando que o envio está em andamento
            Toast.makeText(this, "Salvando dados...", Toast.LENGTH_SHORT).show()

            // Simula o envio dos dados para o servidor
            simulateApiCall(updatedUser) { success ->
                if (success) {
                    Toast.makeText(this, "Dados salvos com sucesso!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Erro ao salvar os dados.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //Editar paciente
    private fun setupPatienteViewEdit(user: UserResponse) {

        val patienteImage = findViewById<ShapeableImageView>(R.id.picture_profile_patient_edit)
        Glide.with(this)
            .load(user.photoURL) // URL da foto
            .placeholder(R.drawable.ic_user_placeholder) // Placeholder enquanto carrega
            .into(patienteImage)
        val inputName = findViewById<EditText>(R.id.inputPatientName)
        val inputLastName = findViewById<EditText>(R.id.inputPatientLastName)
        val inputBio = findViewById<EditText>(R.id.inputPatientBio)
        val inputEmail = findViewById<EditText>(R.id.inputPatientEmail)
        val inputPassword = findViewById<EditText>(R.id.inputPatientPassword)
        val btnSave = findViewById<Button>(R.id.btnSavePatient)

        // Preencher os valores
        inputName.setText(user.name)
        inputLastName.setText(user.lastname)
        inputBio.setText(user.bio)
        inputEmail.setText(user.email)
        inputPassword.setText(user.password)



        //falta a Lógica para o botão de escolher foto no perfil


        btnSave.setOnClickListener {
            // Captura os dados dos campos
            val updatedUser = UserResponse(
                uid = user.uid,
                education = user.education,
                title = user.title,
                professionalType = user.professionalType,
                photoURL = user.photoURL, // Presume que a foto não foi alterada
                name = inputName.text.toString(),
                lastname = inputLastName.text.toString(),
                bio = inputBio.text.toString(),
                email = inputEmail.text.toString(),
                password = inputPassword.text.toString()
            )

            // Exibe um Toast indicando que o envio está em andamento
            Toast.makeText(this, "Salvando dados...", Toast.LENGTH_SHORT).show()

            // Simula o envio dos dados para o servidor
            simulateApiCall(updatedUser) { success ->
                if (success) {
                    Toast.makeText(this, "Dados salvos com sucesso!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Erro ao salvar os dados.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }





}
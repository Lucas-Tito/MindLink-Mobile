package com.titolucas.mindlink.profile.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.MainActivity
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
        val btnCancel = findViewById<Button>(R.id.btnCancel)

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

            Log.d("ProfileEditActivity", "btnSave clicado. Preparando dados para atualizar o address.")
            val updates = mutableMapOf<String, Any>()

            //insere no updates os dados que foram alterados
            if (inputName.text.toString() != user.name) updates["name"] = inputName.text.toString()
            if (inputLastName.text.toString() != user.lastname) updates["lastname"] = inputLastName.text.toString()
            if (inputTitle.text.toString() != user.title) updates["title"] = inputTitle.text.toString()
            if (inputEducation.text.toString() != user.education) updates["education"] = inputEducation.text.toString()
            if (inputBio.text.toString() != user.bio) updates["bio"] = inputBio.text.toString()
            if (inputEmail.text.toString() != user.email) updates["email"] = inputEmail.text.toString()
            if (inputPassword.text.toString() != user.password) updates["password"] = inputPassword.text.toString()

            viewModel.updateUser(user.uid, updates)

            viewModel.updateResponse.observe(this) { response ->
                if (response.message == "Usuário atualizado com sucesso") {
                    Log.d("ProfileEditActivity", "Perfil atualizado com sucesso!")
                    Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.d("ProfileEditActivity", "Falha ao atualizar o perfil: ${response.message}")
                    Toast.makeText(this, "Falha ao atualizar perfil", Toast.LENGTH_SHORT).show()
                }
            }
        }
        btnCancel.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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
        val btnCancel = findViewById<Button>(R.id.btnCancelPatient)

        // Preencher os valores
        inputName.setText(user.name)
        inputLastName.setText(user.lastname)
        inputBio.setText(user.bio)
        inputEmail.setText(user.email)
        inputPassword.setText(user.password)

        //falta a Lógica para o botão de escolher foto no perfil

        btnSave.setOnClickListener {
            // Captura os dados dos campos
            Log.d("ProfileEditActivity", "btnSave clicado. Preparando dados para atualizar o address.")
            val updates = mutableMapOf<String, Any>()

            //insere no updates os dados que foram alterados
            if (inputName.text.toString() != user.name) updates["name"] = inputName.text.toString()
            if (inputLastName.text.toString() != user.lastname) updates["lastname"] = inputLastName.text.toString()
            if (inputBio.text.toString() != user.bio) updates["bio"] = inputBio.text.toString()
            if (inputEmail.text.toString() != user.email) updates["email"] = inputEmail.text.toString()
            if (inputPassword.text.toString() != user.password) updates["password"] = inputPassword.text.toString()

            viewModel.updateUser(user.uid, updates)

            viewModel.updateResponse.observe(this) { response ->
                if (response.message == "Usuário atualizado com sucesso") {
                    Log.d("ProfileEditActivity", "Perfil atualizado com sucesso!")
                    Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.d("ProfileEditActivity", "Falha ao atualizar o perfil: ${response.message}")
                    Toast.makeText(this, "Falha ao atualizar perfil", Toast.LENGTH_SHORT).show()
                }
            }
        }
        btnCancel.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }





}
package com.titolucas.mindlink.profile.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.titolucas.mindlink.R
import com.titolucas.mindlink.profile.repository.ProfileRepository
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModel
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModelFactory
import com.google.android.material.imageview.ShapeableImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.profile.data.UserResponse

class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(ProfileRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            viewModel.fetchUserById(userId)
        } else {
            finish() // Fecha a atividade se não houver um usuário logado
        }

        // Observa os dados do usuário
        viewModel.userDetails.observe(this) { user ->
            if (user.professionalType) {
                setContentView(R.layout.activity_perfil_psicologo)
                setupProfessionalView(user)
            } else {
                setContentView(R.layout.activity_perfil_paciente)
                setupPatientView(user)
            }
        }
    }

    private fun setupProfessionalView(user: UserResponse) {
        val profileImage = findViewById<ShapeableImageView>(R.id.foto_perfil_psicologo)
        val profileName = findViewById<TextView>(R.id.nome_perfil_psicologo)
        val profileTitle = findViewById<TextView>(R.id.titulo_perfil_psicologo)
        val bioText = findViewById<TextView>(R.id.descricao_perfil)

        profileName.text = user.name
        profileTitle.text = "Psicólogo"
        bioText.text = user.bio ?: "Bio não disponível"
        Glide.with(this).load(user.photoURL).placeholder(R.drawable.ic_user_placeholder).into(profileImage)
    }

    private fun setupPatientView(user: UserResponse) {
        val profileImage = findViewById<ShapeableImageView>(R.id.foto_perfil_paciente)
        val profileName = findViewById<TextView>(R.id.nome_perfil_paciente)
        val bioText = findViewById<TextView>(R.id.bio_paciente)

        profileName.text = user.name
        bioText.text = user.bio ?: "Nenhuma bio disponível"
        Glide.with(this).load(user.photoURL).placeholder(R.drawable.ic_user_placeholder).into(profileImage)
    }
}


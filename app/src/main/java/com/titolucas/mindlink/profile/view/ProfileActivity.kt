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

class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(ProfileRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_psicologo)

        val profileImage = findViewById<ShapeableImageView>(R.id.foto_perfil_psicologo)
        val profileName = findViewById<TextView>(R.id.nome_perfil_psicologo)
        val profileTitle = findViewById<TextView>(R.id.titulo_perfil_psicologo)
        val bioText = findViewById<TextView>(R.id.descricao_perfil)

        viewModel.userDetails.observe(this) { user ->
            profileName.text = user.name
            profileTitle.text = if (user.professionalType) "Psicólogo" else "Paciente"
            bioText.text = user.bio ?: "Bio não disponível"
            Glide.with(this).load(user.photoURL).placeholder(R.drawable.ic_user_placeholder).into(profileImage)
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            viewModel.fetchUserById(userId)
        } else {
            // Caso o usuário não esteja logado, trate a situação aqui
            finish()
        }
    }
}


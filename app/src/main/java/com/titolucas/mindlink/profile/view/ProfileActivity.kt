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
import com.titolucas.mindlink.generalData.UserResponse

class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(ProfileRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
                viewModel.fetchUserById(userId)// Caso ocorra uma exception de network security, adicione seu IPV4 em app\src\main\res\xml\network_security_config.xml

        } else {
            finish()
        }

        viewModel.userDetails.observe(this) { user ->
            println("User from backend: "+ user.toString())
            if (user.professionalType) {
                setContentView(R.layout.activity_profile_pyschologist)
                setupProfessionalView(user)
            } else {
                setContentView(R.layout.activity_profile_patient)
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

    private fun setupPatientView(user: UserResponse){
        val profileImage = findViewById<ShapeableImageView>(R.id.picture_profile_patient)
        val profileName = findViewById<TextView>(R.id.name_profile_patient)
        val bioText = findViewById<TextView>(R.id.bio_patient)
        val titleText = findViewById<TextView>(R.id.titleNameText)

        titleText.text = user.title
        profileName.text = user.name
        bioText.text = user.bio ?: "Nenhuma bio disponível"
        Glide.with(this).load(user.photoURL).placeholder(R.drawable.ic_user_placeholder).into(profileImage)
    }
}


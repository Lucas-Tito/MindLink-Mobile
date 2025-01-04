package com.titolucas.mindlink.profile.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModel

import com.titolucas.mindlink.R

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileImage = view.findViewById<ImageView>(R.id.foto_perfil_psicologo)
        val profileName = view.findViewById<TextView>(R.id.nome_perfil_psicologo)
        val profileTitle = view.findViewById<TextView>(R.id.titulo_perfil_psicologo)
        val bioText = view.findViewById<TextView>(R.id.descricao_perfil)

        viewModel.userDetails.observe(viewLifecycleOwner) { user ->
            profileName.text = user.name
            profileTitle.text = if (user.professionalType) "Psicólogo" else "Paciente"
            bioText.text = user.bio
            Glide.with(this).load(user.photoURL).into(profileImage)
        }

        viewModel.fetchUserById("USER_ID")
    }
}

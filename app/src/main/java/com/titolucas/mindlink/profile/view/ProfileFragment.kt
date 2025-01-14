package com.titolucas.mindlink.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.titolucas.mindlink.R
import com.titolucas.mindlink.generalData.UserResponse
import com.titolucas.mindlink.profile.repository.ProfileRepository
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModel
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModelFactory
import com.google.android.material.imageview.ShapeableImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(ProfileRepository())
    }

    private var isProfessional: Boolean = false
    private lateinit var user: UserResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
            return null
        }


        viewModel.fetchUserById(userId)
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observar as informações do usuário e configurar a interface
        viewModel.userDetails.observe(viewLifecycleOwner) { fetchedUser ->
            user = fetchedUser
            isProfessional = user.professionalType

            // Inflar o layout correspondente
            //val layoutId = if (isProfessional) {
                //R.layout.activity_perfil_psicologo
            //} else {
            var layoutId= R.layout.activity_perfil_paciente
           // }


            val viewGroup = view.parent as ViewGroup
            val newView = layoutInflater.inflate(layoutId, viewGroup, false)
            viewGroup.removeAllViews()
            viewGroup.addView(newView)


            if (isProfessional) {
                setupProfessionalView(newView, user)
            } else {
                setupPatientView(newView, user)
            }
        }
    }

    private fun setupProfessionalView(view: View, user: UserResponse) {
        val profileImage = view.findViewById<ShapeableImageView>(R.id.foto_perfil_psicologo)
        val profileName = view.findViewById<TextView>(R.id.nome_perfil_psicologo)
        val profileTitle = view.findViewById<TextView>(R.id.titulo_perfil_psicologo)
        val bioText = view.findViewById<TextView>(R.id.descricao_perfil)

        profileName.text = user.name
        profileTitle.text = "Psicólogo"
        bioText.text = user.bio ?: "Bio não disponível"
        Glide.with(this).load(user.photoURL).placeholder(R.drawable.ic_user_placeholder).into(profileImage)
    }

    private fun setupPatientView(view: View, user: UserResponse) {
        val profileImage = view.findViewById<ShapeableImageView>(R.id.foto_perfil_paciente)
        val profileName = view.findViewById<TextView>(R.id.nome_perfil_paciente)
        val education = view.findViewById<TextView>(R.id.education_paciente)
        val bioText = view.findViewById<TextView>(R.id.bio_paciente)

        profileName.text = user.name
        bioText.text = user.bio ?: "Nenhuma bio disponível"
        education.text = user.education
        Glide.with(this).load(user.photoURL).placeholder(R.drawable.ic_user_placeholder).into(profileImage)
    }
}

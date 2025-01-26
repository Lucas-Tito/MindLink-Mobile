package com.titolucas.mindlink.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.R
import com.titolucas.mindlink.generalData.UserResponse
import com.titolucas.mindlink.profile.repository.ProfileRepository
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModel
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModelFactory

class PatientProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(ProfileRepository())
    }

    private lateinit var user: UserResponse
    private var isProfessional: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_perfil_pessoal_paciente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
            return
        }

        viewModel.fetchUserById(userId)

        viewModel.userDetails.observe(viewLifecycleOwner) { fetchedUser ->
            if (fetchedUser != null) {
                user = fetchedUser
                isProfessional = user.professionalType

                // Atualiza o layout baseado no tipo do usuário
                val parent = view.parent as ViewGroup
                val layoutId = if (isProfessional) {
                    R.layout.activity_perfil_agendamento
                } else {
                    R.layout.activity_perfil_pessoal_paciente
                }

                parent.removeAllViews()
                val newView = layoutInflater.inflate(layoutId, parent, false)
                parent.addView(newView)

                if (isProfessional) {
                    setupProfessionalView(newView, user)
                } else {
                    setupPatientView(newView, user)
                }
            } else {
                Toast.makeText(requireContext(), "Erro ao carregar o perfil do usuário", Toast.LENGTH_SHORT).show()
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
        education.text = user.education ?: "Sem informações de educação"
        Glide.with(this).load(user.photoURL).placeholder(R.drawable.ic_user_placeholder).into(profileImage)
    }
}

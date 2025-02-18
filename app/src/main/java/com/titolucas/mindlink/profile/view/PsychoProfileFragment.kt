package com.titolucas.mindlink.profile.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.R
import com.titolucas.mindlink.generalData.UserResponse
import com.titolucas.mindlink.login.view.LoginActivity
import com.titolucas.mindlink.profile.repository.ProfileRepository
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModel
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModelFactory

class PsychoProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(ProfileRepository())
    }

    private lateinit var user: UserResponse
    private var isProfessional: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_profile_pyschologist, container, false)
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

                val profileImage = view.findViewById<ShapeableImageView>(R.id.picture_profile_psychologist)
                val profileName = view.findViewById<TextView>(R.id.name_profile)
                val profileTitle = view.findViewById<TextView>(R.id.titleNameText)
                val bioText = view.findViewById<TextView>(R.id.bio_psichologist)
                val emailText = view.findViewById<TextView>(R.id.Email_text)
                val educationText = view.findViewById<TextView>(R.id.education_text)
                val editProfileButton = view.findViewById<TextView>(R.id.tvEditarPerfil)
                val btnLogout = view.findViewById<TextView>(R.id.btnLogout)

                educationText.text = user.education
                profileName.text = user.name
                profileTitle.text = user.title
                bioText.text = user.bio ?: "Bio não disponível"
                emailText.text = user.email
                Glide.with(this).load(user.photoURL).placeholder(R.drawable.ic_user_placeholder).into(profileImage)

                editProfileButton.setOnClickListener {
                    val intent = Intent(requireContext(), ProfileEditActivity::class.java)
                    startActivity(intent)
                }

                btnLogout.setOnClickListener {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }


            } else {
                Toast.makeText(requireContext(), "Erro ao carregar o perfil do usuário", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
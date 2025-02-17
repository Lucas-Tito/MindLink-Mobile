package com.titolucas.mindlink

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.appointment.ConsultasPacienteFragment
import com.titolucas.mindlink.databinding.ActivityMainBinding
import com.titolucas.mindlink.home.view.PatientHomeFragment
import com.titolucas.mindlink.messages.view.ChatSelector
import com.titolucas.mindlink.profile.view.PatientProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeView(PatientHomeFragment())
        Log.i("", "Entrou no fluxo do paciente")

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val isProfessionalType = false
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.bottomNavigationView.setOnItemSelectedListener {

            val args = Bundle().apply {
                putString("USER_ID", userId)
                putBoolean("isProfessionalType",isProfessionalType)
            }

            val itemId: Int = it.getItemId()
            if (itemId == R.id.home_nav) {
                changeView(PatientHomeFragment(), args)
            } else if (itemId == R.id.consultas_nav) {
                changeView(ConsultasPacienteFragment(), args)
            } else if (itemId == R.id.chat_nav) {
                changeView(ChatSelector(), args)
            } else if (itemId == R.id.profile_nav) {
                changeView(PatientProfileFragment(), args)
            }
            true
        }
    }

    private fun changeView(fragment: Fragment, args: Bundle? = null) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (args != null) {
            fragment.arguments = args
        }

        fragmentTransaction.replace(R.id.main_frame_layout, fragment)
        fragmentTransaction.commit()
    }
}

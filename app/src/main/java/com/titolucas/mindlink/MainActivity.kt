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
import com.titolucas.mindlink.home.view.HomeFragment
import com.titolucas.mindlink.profile.view.ProfileFragment
import java.sql.DriverManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeView(HomeFragment())
        Log.i("", "Entrou no fluxo do paciente")

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.bottomNavigationView.setOnItemSelectedListener {

            val args = Bundle().apply {
                putString("USER_ID", userId)
            }

            when (it.itemId) {
                R.id.home_nav -> changeView(HomeFragment(), args)
                R.id.consultas_nav -> changeView(ConsultasPacienteFragment(), args)
                R.id.chat_nav -> changeView(ChatFragment(), args)
                R.id.profile_nav -> changeView(ProfileFragment(), args)
                else -> {}
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

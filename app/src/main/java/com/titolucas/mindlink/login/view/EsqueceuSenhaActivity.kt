package com.titolucas.mindlink.login.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.R
import com.titolucas.mindlink.databinding.ActivityEsqueceuSenhaBinding

class EsqueceuSenhaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEsqueceuSenhaBinding
    private var email = ""
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_esqueceu_senha)

        binding = ActivityEsqueceuSenhaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Inicializando o estado inicial da ProgressBar como invisível
        binding.pBar.visibility = View.GONE

        binding.sendBtn.setOnClickListener {
            email = binding.EsqueceuSenhaEmail.text.toString().trim()

            if (email.isEmpty()) {
                binding.EsqueceuSenhaEmail.error = "Insira seu email"
                return@setOnClickListener
            }

            // Inicia o envio do e-mail
            binding.sendBtn.visibility = View.GONE
            binding.pBar.visibility = View.VISIBLE  // Agora a ProgressBar aparece

            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                binding.sendBtn.visibility = View.VISIBLE
                binding.pBar.visibility = View.GONE

                Snackbar.make(
                    binding.root,
                    "Link de recuperação enviado para seu email $email",
                    Snackbar.ANIMATION_MODE_SLIDE
                ).show()
            }.addOnFailureListener {
                binding.sendBtn.visibility = View.VISIBLE
                binding.pBar.visibility = View.GONE
                Snackbar.make(
                    binding.root,
                    "Erro ao enviar link de recuperação",
                    Snackbar.ANIMATION_MODE_SLIDE
                ).show()
            }
        }

        // Ação para voltar ao login
        binding.setaVoltarEsqueceuSenha.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}

package com.titolucas.mindlink.register.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.titolucas.mindlink.R
import com.titolucas.mindlink.login.view.LoginActivity
import com.titolucas.mindlink.profile.view.ProfileEditActivity
import com.titolucas.mindlink.register.data.RegisterResult
import com.titolucas.mindlink.register.viewmodel.RegisterViewModel
import com.titolucas.mindlink.register.viewmodel.RegisterViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private val viewModel: RegisterViewModel by viewModels { RegisterViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nameField = findViewById<EditText>(R.id.nomeRegistrarInput)
        val emailField = findViewById<EditText>(R.id.emailRegistrarInput)
        val passwordField = findViewById<EditText>(R.id.senhaRegistrarInput)
        val registerButton = findViewById<Button>(R.id.buttonEnviarRegistrar)
        val buttonVoltarLogin = findViewById<Button>(R.id.buttonVoltarLoginRegitrar)

        registerButton.setOnClickListener {
            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (validateInputs(name, email, password)) {
                viewModel.register(name, email, password, null)
            }
        }

        buttonVoltarLogin.setOnClickListener {
            finish()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.registerState.observe(this) { result ->
            when (result) {
                is RegisterResult.Success -> {
                    Toast.makeText(this, "Registro bem-sucedido!", Toast.LENGTH_SHORT).show()
                    finish() // Voltar ou navegar para outra tela
                }
                is RegisterResult.Error -> {
                    Toast.makeText(this, "Erro: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateInputs(name: String, email: String, password: String): Boolean {
        return when {
            name.isEmpty() -> {
                Toast.makeText(this, "Preencha o nome!", Toast.LENGTH_SHORT).show()
                false
            }
            email.isEmpty() -> {
                Toast.makeText(this, "Preencha o e-mail!", Toast.LENGTH_SHORT).show()
                false
            }
            password.isEmpty() || password.length < 6 -> {
                Toast.makeText(this, "Senha inválida!", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }
}
package com.titolucas.mindlink.login.view
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity


import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.MainActivity
import com.titolucas.mindlink.R
import com.titolucas.mindlink.login.data.LoginResult
import com.titolucas.mindlink.login.viewmodel.LoginViewModel
import com.titolucas.mindlink.login.viewmodel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory() // Crie um factory se necessário para passar o repositório
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailInput = findViewById(R.id.editTextEmail)
        passwordInput = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        progressBar = findViewById(R.id.progressBar)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (validateInputs(email, password)) {
                progressBar.visibility = View.VISIBLE
                viewModel.login(email, password)
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loginState.observe(this) { result ->
            progressBar.visibility = View.GONE
            when (result) {
                is LoginResult.Success -> {
                    Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is LoginResult.Error -> {
                    Toast.makeText(this, "Erro: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                emailInput.error = "Digite o e-mail"
                false
            }
            TextUtils.isEmpty(password) -> {
                passwordInput.error = "Digite a senha"
                false
            }
            password.length < 6 -> {
                passwordInput.error = "A senha deve ter pelo menos 6 caracteres"
                false
            }
            else -> true
        }
    }
}

package com.titolucas.mindlink.login.view
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


import com.titolucas.mindlink.R
import com.titolucas.mindlink.login.data.LoginResult
import com.titolucas.mindlink.login.viewmodel.LoginViewModel
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModel
import com.titolucas.mindlink.login.viewmodel.LoginViewModelFactory
import com.titolucas.mindlink.MainActivity
import com.titolucas.mindlink.MainPsycho
import com.titolucas.mindlink.profile.repository.ProfileRepository
import com.titolucas.mindlink.profile.viewmodel.ProfileViewModelFactory
import com.titolucas.mindlink.register.view.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var esqueceuButton : Button
    private lateinit var signupButton : Button


    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory()
    }
    private val profileViewModel: ProfileViewModel by
    viewModels{
        ProfileViewModelFactory(ProfileRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailInput = findViewById(R.id.editTextEmail)
        passwordInput = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        esqueceuButton = findViewById(R.id.buttonEsqueceuSenha)
        progressBar = findViewById(R.id.progressBar)
        signupButton = findViewById(R.id.buttonSignUp)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (validateInputs(email, password)) {
                progressBar.visibility = View.VISIBLE
                viewModel.login(email, password)
            }
        }

        esqueceuButton.setOnClickListener {
            val intent = Intent(this, EsqueceuSenhaActivity::class.java)
            startActivity(intent)
        }

        signupButton.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loginState.observe(this) { result ->
            progressBar.visibility = View.GONE
            when (result) {
                is LoginResult.Success -> {
                    Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this, SearchActivity::class.java))
                    // Obter o userId diretamente do FirebaseAuth
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        // Buscar o tipo de usuário
                        fetchUserTypeAndNavigate(userId)
                    } else {
                        Toast.makeText(this, "Erro ao obter o ID do usuário", Toast.LENGTH_SHORT).show()
                    }
                }
                is LoginResult.Error -> {
                    Toast.makeText(this, "Erro: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun fetchUserTypeAndNavigate(userId: String) {
        // Busca os detalhes do usuário no ViewModel
        profileViewModel.fetchUserById(userId)

        profileViewModel.userDetails.observe(this) { fetchedUser ->
            if (fetchedUser != null) {
                val isProfessional = fetchedUser.professionalType

                // Navega para a tela correta
                if (isProfessional) {
                    startActivity(Intent(this, MainPsycho::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                finish()
            } else {
                Toast.makeText(this, "Erro ao carregar o perfil do usuário", Toast.LENGTH_SHORT).show()
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

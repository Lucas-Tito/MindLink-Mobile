package com.titolucas.mindlink
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null
    private var loginButton: Button? = null
    private var progressBar: ProgressBar? = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailInput = findViewById<EditText>(R.id.editTextEmail)
        passwordInput = findViewById<EditText>(R.id.editTextPassword)
        loginButton = findViewById<Button>(R.id.buttonLogin)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)


        mAuth = FirebaseAuth.getInstance()


        this.loginButton!!.setOnClickListener(View.OnClickListener {
            val email = this.emailInput!!.getText().toString().trim { it <= ' ' }
            val password = this.passwordInput!!.getText().toString().trim { it <= ' ' }

            if (TextUtils.isEmpty(email)) {
                this.emailInput!!.setError("Digite o e-mail")
                return@OnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                this.passwordInput!!.setError("Digite a senha")
                return@OnClickListener
            }

            if (password.length < 6) {
                this.passwordInput!!.setError("A senha deve ter pelo menos 6 caracteres")
                return@OnClickListener
            }

            this.progressBar!!.setVisibility(View.VISIBLE)

            mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    progressBar!!.setVisibility(View.GONE)
                    if (task.isSuccessful()) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login bem-sucedido!",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(
                            Intent(
                                this@LoginActivity,
                                MainActivity::class.java
                            )
                        )
                        finish()
                    } else {
                        Log.e(
                            "LoginActivity",
                            "Erro ao fazer login",
                            task.getException()
                        )
                        Toast.makeText(
                            this@LoginActivity,
                            "Erro ao fazer login: " + task.getException()!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        })
    }
}

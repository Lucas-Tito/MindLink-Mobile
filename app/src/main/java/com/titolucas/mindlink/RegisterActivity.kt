package com.titolucas.mindlink

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nameField = findViewById<EditText>(R.id.editTextText)
        val emailField = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordField = findViewById<EditText>(R.id.editTextNumberPassword)
        val registerButton = findViewById<Button>(R.id.button3)

        registerButton.setOnClickListener {
            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(name, email, password, null) // Substitua `null` pela lógica para obter o Uri da foto, se necessário
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String, photoUri: Uri?) {
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val uid = it.uid
                        if (photoUri != null) {
                            val storageRef = storage.reference.child("user-photos/$uid/${photoUri.lastPathSegment}")
                            storageRef.putFile(photoUri)
                                .addOnSuccessListener {
                                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                        updateUserProfileAndDatabase(uid, name, email, password, downloadUrl.toString())
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Falha no upload da foto: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            updateUserProfileAndDatabase(uid, name, email, password, null)
                        }
                    }
                } else {
                    Toast.makeText(this, "Erro ao registrar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUserProfileAndDatabase(
        uid: String,
        name: String,
        email: String,
        password: String,
        photoUrl: String?
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val userMap = hashMapOf(
            "uid" to uid,
            "name" to name,
            "email" to email,
            "password" to password,
            "photoURL" to photoUrl
        )

        firestore.collection("Users").document(uid).set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao salvar no Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
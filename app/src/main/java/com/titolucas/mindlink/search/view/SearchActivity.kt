package com.titolucas.mindlink.search.view

import android.os.Bundle
import android.text.TextUtils.replace
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.titolucas.mindlink.R

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val editTextSearch = findViewById<EditText>(R.id.editTextSearch)
        val buttonSearch = findViewById<Button>(R.id.buttonSearch)

        // Adiciona o SearchFragment ao container
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragmentContainer, SearchFragment())
            }
        }

        // Configura o botão de busca
        buttonSearch.setOnClickListener {
            val query = editTextSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as? SearchFragment
                fragment?.performSearch(query)
            }
        }
    }
}
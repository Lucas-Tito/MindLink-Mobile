package com.titolucas.mindlink.home.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.titolucas.mindlink.R
import com.titolucas.mindlink.search.view.SearchAdapter
import com.titolucas.mindlink.home.repository.HomeRepository
import com.titolucas.mindlink.home.viewmodel.HomeViewModel
import com.titolucas.mindlink.home.viewmodel.HomeViewModelFactory

class PatientHomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(HomeRepository())
    }
    private val adapter = SearchAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchField = view.findViewById<EditText>(R.id.search_field)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        // Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Observando os resultados iniciais
        viewModel.professionalUsers.observe(viewLifecycleOwner) { results ->
            adapter.submitList(results)
        }

        // Observando resultados da pesquisa
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            adapter.submitList(results)
        }

        // Carregar psicólogos na inicialização
        viewModel.loadProfessionalUsers()

        // Configurar busca dinâmica
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.searchPsychologists(query)
                } else {
                    viewModel.loadProfessionalUsers() // Retorna à lista inicial
                }
            }
        })
    }
}

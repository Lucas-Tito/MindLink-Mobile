package com.titolucas.mindlink.search.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.titolucas.mindlink.R
import com.titolucas.mindlink.search.repository.SearchRepository
import com.titolucas.mindlink.search.repository.SearchViewModelFactory
import com.titolucas.mindlink.search.viewmodel.SearchViewModel

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(SearchRepository())
    }
    private val adapter = SearchAdapter()
    private lateinit var recyclerViewSearch: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewSearch = view.findViewById(R.id.recyclerViewSearch) ;
        // Configurar RecyclerView
        recyclerViewSearch.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewSearch.adapter = adapter

        // Observar os dados da ViewModel e depois atualiza a lista de resultados
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            adapter.submitList(results)
        }

        // busca inicial
        viewModel.searchPsychologists("terapia")
    }

    fun performSearch(query: String) {
        viewModel.searchPsychologists(query)
    }

}
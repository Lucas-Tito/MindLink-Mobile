package com.titolucas.mindlink.agenda.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.titolucas.mindlink.R;
import com.titolucas.mindlink.agenda.repository.AgendaRepository;
import com.titolucas.mindlink.agenda.viewmodel.AgendaViewModel;
import com.titolucas.mindlink.agenda.viewmodel.AgendaViewModelFactory;

public class AgendaFragment extends Fragment {

    private AgendaViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar o ViewModel
        AgendaRepository repository = new AgendaRepository();
        AgendaViewModelFactory factory = new AgendaViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(AgendaViewModel.class);

        // Observar os dados
        viewModel.getAgendaLiveData().observe(getViewLifecycleOwner(), result -> {
            // Aqui você pode atualizar a UI com os dados de AgendaResult
            // Por exemplo: Atualizar um RecyclerView
        });

        // Carregar os dados
        viewModel.loadAgendaData();
    }
}

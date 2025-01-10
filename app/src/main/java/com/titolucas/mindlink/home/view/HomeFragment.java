package com.titolucas.mindlink.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.titolucas.mindlink.PsychologistAdapter;
import com.titolucas.mindlink.R;
import com.titolucas.mindlink.home.repository.HomeRepository;
import com.titolucas.mindlink.home.viewmodel.HomeViewModel;
import com.titolucas.mindlink.home.viewmodel.HomeViewModelFactory;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Configurar ViewModel
        HomeRepository repository = new HomeRepository();
        HomeViewModelFactory factory = new HomeViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);

        // Observar os dados do ViewModel
        viewModel.getHomeLiveData().observe(getViewLifecycleOwner(), homeData -> {
            RecyclerView.Adapter adapter = new PsychologistAdapter(homeData.getPsychologistList());
            recyclerView.setAdapter(adapter);
        });

        // Carregar os dados do ViewModel
        viewModel.loadHomeData();
    }
}

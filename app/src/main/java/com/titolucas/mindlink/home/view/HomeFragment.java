package com.titolucas.mindlink.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.titolucas.mindlink.PsychologistAdapter;
import com.titolucas.mindlink.R;
import com.titolucas.mindlink.agenda.view.AgendaFragment;
import com.titolucas.mindlink.home.repository.HomeRepository;
import com.titolucas.mindlink.home.viewmodel.HomeViewModel;
import com.titolucas.mindlink.home.viewmodel.HomeViewModelFactory;
import com.titolucas.mindlink.perfil.view.PerfilFragment;


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

        // Configurar BottomNavigationView
        setupBottomNavigationView(view);

        // Carregar o fragmento inicial (AgendaFragment)
        replaceInternalFragment(new AgendaFragment());
    }

    /**
     * Configura o BottomNavigationView para alternar entre fragmentos.
     */
    private void setupBottomNavigationView(View view) {
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                // Apenas exibe o RecyclerView da Home
                recyclerView.setVisibility(View.VISIBLE);
                replaceInternalFragment(null);
                return true;

            } else if (itemId == R.id.agenda) {
                // Trocar para AgendaFragment
                recyclerView.setVisibility(View.GONE); // Esconde a lista da Home
                replaceInternalFragment(new AgendaFragment());
                return true;

            } else if (itemId == R.id.perfil) {
                // Trocar para PerfilFragment
                recyclerView.setVisibility(View.GONE); // Esconde a lista da Home
                replaceInternalFragment(new PerfilFragment());
                return true;

            } else {
                return false;
            }
        });
    }


    /**
     * Substitui o fragmento interno carregado no container.
     */
    private void replaceInternalFragment(@Nullable Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();

        if (fragment == null) {
            // Limpa o container se o fragmento for nulo
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_internal_container, new Fragment()) // Adiciona um fragmento vazio
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_internal_container, fragment)
                    .commit();
        }
    }
}

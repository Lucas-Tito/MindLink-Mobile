package com.titolucas.mindlink;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.titolucas.mindlink.appointment.ConsultasPacienteFragment;
import com.titolucas.mindlink.databinding.ActivityMainPsychoBinding;
import com.titolucas.mindlink.home.view.HomeFragment;
import com.titolucas.mindlink.profile.view.ProfileFragment;

public class MainPsycho extends AppCompatActivity {

    private ActivityMainPsychoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainPsychoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        changeView(new HomeFragment(),null);

        String userId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        enableEdgeToEdge();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Bundle args = new Bundle();
            args.putString("USER_ID", userId);

            int itemId = item.getItemId();
            if (itemId == R.id.home_nav) {
                changeView(new HomeFragment(), args);
            } else if (itemId == R.id.consultas_nav) {
                changeView(new ConsultasPacienteFragment(), args);
            } else if (itemId == R.id.chat_nav) {
                changeView(new ChatFragment(), args);
            } else if (itemId == R.id.profile_nav) {
                changeView(new ProfileFragment(), args);
            }
            return true;
        });

    }

    private void changeView(@NonNull Fragment fragment, Bundle args) {
        if (args != null) {
            fragment.setArguments(args);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame_layout, fragment)
                .commit();
    }

    private void enableEdgeToEdge() {
        // Implementar se houver configuração específica para edge-to-edge.
    }
}

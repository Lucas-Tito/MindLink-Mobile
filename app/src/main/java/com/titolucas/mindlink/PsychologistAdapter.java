package com.titolucas.mindlink;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PsychologistAdapter extends RecyclerView.Adapter<PsychologistAdapter.PsychologistViewHolder> {

    private final List<Psychologist> psychologistList;

    public PsychologistAdapter(List<Psychologist> psychologistList) {
        this.psychologistList = psychologistList;
    }

    @NonNull
    @Override
    public PsychologistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_psychologist, parent, false);
        return new PsychologistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PsychologistViewHolder holder, int position) {
        Psychologist psychologist = psychologistList.get(position);

        // Configurar os textos
        holder.name.setText(psychologist.getName());
        holder.specialty.setText(psychologist.getSpecialty());
        holder.rating.setText("★ " + psychologist.getRating());

        // Configurar a imagem
        if ("Ayrton Senna".equals(psychologist.getName())) {
            holder.image.setImageResource(R.drawable.sena); // Use sena.png
        } else {
            holder.image.setImageResource(R.drawable.ic_profile); // Placeholder padrão
        }

        // Configurar ação do botão (opcional)
        holder.scheduleButton.setOnClickListener(v -> {
            // Ação ao clicar no botão
        });
    }

    @Override
    public int getItemCount() {
        return psychologistList.size();
    }

    public static class PsychologistViewHolder extends RecyclerView.ViewHolder {
        TextView name, specialty, rating;
        ImageView image;
        Button scheduleButton;

        public PsychologistViewHolder(@NonNull View itemView) {
            super(itemView);

            // Vincular os elementos do layout
            name = itemView.findViewById(R.id.psychologist_name);
            specialty = itemView.findViewById(R.id.psychologist_specialty);
            rating = itemView.findViewById(R.id.psychologist_rating);
            image = itemView.findViewById(R.id.psychologist_image);
            scheduleButton = itemView.findViewById(R.id.schedule_button);
        }
    }
}

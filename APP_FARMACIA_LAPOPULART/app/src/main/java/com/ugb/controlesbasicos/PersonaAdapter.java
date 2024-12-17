package com.ugb.controlesbasicos;

// PersonaAdapter.java
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PersonaAdapter extends RecyclerView.Adapter<PersonaAdapter.PersonaViewHolder> {
    private Context context;
    private List<Persona> personas;

    public PersonaAdapter(Context context, List<Persona> personas) {
        this.context = context;
        this.personas = personas;
    }

    @NonNull
    @Override
    public PersonaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_persona, parent, false);
        return new PersonaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonaViewHolder holder, int position) {
        Persona persona = personas.get(position);
        holder.txtNombre.setText(persona.getNombre());
        holder.txtProfesion.setText(persona.getProfesion());
        holder.imgPersona.setImageResource(persona.getFoto());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetallePersonaActivity.class);
            intent.putExtra("nombre", persona.getNombre());
            intent.putExtra("edad", persona.getEdad());
            intent.putExtra("correo", persona.getCorreo());
            intent.putExtra("profesion", persona.getProfesion());
            intent.putExtra("trabajoProfesional", persona.getTrabajoProfesional());
            intent.putExtra("foto", persona.getFoto());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return personas.size();
    }

    public static class PersonaViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtProfesion;
        ImageView imgPersona;

        public PersonaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtProfesion = itemView.findViewById(R.id.txtProfesion);
            txtProfesion = itemView.findViewById(R.id.txtProfesion);
            imgPersona = itemView.findViewById(R.id.imgPersona);
        }
    }
}

package com.conectatec.ui.docente.tareas.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.data.model.Grupo;
import com.conectatec.databinding.ItemGrupoDocenteBinding;

import java.util.ArrayList;
import java.util.List;

public class GrupoTareasDocenteAdapter
        extends RecyclerView.Adapter<GrupoTareasDocenteAdapter.ViewHolder> {

    public interface OnGrupoClickListener {
        void onClick(Grupo grupo);
    }

    private final List<Grupo> lista = new ArrayList<>();
    private OnGrupoClickListener listener;

    public GrupoTareasDocenteAdapter() { }

    public void setOnClickListener(OnGrupoClickListener l) {
        this.listener = l;
    }

    public void setLista(List<Grupo> nuevaLista) {
        lista.clear();
        lista.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    public int conteo() {
        return lista.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemGrupoDocenteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Grupo g = lista.get(position);
        h.bind(g);
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(g);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemGrupoDocenteBinding b;

        ViewHolder(ItemGrupoDocenteBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        void bind(Grupo g) {
            b.tvInicialGrupoDocente.setText(extraerIniciales(g.nombre));
            b.tvNombreGrupoDocente.setText(g.nombre);
            b.tvMateriaGrupoDocente.setText(g.materia);
            b.tvFechaGrupoDocente.setText("Creado el " + g.fechaCreacion);
            b.tvCodigoGrupoDocente.setText("Código de acceso: " + g.codigoUnion);
            b.tvBadgeAlumnosDocente.setText(g.totalAlumnos + " alumnos");
        }
    }

    private static String extraerIniciales(String nombre) {
        String[] palabras = nombre.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(2, palabras.length); i++) {
            if (!palabras[i].isEmpty()) sb.append(palabras[i].charAt(0));
        }
        return sb.toString().toUpperCase();
    }
}

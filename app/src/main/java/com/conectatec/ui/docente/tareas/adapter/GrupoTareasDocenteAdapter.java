package com.conectatec.ui.docente.tareas.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.databinding.ItemGrupoDocenteBinding;

import java.util.ArrayList;
import java.util.List;

public class GrupoTareasDocenteAdapter
        extends RecyclerView.Adapter<GrupoTareasDocenteAdapter.ViewHolder> {

    public static class GrupoDummyTareas {
        public final int id;
        public final String nombre;
        public final String materia;
        public final int totalAlumnos;
        public final String fechaCreacion;
        public final boolean activo;

        public GrupoDummyTareas(int id, String nombre, String materia,
                                int totalAlumnos, String fechaCreacion, boolean activo) {
            this.id = id;
            this.nombre = nombre;
            this.materia = materia;
            this.totalAlumnos = totalAlumnos;
            this.fechaCreacion = fechaCreacion;
            this.activo = activo;
        }
    }

    public interface OnGrupoClickListener {
        void onClick(GrupoDummyTareas grupo);
    }

    private final List<GrupoDummyTareas> lista = new ArrayList<>();
    private OnGrupoClickListener listener;

    public GrupoTareasDocenteAdapter() {
        cargarDatosDummy();
    }

    public void setOnClickListener(OnGrupoClickListener l) {
        this.listener = l;
    }

    public int conteo() {
        return lista.size();
    }

    private void cargarDatosDummy() {
        lista.clear();
        lista.add(new GrupoDummyTareas(1,
                "Programación Móvil 6A", "Programación Móvil",
                18, "01/02/2026", true));
        lista.add(new GrupoDummyTareas(2,
                "Bases de Datos 4B", "Bases de Datos",
                16, "15/01/2026", true));
        lista.add(new GrupoDummyTareas(3,
                "Cálculo Integral 2A", "Cálculo Integral",
                13, "20/01/2026", true));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemGrupoDocenteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        GrupoDummyTareas g = lista.get(position);
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

        void bind(GrupoDummyTareas g) {
            b.tvNombreGrupoDocente.setText(g.nombre);
            b.tvMateriaGrupoDocente.setText(g.materia);
            b.tvBadgeAlumnosDocente.setText(String.valueOf(g.totalAlumnos));
            b.tvFechaGrupoDocente.setText(g.fechaCreacion);
        }
    }
}

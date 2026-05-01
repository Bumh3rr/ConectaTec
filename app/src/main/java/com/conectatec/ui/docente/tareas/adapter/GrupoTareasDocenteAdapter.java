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
        public final String codigoUnion;
        public final boolean activo;

        public GrupoDummyTareas(int id, String nombre, String materia,
                                int totalAlumnos, String fechaCreacion,
                                String codigoUnion, boolean activo) {
            this.id = id;
            this.nombre = nombre;
            this.materia = materia;
            this.totalAlumnos = totalAlumnos;
            this.fechaCreacion = fechaCreacion;
            this.codigoUnion = codigoUnion;
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
                18, "01/02/2026", "TC-9X4P", true));
        lista.add(new GrupoDummyTareas(2,
                "Bases de Datos 4B", "Bases de Datos",
                16, "15/01/2026", "TC-K3M2", true));
        lista.add(new GrupoDummyTareas(3,
                "Cálculo Integral 2A", "Cálculo Integral",
                13, "20/01/2026", "TC-7Z8R", true));
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

package com.conectatec.ui.docente.grupos.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.databinding.ItemGrupoDocenteBinding;

import java.util.ArrayList;
import java.util.List;

public class GrupoDocenteAdapter
        extends RecyclerView.Adapter<GrupoDocenteAdapter.ViewHolder> {

    // ── Modelo dummy ──────────────────────────────────────────────
    public static class GrupoDummyDocente {
        public final int id;
        public final String nombre;
        public final String materia;
        public final int totalAlumnos;
        public final String fechaCreacion;
        public final String codigoUnion;
        public final boolean activo;

        public GrupoDummyDocente(int id, String nombre, String materia,
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

    public static List<GrupoDummyDocente> cargarDatosDummy() {
        List<GrupoDummyDocente> lista = new ArrayList<>();
        lista.add(new GrupoDummyDocente(1, "Programación Móvil 6A", "Programación Móvil",
                18, "01/02/2026", "TC-9X4P", true));
        lista.add(new GrupoDummyDocente(2, "Bases de Datos 4B", "Bases de Datos",
                16, "15/01/2026", "TC-K3M2", true));
        lista.add(new GrupoDummyDocente(3, "Cálculo Integral 2A", "Cálculo Integral",
                13, "20/01/2026", "TC-7Z8R", true));
        return lista;
    }

    // ── Listener ──────────────────────────────────────────────────
    public interface OnGrupoClickListener {
        void onGrupoClick(GrupoDummyDocente grupo);
    }

    // ── Adapter ───────────────────────────────────────────────────
    private final List<GrupoDummyDocente> listaOriginal;
    private List<GrupoDummyDocente> listaFiltrada;
    private final OnGrupoClickListener listener;

    public GrupoDocenteAdapter(List<GrupoDummyDocente> lista, OnGrupoClickListener listener) {
        this.listaOriginal = new ArrayList<>(lista);
        this.listaFiltrada = new ArrayList<>(lista);
        this.listener = listener;
    }

    public void filtrar(String query) {
        listaFiltrada = new ArrayList<>();
        String q = query.toLowerCase().trim();
        for (GrupoDummyDocente g : listaOriginal) {
            if (g.nombre.toLowerCase().contains(q) || g.materia.toLowerCase().contains(q)) {
                listaFiltrada.add(g);
            }
        }
        notifyDataSetChanged();
    }

    public int conteo() {
        return listaFiltrada.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGrupoDocenteBinding b = ItemGrupoDocenteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        GrupoDummyDocente grupo = listaFiltrada.get(position);
        h.binding.tvNombreGrupoDocente.setText(grupo.nombre);
        h.binding.tvMateriaGrupoDocente.setText(grupo.materia);
        h.binding.tvFechaGrupoDocente.setText("Creado: " + grupo.fechaCreacion);
        h.binding.tvBadgeAlumnosDocente.setText(grupo.totalAlumnos + " alumnos");
        h.itemView.setOnClickListener(v -> listener.onGrupoClick(grupo));
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemGrupoDocenteBinding binding;

        ViewHolder(ItemGrupoDocenteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

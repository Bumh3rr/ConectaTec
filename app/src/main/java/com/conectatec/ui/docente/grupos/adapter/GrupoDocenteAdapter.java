package com.conectatec.ui.docente.grupos.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.data.model.Grupo;
import com.conectatec.databinding.ItemGrupoDocenteBinding;

import java.util.ArrayList;
import java.util.List;

public class GrupoDocenteAdapter
        extends RecyclerView.Adapter<GrupoDocenteAdapter.ViewHolder> {

    public interface OnGrupoClickListener {
        void onGrupoClick(Grupo grupo);
    }

    private final List<Grupo> listaOriginal;
    private List<Grupo> listaFiltrada;
    private final OnGrupoClickListener listener;

    public GrupoDocenteAdapter(List<Grupo> lista, OnGrupoClickListener listener) {
        this.listaOriginal = new ArrayList<>(lista);
        this.listaFiltrada = new ArrayList<>(lista);
        this.listener = listener;
    }

    public void setLista(List<Grupo> lista) {
        listaOriginal.clear();
        listaOriginal.addAll(lista);
        listaFiltrada = new ArrayList<>(lista);
        notifyDataSetChanged();
    }

    public void filtrar(String query) {
        listaFiltrada = new ArrayList<>();
        String q = query.toLowerCase().trim();
        for (Grupo g : listaOriginal) {
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
        Grupo grupo = listaFiltrada.get(position);
        h.binding.tvInicialGrupoDocente.setText(extraerIniciales(grupo.nombre));
        h.binding.tvNombreGrupoDocente.setText(grupo.nombre);
        h.binding.tvMateriaGrupoDocente.setText(grupo.materia);
        h.binding.tvFechaGrupoDocente.setText("Creado el " + grupo.fechaCreacion);
        h.binding.tvCodigoGrupoDocente.setText("Código de acceso: " + grupo.codigoUnion);
        h.binding.tvBadgeAlumnosDocente.setText(grupo.totalAlumnos + " alumnos");
        h.itemView.setOnClickListener(v -> listener.onGrupoClick(grupo));
    }

    private static String extraerIniciales(String nombre) {
        String[] palabras = nombre.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(2, palabras.length); i++) {
            if (!palabras[i].isEmpty()) sb.append(palabras[i].charAt(0));
        }
        return sb.toString().toUpperCase();
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

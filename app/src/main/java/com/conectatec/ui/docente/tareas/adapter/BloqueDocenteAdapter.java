package com.conectatec.ui.docente.tareas.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;
import com.conectatec.databinding.ItemBloqueDocenteBinding;

import java.util.ArrayList;
import java.util.List;

public class BloqueDocenteAdapter
        extends RecyclerView.Adapter<BloqueDocenteAdapter.ViewHolder> {

    public static class BloqueDummyDocente {
        public final int id;
        public final int numero;
        public final String nombre;
        public final int totalTareas;

        public BloqueDummyDocente(int id, int numero, String nombre, int totalTareas) {
            this.id = id;
            this.numero = numero;
            this.nombre = nombre;
            this.totalTareas = totalTareas;
        }
    }

    public interface OnBloqueClickListener {
        void onClick(BloqueDummyDocente bloque);
    }

    private final List<BloqueDummyDocente> lista = new ArrayList<>();
    private OnBloqueClickListener listener;

    public BloqueDocenteAdapter() { }

    public void setOnClickListener(OnBloqueClickListener l) {
        this.listener = l;
    }

    public void cargarParaGrupo(int grupoId) {
        lista.clear();
        int t1, t2, t3;
        switch (grupoId) {
            case 1: t1 = 2; t2 = 1; t3 = 1; break;
            case 2: t1 = 1; t2 = 1; t3 = 0; break;
            case 3: t1 = 1; t2 = 1; t3 = 0; break;
            default: t1 = 0; t2 = 0; t3 = 0; break;
        }
        lista.add(new BloqueDummyDocente(1, 1, "Bloque 1", t1));
        lista.add(new BloqueDummyDocente(2, 2, "Bloque 2", t2));
        lista.add(new BloqueDummyDocente(3, 3, "Bloque 3", t3));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemBloqueDocenteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        BloqueDummyDocente b = lista.get(position);
        h.bind(b);
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(b);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemBloqueDocenteBinding b;

        ViewHolder(ItemBloqueDocenteBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        void bind(BloqueDummyDocente bl) {
            b.tvNumeroBloque.setText(String.valueOf(bl.numero));
            b.tvNombreBloque.setText(bl.nombre);
            b.tvTotalTareasBloque.setText(b.getRoot().getContext()
                    .getString(R.string.bloque_sufijo_tareas, bl.totalTareas));
        }
    }
}

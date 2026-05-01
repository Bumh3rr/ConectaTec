package com.conectatec.ui.docente.tareas.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;
import com.conectatec.data.model.Bloque;
import com.conectatec.databinding.ItemBloqueDocenteBinding;

import java.util.ArrayList;
import java.util.List;

public class BloqueDocenteAdapter
        extends RecyclerView.Adapter<BloqueDocenteAdapter.ViewHolder> {

    public interface OnBloqueClickListener {
        void onClick(Bloque bloque);
    }

    private final List<Bloque> lista = new ArrayList<>();
    private OnBloqueClickListener listener;

    public BloqueDocenteAdapter() { }

    public void setOnClickListener(OnBloqueClickListener l) {
        this.listener = l;
    }

    public void setLista(List<Bloque> nuevaLista) {
        lista.clear();
        lista.addAll(nuevaLista);
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
        Bloque b = lista.get(position);
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

        void bind(Bloque bl) {
            b.tvNumeroBloque.setText(String.valueOf(bl.numero));
            b.tvNombreBloque.setText(bl.nombre);
            b.tvTotalTareasBloque.setText(b.getRoot().getContext()
                    .getString(R.string.bloque_sufijo_tareas, bl.totalTareas));
        }
    }
}

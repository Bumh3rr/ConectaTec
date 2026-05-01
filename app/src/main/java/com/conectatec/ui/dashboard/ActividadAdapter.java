package com.conectatec.ui.dashboard;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.databinding.ItemActividadRecienteBinding;
import com.conectatec.model.ActividadItem;

import java.util.ArrayList;
import java.util.List;

public class ActividadAdapter extends RecyclerView.Adapter<ActividadAdapter.ViewHolder> {

    private final List<ActividadItem> lista = new ArrayList<>();

    public void setLista(List<ActividadItem> items) {
        lista.clear();
        lista.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemActividadRecienteBinding b = ItemActividadRecienteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActividadItem item = lista.get(position);
        holder.binding.tvTitulo.setText(item.titulo);
        holder.binding.tvDescripcion.setText(item.descripcion);
        holder.binding.tvTimestamp.setText(item.timestamp);

        String inicial = item.titulo.isEmpty()
                ? "?" : String.valueOf(item.titulo.charAt(0)).toUpperCase();
        holder.binding.tvAvatarInicial.setText(inicial);

        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        circle.setColor(item.colorAvatar);
        holder.binding.tvAvatarInicial.setBackground(circle);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemActividadRecienteBinding binding;

        ViewHolder(ItemActividadRecienteBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}

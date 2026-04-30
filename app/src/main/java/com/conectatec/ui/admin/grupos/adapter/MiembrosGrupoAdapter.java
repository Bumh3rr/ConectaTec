package com.conectatec.ui.admin.grupos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;
import com.conectatec.databinding.ItemMiembroGrupoBinding;

import java.util.ArrayList;
import java.util.List;

public class MiembrosGrupoAdapter extends RecyclerView.Adapter<MiembrosGrupoAdapter.ViewHolder> {

    public static class MiembroDummy {
        public final int id;
        public final String nombre;
        public final String iniciales;
        public final String correo;
        public final String rol;
        public @Nullable Boolean entregado; // null = no mostrar estado de entrega

        public MiembroDummy(int id, String nombre, String iniciales, String correo, String rol) {
            this.id = id;
            this.nombre = nombre;
            this.iniciales = iniciales;
            this.correo = correo;
            this.rol = rol;
        }
    }

    public interface OnMiembroClickListener {
        void onClick(MiembroDummy miembro);
    }

    private final List<MiembroDummy> lista = new ArrayList<>();
    private @Nullable OnMiembroClickListener listener;

    public void setLista(List<MiembroDummy> miembros) {
        lista.clear();
        lista.addAll(miembros);
        notifyDataSetChanged();
    }

    public void setOnClickListener(@Nullable OnMiembroClickListener l) {
        this.listener = l;
    }

    public int conteo() { return lista.size(); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMiembroGrupoBinding b = ItemMiembroGrupoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MiembroDummy m = lista.get(position);
        holder.b.tvInicialesMiembro.setText(m.iniciales);
        holder.b.tvNombreMiembro.setText(m.nombre);
        holder.b.tvCorreoMiembro.setText(m.correo);

        int chipDrawable;
        String chipLabel;
        switch (m.rol) {
            case "DOCENTE":
                chipLabel = "DOCENTE";
                chipDrawable = R.drawable.bg_chip_docente;
                break;
            case "ADMIN":
                chipLabel = "ADMIN";
                chipDrawable = R.drawable.bg_chip_admin;
                break;
            default:
                chipLabel = "ESTUDIANTE";
                chipDrawable = R.drawable.bg_chip_estudiante;
                break;
        }
        holder.b.tvChipRolMiembro.setText(chipLabel);
        holder.b.tvChipRolMiembro.setBackgroundResource(chipDrawable);

        if (m.entregado != null) {
            holder.b.tvEstadoEntregaMiembro.setVisibility(View.VISIBLE);
            holder.b.tvEstadoEntregaMiembro.setText(m.entregado ? "Entregado" : "Pendiente");
            holder.b.tvEstadoEntregaMiembro.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(),
                            m.entregado ? R.color.colorSuccess : R.color.colorWarning));
        } else {
            holder.b.tvEstadoEntregaMiembro.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(m);
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemMiembroGrupoBinding b;
        ViewHolder(ItemMiembroGrupoBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}

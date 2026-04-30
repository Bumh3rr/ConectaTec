package com.conectatec.ui.docente.chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SalaDocenteAdapter extends RecyclerView.Adapter<SalaDocenteAdapter.SalaViewHolder> {

    public interface OnSalaClickListener {
        void onSalaClick(SalaDummyDocente sala);
    }

    public static final class SalaDummyDocente {
        public final int id;
        public final String nombre;
        public final String tipo;           // "GRUPO" | "PRIVADO"
        public final String avatarIniciales;
        public final String ultimoMensaje;
        public final String fechaUltimo;
        public final int noLeidos;

        public SalaDummyDocente(int id, String nombre, String tipo, String avatarIniciales,
                                String ultimoMensaje, String fechaUltimo, int noLeidos) {
            this.id = id;
            this.nombre = nombre;
            this.tipo = tipo;
            this.avatarIniciales = avatarIniciales;
            this.ultimoMensaje = ultimoMensaje;
            this.fechaUltimo = fechaUltimo;
            this.noLeidos = noLeidos;
        }
    }

    private final List<SalaDummyDocente> dataset;
    private final List<SalaDummyDocente> visible;
    private final OnSalaClickListener listener;
    @Nullable private String filtroTipo;

    public SalaDocenteAdapter(OnSalaClickListener listener) {
        this.listener = listener;
        this.dataset = new ArrayList<>(Arrays.asList(
                new SalaDummyDocente(1, "Programación Móvil 6A", "GRUPO",   "PM", "Recuerden la entrega del viernes", "10:32", 3),
                new SalaDummyDocente(2, "Bases de Datos 4B",     "GRUPO",   "BD", "¿Hay duda sobre el ER?",           "Ayer",  0),
                new SalaDummyDocente(3, "Cálculo Integral 2A",   "GRUPO",   "CI", "Práctica resuelta arriba",         "Lun",   1),
                new SalaDummyDocente(4, "Ana López",             "PRIVADO", "AL", "Profe, una consulta…",             "11:05", 2),
                new SalaDummyDocente(5, "Diego Ruiz",            "PRIVADO", "DR", "Gracias por la asesoría",          "Mar",   0)
        ));
        this.visible = new ArrayList<>(dataset);
    }

    public void setFiltroTipo(@Nullable String tipoOrNull) {
        this.filtroTipo = tipoOrNull;
        visible.clear();
        if (tipoOrNull == null) {
            visible.addAll(dataset);
        } else {
            for (SalaDummyDocente s : dataset) {
                if (tipoOrNull.equals(s.tipo)) visible.add(s);
            }
        }
        notifyDataSetChanged();
    }

    public int getTotalNoLeidos() {
        int total = 0;
        for (SalaDummyDocente s : dataset) total += s.noLeidos;
        return total;
    }

    @NonNull
    @Override
    public SalaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sala_chat_docente, parent, false);
        return new SalaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SalaViewHolder h, int position) {
        SalaDummyDocente s = visible.get(position);
        h.tvAvatar.setText(s.avatarIniciales);
        h.tvNombre.setText(s.nombre);
        h.tvUltimoMensaje.setText(s.ultimoMensaje);
        h.tvHora.setText(s.fechaUltimo);
        if (s.noLeidos > 0) {
            h.tvBadge.setVisibility(View.VISIBLE);
            h.tvBadge.setText(String.valueOf(s.noLeidos));
        } else {
            h.tvBadge.setVisibility(View.GONE);
        }
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onSalaClick(s);
        });
    }

    @Override
    public int getItemCount() {
        return visible.size();
    }

    static class SalaViewHolder extends RecyclerView.ViewHolder {
        final TextView tvAvatar;
        final TextView tvNombre;
        final TextView tvUltimoMensaje;
        final TextView tvHora;
        final TextView tvBadge;

        SalaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAvatar        = itemView.findViewById(R.id.tvAvatarSalaChat);
            tvNombre        = itemView.findViewById(R.id.tvNombreSalaChat);
            tvUltimoMensaje = itemView.findViewById(R.id.tvUltimoMensajeSalaChat);
            tvHora          = itemView.findViewById(R.id.tvHoraSalaChat);
            tvBadge         = itemView.findViewById(R.id.tvBadgeNoLeidosSalaChat);
        }
    }
}

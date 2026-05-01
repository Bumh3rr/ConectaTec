package com.conectatec.ui.docente.chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;
import com.conectatec.data.model.Mensaje;

import java.util.ArrayList;
import java.util.List;

public class MensajeDocenteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_RECIBIDO = 0;
    public static final int VIEW_TYPE_ENVIADO  = 1;

    private final List<Mensaje> mensajes;

    public MensajeDocenteAdapter(List<Mensaje> mensajes) {
        this.mensajes = (mensajes != null) ? mensajes : new ArrayList<>();
    }

    public void agregarMensaje(Mensaje m) {
        mensajes.add(m);
        notifyItemInserted(mensajes.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        return mensajes.get(position).esMio ? VIEW_TYPE_ENVIADO : VIEW_TYPE_RECIBIDO;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_ENVIADO) {
            View v = inf.inflate(R.layout.item_mensaje_enviado, parent, false);
            return new EnviadoViewHolder(v);
        } else {
            View v = inf.inflate(R.layout.item_mensaje_recibido, parent, false);
            return new RecibidoViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Mensaje m = mensajes.get(position);
        if (holder instanceof EnviadoViewHolder) {
            EnviadoViewHolder h = (EnviadoViewHolder) holder;
            h.tvTexto.setText(m.texto);
            h.tvHora.setText(m.hora);
        } else if (holder instanceof RecibidoViewHolder) {
            RecibidoViewHolder h = (RecibidoViewHolder) holder;
            h.tvAvatar.setText(m.autorIniciales);
            h.tvAutor.setText(m.autorNombre);
            h.tvTexto.setText(m.texto);
            h.tvHora.setText(m.hora);
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    static class EnviadoViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTexto;
        final TextView tvHora;
        EnviadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTexto = itemView.findViewById(R.id.tvTextoEnviado);
            tvHora  = itemView.findViewById(R.id.tvHoraEnviado);
        }
    }

    static class RecibidoViewHolder extends RecyclerView.ViewHolder {
        final TextView tvAvatar;
        final TextView tvAutor;
        final TextView tvTexto;
        final TextView tvHora;
        RecibidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAvatar = itemView.findViewById(R.id.tvAvatarRecibido);
            tvAutor  = itemView.findViewById(R.id.tvAutorRecibido);
            tvTexto  = itemView.findViewById(R.id.tvTextoRecibido);
            tvHora   = itemView.findViewById(R.id.tvHoraRecibido);
        }
    }
}

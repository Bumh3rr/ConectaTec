package com.conectatec.ui.docente.grupos.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.data.model.Miembro;
import com.conectatec.databinding.ItemMiembroGrupoDocenteBinding;

import java.util.ArrayList;
import java.util.List;

public class MiembroGrupoDocenteAdapter
        extends RecyclerView.Adapter<MiembroGrupoDocenteAdapter.ViewHolder> {

    public interface OnMiembroActionListener {
        void onVerPerfil(Miembro m);
        void onMensaje(Miembro m);
    }

    private List<Miembro> lista = new ArrayList<>();
    private OnMiembroActionListener listener;

    public void setLista(List<Miembro> nuevaLista) {
        this.lista = new ArrayList<>(nuevaLista);
        notifyDataSetChanged();
    }

    public void setListener(OnMiembroActionListener l) {
        this.listener = l;
    }

    public int conteo() {
        return lista.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMiembroGrupoDocenteBinding b = ItemMiembroGrupoDocenteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Miembro m = lista.get(position);
        h.binding.tvInicialesMiembroDocente.setText(m.iniciales);
        h.binding.tvNombreMiembroDocente.setText(m.nombre);
        h.binding.tvCorreoMiembroDocente.setText(m.correo);
        h.binding.tvMatriculaMiembroDocente.setText(m.matricula);

        h.binding.btnVerPerfilMiembro.setOnClickListener(v -> {
            if (listener != null) listener.onVerPerfil(m);
        });
        h.binding.btnMensajeMiembro.setOnClickListener(v -> {
            if (listener != null) listener.onMensaje(m);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemMiembroGrupoDocenteBinding binding;

        ViewHolder(ItemMiembroGrupoDocenteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

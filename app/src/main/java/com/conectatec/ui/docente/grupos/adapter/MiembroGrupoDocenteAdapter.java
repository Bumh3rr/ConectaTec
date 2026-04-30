package com.conectatec.ui.docente.grupos.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.databinding.ItemMiembroGrupoDocenteBinding;

import java.util.ArrayList;
import java.util.List;

public class MiembroGrupoDocenteAdapter
        extends RecyclerView.Adapter<MiembroGrupoDocenteAdapter.ViewHolder> {

    public static class MiembroDummyDocente {
        public final int id;
        public final String nombre;
        public final String iniciales;
        public final String correo;
        public final String matricula;

        public MiembroDummyDocente(int id, String nombre, String iniciales,
                                   String correo, String matricula) {
            this.id = id;
            this.nombre = nombre;
            this.iniciales = iniciales;
            this.correo = correo;
            this.matricula = matricula;
        }
    }

    private List<MiembroDummyDocente> lista = new ArrayList<>();

    public void setLista(List<MiembroDummyDocente> nuevaLista) {
        this.lista = new ArrayList<>(nuevaLista);
        notifyDataSetChanged();
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
        MiembroDummyDocente m = lista.get(position);
        h.binding.tvInicialesMiembroDocente.setText(m.iniciales);
        h.binding.tvNombreMiembroDocente.setText(m.nombre);
        h.binding.tvCorreoMiembroDocente.setText(m.correo);
        h.binding.tvMatriculaMiembroDocente.setText(m.matricula);
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

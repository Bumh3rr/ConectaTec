package com.conectatec.ui.admin.usuarios.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.databinding.ItemUsuarioPendienteBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter para la lista de usuarios con rol PENDIENTE.
 * Cada ítem ofrece dos botones para asignar Docente o Estudiante.
 */
public class UsuarioPendienteAdapter extends RecyclerView.Adapter<UsuarioPendienteAdapter.ViewHolder> {

    public interface OnPendienteAccionListener {
        void onAsignarDocente(UsuarioAdminAdapter.UsuarioDummy usuario, int position);
        void onAsignarEstudiante(UsuarioAdminAdapter.UsuarioDummy usuario, int position);
    }

    private List<UsuarioAdminAdapter.UsuarioDummy> lista = new ArrayList<>();
    private OnPendienteAccionListener listener;

    public UsuarioPendienteAdapter(OnPendienteAccionListener listener) {
        this.listener = listener;
        cargarDatosDummy();
    }

    /** 3 usuarios pendientes de ejemplo. */
    public void cargarDatosDummy() {
        lista.clear();
        lista.add(new UsuarioAdminAdapter.UsuarioDummy("Roberto Sánchez",   "roberto.sanchez@tec.mx",   "PENDIENTE", true));
        lista.add(new UsuarioAdminAdapter.UsuarioDummy("Jorge Hernández",   "jorge.hernandez@tec.mx",   "PENDIENTE", true));
        lista.add(new UsuarioAdminAdapter.UsuarioDummy("Valentina Cruz",    "valentina.cruz@tec.mx",    "PENDIENTE", true));
        notifyDataSetChanged();
    }

    public void setLista(List<UsuarioAdminAdapter.UsuarioDummy> nuevaLista) {
        lista = nuevaLista;
        notifyDataSetChanged();
    }

    public int conteo() { return lista.size(); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUsuarioPendienteBinding b = ItemUsuarioPendienteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsuarioAdminAdapter.UsuarioDummy u = lista.get(position);

        holder.b.tvNombrePendiente.setText(u.nombre);
        holder.b.tvCorreoPendiente.setText(u.correo);

        // Mostrar iniciales como placeholder
        holder.b.ivAvatarPendiente.setVisibility(View.GONE);
        holder.b.tvInicialesPendiente.setVisibility(View.VISIBLE);
        holder.b.tvInicialesPendiente.setText(u.iniciales());

        holder.b.btnAsignarDocente.setOnClickListener(v -> {
            if (listener != null) listener.onAsignarDocente(u, holder.getAdapterPosition());
        });
        holder.b.btnAsignarEstudiante.setOnClickListener(v -> {
            if (listener != null) listener.onAsignarEstudiante(u, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemUsuarioPendienteBinding b;
        ViewHolder(ItemUsuarioPendienteBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}

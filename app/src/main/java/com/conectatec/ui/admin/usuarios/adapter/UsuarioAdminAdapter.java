package com.conectatec.ui.admin.usuarios.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;
import com.conectatec.databinding.ItemUsuarioAdminBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter para la lista general de usuarios del Administrador.
 * Muestra avatar (iniciales si no hay foto), nombre, correo y chip de rol.
 */
public class UsuarioAdminAdapter extends RecyclerView.Adapter<UsuarioAdminAdapter.ViewHolder> {

    /** Modelo de datos dummy para un usuario. */
    public static class UsuarioDummy {
        public final String nombre;
        public final String correo;
        public final String rol;      // "PENDIENTE", "DOCENTE", "ESTUDIANTE", "ADMINISTRADOR"
        public final boolean activo;

        public UsuarioDummy(String nombre, String correo, String rol, boolean activo) {
            this.nombre = nombre;
            this.correo = correo;
            this.rol = rol;
            this.activo = activo;
        }

        /** Iniciales para el avatar placeholder (máx. 2 letras). */
        public String iniciales() {
            String[] partes = nombre.trim().split(" ");
            if (partes.length >= 2) {
                return String.valueOf(partes[0].charAt(0)).toUpperCase()
                        + String.valueOf(partes[1].charAt(0)).toUpperCase();
            }
            return nombre.length() >= 2 ? nombre.substring(0, 2).toUpperCase() : nombre.toUpperCase();
        }
    }

    public interface OnUsuarioClickListener {
        void onClick(UsuarioDummy usuario, int position);
        void onOverflowClick(View anchor, UsuarioDummy usuario, int position);
    }

    public List<UsuarioDummy> lista = new ArrayList<>();
    private OnUsuarioClickListener listener;

    public UsuarioAdminAdapter(OnUsuarioClickListener listener) {
        this.listener = listener;
        cargarDatosDummy();
    }

    /** Rellena la lista con 7 usuarios dummy para visualizar el diseño en el emulador. */
    public void cargarDatosDummy() {
        lista.clear();
        lista.add(new UsuarioDummy("Carlos Bautista",      "carlos.bautista@tec.mx",    "DOCENTE",       true));
        lista.add(new UsuarioDummy("María González",        "maria.gonzalez@tec.mx",     "ESTUDIANTE",    true));
        lista.add(new UsuarioDummy("Roberto Sánchez",       "roberto.sanchez@tec.mx",    "PENDIENTE",     true));
        lista.add(new UsuarioDummy("Ana Laura Méndez",      "ana.mendez@tec.mx",         "ESTUDIANTE",    true));
        lista.add(new UsuarioDummy("Jorge Hernández",       "jorge.hernandez@tec.mx",    "PENDIENTE",     true));
        lista.add(new UsuarioDummy("Sofía Torres",          "sofia.torres@tec.mx",       "DOCENTE",       false));
        lista.add(new UsuarioDummy("Luis Miguel Ramírez",   "luis.ramirez@tec.mx",       "ADMINISTRADOR", true));
        notifyDataSetChanged();
    }

    public void setLista(List<UsuarioDummy> nuevaLista) {
        lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUsuarioAdminBinding b = ItemUsuarioAdminBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsuarioDummy u = lista.get(position);

        holder.b.tvNombreUsuario.setText(u.nombre);
        holder.b.tvCorreoUsuario.setText(u.correo);

        // Mostrar iniciales como placeholder de avatar
        holder.b.ivAvatar.setVisibility(View.GONE);
        holder.b.tvIniciales.setVisibility(View.VISIBLE);
        holder.b.tvIniciales.setText(u.iniciales());

        // Chip de rol con color correspondiente
        holder.b.tvChipRol.setText(u.rol);
        holder.b.tvChipRol.setBackgroundResource(chipDrawableForRol(u.rol));

        // Indicador de cuenta desactivada
        holder.b.tvDesactivado.setVisibility(u.activo ? View.GONE : View.VISIBLE);
        holder.itemView.setAlpha(u.activo ? 1f : 0.5f);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(u, holder.getAdapterPosition());
        });
        holder.b.ivOverflow.setOnClickListener(v -> {
            if (listener != null) listener.onOverflowClick(v, u, holder.getAdapterPosition());
        });
    }

    private int chipDrawableForRol(String rol) {
        switch (rol) {
            case "DOCENTE":       return R.drawable.bg_chip_docente;
            case "ESTUDIANTE":    return R.drawable.bg_chip_estudiante;
            case "ADMINISTRADOR": return R.drawable.bg_chip_admin;
            default:              return R.drawable.bg_chip_pendiente;
        }
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemUsuarioAdminBinding b;
        ViewHolder(ItemUsuarioAdminBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}

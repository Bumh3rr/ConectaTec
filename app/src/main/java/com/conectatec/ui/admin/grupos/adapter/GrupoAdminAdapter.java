package com.conectatec.ui.admin.grupos.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;
import com.conectatec.databinding.ItemGrupoAdminBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter para la lista de grupos del Administrador con datos dummy.
 */
public class GrupoAdminAdapter extends RecyclerView.Adapter<GrupoAdminAdapter.ViewHolder> {

    /** Modelo dummy de un grupo. */
    public static class GrupoDummy {
        public final int id;
        public final String nombre;
        public final String materia;
        public final String docente;
        public final int miembros;
        public final String fechaCreacion;
        public final boolean activo;

        public GrupoDummy(int id, String nombre, String materia, String docente,
                          int miembros, String fechaCreacion, boolean activo) {
            this.id = id;
            this.nombre = nombre;
            this.materia = materia;
            this.docente = docente;
            this.miembros = miembros;
            this.fechaCreacion = fechaCreacion;
            this.activo = activo;
        }
    }

    public interface OnGrupoClickListener {
        void onClick(GrupoDummy grupo, int position);
    }

    private List<GrupoDummy> lista = new ArrayList<>();
    private final List<GrupoDummy> listaCompleta = new ArrayList<>();
    private OnGrupoClickListener listener;

    public GrupoAdminAdapter(OnGrupoClickListener listener) {
        this.listener = listener;
        cargarDatosDummy();
    }

    /** 6 grupos dummy para visualizar el diseño en el emulador. */
    public void cargarDatosDummy() {
        lista.clear();
        listaCompleta.clear();
        lista.add(new GrupoDummy(1, "Ingeniería de Software – 6A",  "Ing. de Software",   "Prof. Carlos Bautista",   28, "01/02/2025", true));
        lista.add(new GrupoDummy(2, "Bases de Datos – 4B",          "Bases de Datos",      "Prof. Carlos Bautista",   22, "15/01/2025", true));
        lista.add(new GrupoDummy(3, "Redes de Computadoras – 5C",   "Redes",               "Prof. Sofía Torres",      18, "20/01/2025", true));
        lista.add(new GrupoDummy(4, "Cálculo Integral – 2A",        "Cálculo",             "Prof. Luis Ramírez",      35, "10/02/2025", true));
        lista.add(new GrupoDummy(5, "Programación Orientada – 3D",  "Programación OO",     "Prof. Carlos Bautista",   30, "05/02/2025", false));
        lista.add(new GrupoDummy(6, "Sistemas Operativos – 5A",     "Sistemas Operativos", "Prof. Sofía Torres",      25, "12/02/2025", true));
        listaCompleta.addAll(lista);
        notifyDataSetChanged();
    }

    public void filtrarPorActivo(@Nullable Boolean activo) {
        lista.clear();
        for (GrupoDummy g : listaCompleta) {
            if (activo == null || g.activo == activo) lista.add(g);
        }
        notifyDataSetChanged();
    }

    /** Aplica simultáneamente los tres filtros (docente, activo, texto). Pasa null/"" para no filtrar cada uno. */
    public void setFiltros(@Nullable String docente, @Nullable Boolean activo, @NonNull String query) {
        lista.clear();
        String q = query.toLowerCase(Locale.getDefault()).trim();
        for (GrupoDummy g : listaCompleta) {
            boolean okDocente = docente == null || docente.isEmpty()
                    || g.docente.toLowerCase(Locale.getDefault()).contains(
                            docente.toLowerCase(Locale.getDefault()));
            boolean okActivo = activo == null || g.activo == activo;
            boolean okQuery  = q.isEmpty()
                    || g.nombre.toLowerCase(Locale.getDefault()).contains(q)
                    || g.materia.toLowerCase(Locale.getDefault()).contains(q)
                    || g.docente.toLowerCase(Locale.getDefault()).contains(q);
            if (okDocente && okActivo && okQuery) lista.add(g);
        }
        notifyDataSetChanged();
    }

    public void setLista(List<GrupoDummy> nuevaLista) {
        lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGrupoAdminBinding b = ItemGrupoAdminBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GrupoDummy g = lista.get(position);

        holder.b.tvNombreGrupo.setText(g.nombre);
        holder.b.tvMateriaGrupo.setText(g.materia);
        holder.b.tvDocenteGrupo.setText(g.docente);
        holder.b.tvMiembrosGrupo.setText(g.miembros + " miembros");
        holder.b.tvFechaCreacionGrupo.setText("Creado el " + g.fechaCreacion);

        // Badge activo / inactivo
        if (g.activo) {
            holder.b.tvEstadoGrupo.setText("ACTIVO");
            holder.b.tvEstadoGrupo.setBackgroundResource(R.drawable.bg_chip_estudiante);
        } else {
            holder.b.tvEstadoGrupo.setText("INACTIVO");
            holder.b.tvEstadoGrupo.setBackgroundResource(R.drawable.bg_chip_pendiente);
        }
        holder.itemView.setAlpha(g.activo ? 1f : 0.6f);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(g, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemGrupoAdminBinding b;
        ViewHolder(ItemGrupoAdminBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}

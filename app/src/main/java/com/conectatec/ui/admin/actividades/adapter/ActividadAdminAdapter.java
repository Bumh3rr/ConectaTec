package com.conectatec.ui.admin.actividades.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;
import com.conectatec.databinding.ItemActividadAdminBinding;

import java.util.ArrayList;
import java.util.List;

public class ActividadAdminAdapter
        extends RecyclerView.Adapter<ActividadAdminAdapter.ViewHolder> {

    // ── Modelo ────────────────────────────────────────────────────────────────

    public static class ActividadDummy {
        public static final String EN_CURSO    = "EN_CURSO";
        public static final String PENDIENTE   = "PENDIENTE";
        public static final String COMPLETADA  = "COMPLETADA";
        public static final String VENCIDA     = "VENCIDA";

        public final int    id;
        public final String titulo;
        public final String grupo;
        public final String docente;
        public final String fechaVencimiento;
        public final String estado;
        public final int    entregasRecibidas;
        public final int    totalMiembros;

        public ActividadDummy(int id, String titulo, String grupo, String docente,
                              String fechaVencimiento, String estado,
                              int entregasRecibidas, int totalMiembros) {
            this.id                = id;
            this.titulo            = titulo;
            this.grupo             = grupo;
            this.docente           = docente;
            this.fechaVencimiento  = fechaVencimiento;
            this.estado            = estado;
            this.entregasRecibidas = entregasRecibidas;
            this.totalMiembros     = totalMiembros;
        }
    }

    // ── Datos dummy ───────────────────────────────────────────────────────────

    public final List<ActividadDummy> listaCompleta = new ArrayList<>();
    private final List<ActividadDummy> listaFiltrada = new ArrayList<>();

    private String filtroEstado = null;
    private String filtroTexto  = "";

    public ActividadAdminAdapter() {
        listaCompleta.add(new ActividadDummy(1,
                "Proyecto Final de Semestre",
                "Ingeniería de Software – 6A",
                "Prof. Carlos Bautista",
                "30/05/2025", ActividadDummy.EN_CURSO, 18, 24));
        listaCompleta.add(new ActividadDummy(2,
                "Parcial 2 – Bases de Datos",
                "Bases de Datos – 4B",
                "Prof. Ana Ramírez",
                "15/04/2025", ActividadDummy.COMPLETADA, 22, 22));
        listaCompleta.add(new ActividadDummy(3,
                "Práctica de Laboratorio #3",
                "Redes de Computadoras – 3A",
                "Prof. Luis Torres",
                "05/05/2025", ActividadDummy.EN_CURSO, 8, 20));
        listaCompleta.add(new ActividadDummy(4,
                "Ensayo de Ética Profesional",
                "Humanidades – 5C",
                "Prof. Marta García",
                "20/05/2025", ActividadDummy.PENDIENTE, 0, 18));
        listaCompleta.add(new ActividadDummy(5,
                "Examen Final Cálculo II",
                "Cálculo Diferencial – 2B",
                "Prof. Jorge López",
                "10/04/2025", ActividadDummy.VENCIDA, 15, 25));
        listaCompleta.add(new ActividadDummy(6,
                "Reporte de Investigación",
                "Ingeniería de Software – 6A",
                "Prof. Carlos Bautista",
                "25/05/2025", ActividadDummy.EN_CURSO, 12, 24));
        listaCompleta.add(new ActividadDummy(7,
                "Taller de Programación OO",
                "POO – 1A",
                "Prof. Sandra Martínez",
                "20/03/2025", ActividadDummy.COMPLETADA, 30, 30));
        listaCompleta.add(new ActividadDummy(8,
                "Diseño de Arquitectura SW",
                "Ingeniería de Software – 6A",
                "Prof. Carlos Bautista",
                "15/06/2025", ActividadDummy.PENDIENTE, 0, 24));

        listaFiltrada.addAll(listaCompleta);
    }

    // ── Filtrado ──────────────────────────────────────────────────────────────

    public void filtrar(@Nullable String estado, @NonNull String query) {
        filtroEstado = estado;
        filtroTexto  = query;
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        listaFiltrada.clear();
        String q = filtroTexto.trim().toLowerCase();
        for (ActividadDummy a : listaCompleta) {
            boolean matchEstado = (filtroEstado == null) || filtroEstado.equals(a.estado);
            boolean matchQuery  = q.isEmpty()
                    || a.titulo.toLowerCase().contains(q)
                    || a.grupo.toLowerCase().contains(q)
                    || a.docente.toLowerCase().contains(q);
            if (matchEstado && matchQuery) listaFiltrada.add(a);
        }
        notifyDataSetChanged();
    }

    public int conteo() { return listaFiltrada.size(); }

    // ── Click listener ────────────────────────────────────────────────────────

    public interface OnActividadClickListener {
        void onClick(ActividadDummy actividad);
    }

    private OnActividadClickListener clickListener;

    public void setOnClickListener(OnActividadClickListener l) {
        clickListener = l;
    }

    // ── RecyclerView.Adapter ─────────────────────────────────────────────────

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemActividadAdminBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        ActividadDummy a = listaFiltrada.get(position);
        h.bind(a);
        h.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onClick(a);
        });
    }

    @Override
    public int getItemCount() { return listaFiltrada.size(); }

    // ── ViewHolder ────────────────────────────────────────────────────────────

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemActividadAdminBinding b;

        ViewHolder(ItemActividadAdminBinding binding) {
            super(binding.getRoot());
            b = binding;
        }

        void bind(ActividadDummy a) {
            b.tvTituloActividad.setText(a.titulo);
            b.tvGrupoActividad.setText(a.grupo);
            b.tvDocenteActividad.setText(a.docente);
            b.tvFechaVenceActividad.setText("Vence: " + a.fechaVencimiento);
            b.tvEntregasActividad.setText(a.entregasRecibidas + " / " + a.totalMiembros + " entregas");

            aplicarEstilo(a.estado);
        }

        private void aplicarEstilo(String estado) {
            int accentColor;
            int chipDrawable;
            String chipLabel;

            switch (estado) {
                case ActividadDummy.EN_CURSO:
                    accentColor  = R.color.colorPrimary;
                    chipDrawable = R.drawable.bg_chip_docente;
                    chipLabel    = "EN CURSO";
                    break;
                case ActividadDummy.COMPLETADA:
                    accentColor  = R.color.colorSuccess;
                    chipDrawable = R.drawable.bg_chip_estudiante;
                    chipLabel    = "COMPLETADA";
                    break;
                case ActividadDummy.VENCIDA:
                    accentColor  = R.color.colorError;
                    chipDrawable = R.drawable.bg_chip_admin;
                    chipLabel    = "VENCIDA";
                    break;
                default: // PENDIENTE
                    accentColor  = R.color.colorWarning;
                    chipDrawable = R.drawable.bg_chip_pendiente;
                    chipLabel    = "PENDIENTE";
                    break;
            }

            b.viewStatusAccent.setBackgroundColor(
                    ContextCompat.getColor(b.getRoot().getContext(), accentColor));
            b.tvChipEstadoActividad.setBackgroundResource(chipDrawable);
            b.tvChipEstadoActividad.setText(chipLabel);
        }
    }
}

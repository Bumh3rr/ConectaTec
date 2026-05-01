package com.conectatec.ui.docente.tareas.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;
import com.conectatec.databinding.ItemTareaDocenteBinding;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class TareaDocenteAdapter
        extends RecyclerView.Adapter<TareaDocenteAdapter.ViewHolder> {

    public static final String TIPO_TAREA    = "TAREA";
    public static final String TIPO_TRABAJO  = "TRABAJO";
    public static final String TIPO_EXAMEN   = "EXAMEN";
    public static final String TIPO_PROYECTO = "PROYECTO";

    public static final String EST_EN_CURSO   = "EN_CURSO";
    public static final String EST_VENCIDA    = "VENCIDA";
    public static final String EST_COMPLETADA = "COMPLETADA";

    public static class TareaDummyDocente {
        public final int id;
        public final String titulo;
        public final String tipo;
        public final String estado;
        public final int grupoId;
        public final int bloqueId;
        public final String fechaPublicacion;
        public final String fechaVence;
        public final int totalAlumnos;
        public final int entregadas;

        public TareaDummyDocente(int id, String titulo, String tipo, String estado,
                                 int grupoId, int bloqueId, String fechaPublicacion,
                                 String fechaVence, int totalAlumnos, int entregadas) {
            this.id = id;
            this.titulo = titulo;
            this.tipo = tipo;
            this.estado = estado;
            this.grupoId = grupoId;
            this.bloqueId = bloqueId;
            this.fechaPublicacion = fechaPublicacion;
            this.fechaVence = fechaVence;
            this.totalAlumnos = totalAlumnos;
            this.entregadas = entregadas;
        }
    }

    public static final List<TareaDummyDocente> DATASET = new ArrayList<>();
    static {
        DATASET.add(new TareaDummyDocente(1, "Práctica 1: Layouts",TIPO_TAREA, EST_EN_CURSO, 1, 1, "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new TareaDummyDocente(1, "Práctica 1: Layouts",TIPO_TAREA, EST_EN_CURSO, 1, 1, "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new TareaDummyDocente(1, "Práctica 1: Layouts",TIPO_TAREA, EST_EN_CURSO, 1, 1, "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new TareaDummyDocente(1, "Práctica 1: Layouts",TIPO_TAREA, EST_EN_CURSO, 1, 1, "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new TareaDummyDocente(1, "Práctica 1: Layouts",TIPO_TAREA, EST_EN_CURSO, 1, 1, "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new TareaDummyDocente(1, "Práctica 1: Layouts",TIPO_TAREA, EST_EN_CURSO, 1, 1, "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new TareaDummyDocente(1, "Práctica 1: Layouts",TIPO_TAREA, EST_EN_CURSO, 1, 1, "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new TareaDummyDocente(1, "Práctica 1: Layouts",TIPO_TAREA, EST_EN_CURSO, 1, 1, "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new TareaDummyDocente(1, "Práctica 1: Layouts",TIPO_TAREA, EST_EN_CURSO, 1, 1, "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new TareaDummyDocente(1, "Práctica 1: Layouts",TIPO_TAREA, EST_EN_CURSO, 1, 1, "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new TareaDummyDocente(1, "Práctica 1: Layouts",TIPO_TAREA, EST_EN_CURSO, 1, 1, "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new TareaDummyDocente(1, "Práctica 1: Layouts",TIPO_TAREA, EST_EN_CURSO, 1, 1, "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new TareaDummyDocente(1, "Práctica 1: Layouts",TIPO_TAREA, EST_EN_CURSO, 1, 1, "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new TareaDummyDocente(1, "Práctica 1: Layouts",TIPO_TAREA, EST_EN_CURSO, 1, 1, "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new TareaDummyDocente(2, "Examen Parcial 1",
                TIPO_EXAMEN, EST_COMPLETADA, 1, 1, "15/02/2026", "28/02/2026", 18, 18));
        DATASET.add(new TareaDummyDocente(3, "Proyecto Final",
                TIPO_PROYECTO, EST_EN_CURSO, 1, 2, "01/03/2026", "30/05/2026", 18, 5));
        DATASET.add(new TareaDummyDocente(4, "Trabajo de investigación",
                TIPO_TRABAJO, EST_EN_CURSO, 1, 3, "10/04/2026", "25/05/2026", 18, 8));
        DATASET.add(new TareaDummyDocente(5, "Tarea 1: Modelo ER",
                TIPO_TAREA, EST_COMPLETADA, 2, 1, "20/01/2026", "05/02/2026", 16, 16));
        DATASET.add(new TareaDummyDocente(6, "Examen parcial 2",
                TIPO_EXAMEN, EST_EN_CURSO, 2, 2, "15/03/2026", "12/05/2026", 16, 0));
        DATASET.add(new TareaDummyDocente(7, "Tarea 5: Integrales",
                TIPO_TAREA, EST_VENCIDA, 3, 1, "01/02/2026", "20/04/2026", 13, 9));
        DATASET.add(new TareaDummyDocente(8, "Tarea 6: Series",
                TIPO_TAREA, EST_EN_CURSO, 3, 2, "10/04/2026", "08/05/2026", 13, 4));
    }

    public static TareaDummyDocente buscarPorId(int id) {
        for (TareaDummyDocente t : DATASET) {
            if (t.id == id) return t;
        }
        return null;
    }

    public interface OnTareaClickListener {
        void onClick(TareaDummyDocente tarea);
    }

    private final List<TareaDummyDocente> lista        = new ArrayList<>();
    private final List<TareaDummyDocente> listaCompleta = new ArrayList<>();
    private OnTareaClickListener listener;

    private String filtroTipo   = null;
    private String filtroEstado = null;

    public TareaDocenteAdapter() {}

    public void setOnClickListener(OnTareaClickListener l) { this.listener = l; }

    public int conteo() { return lista.size(); }

    public int totalBloque() { return listaCompleta.size(); }

    public int conteoEstado(String estado) {
        int c = 0;
        for (TareaDummyDocente t : listaCompleta) {
            if (estado.equals(t.estado)) c++;
        }
        return c;
    }

    public void cargarParaBloque(int grupoId, int bloqueId) {
        listaCompleta.clear();
        for (TareaDummyDocente t : DATASET) {
            if (t.grupoId == grupoId && t.bloqueId == bloqueId) listaCompleta.add(t);
        }
        aplicarFiltros();
    }

    public void filtrar(@Nullable String tipo, @Nullable String estado) {
        this.filtroTipo   = tipo;
        this.filtroEstado = estado;
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        lista.clear();
        for (TareaDummyDocente t : listaCompleta) {
            boolean okTipo = (filtroTipo == null)   || filtroTipo.equals(t.tipo);
            boolean okEst  = (filtroEstado == null) || filtroEstado.equals(t.estado);
            if (okTipo && okEst) lista.add(t);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemTareaDocenteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        TareaDummyDocente t = lista.get(position);
        h.bind(t);
        h.itemView.setOnClickListener(v -> { if (listener != null) listener.onClick(t); });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTareaDocenteBinding b;

        ViewHolder(ItemTareaDocenteBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        void bind(TareaDummyDocente t) {
            b.tvTituloTarea.setText(t.titulo);
            b.tvFechaVenceTarea.setText("Vence: " + t.fechaVence);
            b.tvEntregasTarea.setText(
                    b.getRoot().getContext().getString(
                            R.string.tareas_sufijo_entregas, t.entregadas, t.totalAlumnos));

            aplicarChipTipo(t.tipo);
            aplicarChipEstado(t.estado);
            aplicarEstadoVisual(t);
        }

        private void aplicarChipTipo(String tipo) {
            int drawable;
            String label;
            switch (tipo) {
                case TIPO_TRABAJO:  drawable = R.drawable.bg_chip_pendiente;  label = "TRABAJO";  break;
                case TIPO_EXAMEN:   drawable = R.drawable.bg_chip_admin;      label = "EXAMEN";   break;
                case TIPO_PROYECTO: drawable = R.drawable.bg_chip_estudiante; label = "PROYECTO"; break;
                default:            drawable = R.drawable.bg_chip_docente;    label = "TAREA";    break;
            }
            b.tvChipTipoTarea.setBackgroundResource(drawable);
            b.tvChipTipoTarea.setText(label);
        }

        private void aplicarChipEstado(String estado) {
            int drawable;
            String label;
            switch (estado) {
                case EST_VENCIDA:    drawable = R.drawable.bg_chip_admin;      label = "VENCIDA";    break;
                case EST_COMPLETADA: drawable = R.drawable.bg_chip_estudiante; label = "COMPLETADA"; break;
                default:             drawable = R.drawable.bg_chip_docente;    label = "EN CURSO";   break;
            }
            b.tvChipEstadoTarea.setBackgroundResource(drawable);
            b.tvChipEstadoTarea.setText(label);
        }

        private void aplicarEstadoVisual(TareaDummyDocente t) {
            int colorRes;
            switch (t.estado) {
                case EST_COMPLETADA: colorRes = R.color.colorChipEstudiante; break;
                case EST_VENCIDA:    colorRes = R.color.colorError;          break;
                default:             colorRes = R.color.colorPrimary;        break;
            }

            int color = ContextCompat.getColor(b.getRoot().getContext(), colorRes);

            // Stroke del card coloreado por estado
            ((MaterialCardView) b.getRoot()).setStrokeColor(ColorStateList.valueOf(color));

            // Progreso de entregas
            int pct = t.totalAlumnos > 0 ? (t.entregadas * 100 / t.totalAlumnos) : 0;
            b.pbEntregasTarea.setProgress(pct);
            b.pbEntregasTarea.setProgressTintList(ColorStateList.valueOf(color));
            b.pbEntregasTarea.setProgressBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(b.getRoot().getContext(), R.color.colorDivider)));
        }
    }
}

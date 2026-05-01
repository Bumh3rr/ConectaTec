package com.conectatec.ui.docente.tareas.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;
import com.conectatec.data.model.Tarea;
import com.conectatec.databinding.ItemTareaDocenteBinding;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class TareaDocenteAdapter
        extends RecyclerView.Adapter<TareaDocenteAdapter.ViewHolder> {

    public interface OnTareaClickListener {
        void onClick(Tarea tarea);
    }

    private final List<Tarea> lista         = new ArrayList<>();
    private final List<Tarea> listaCompleta = new ArrayList<>();
    private OnTareaClickListener listener;

    private String filtroTipo   = null;
    private String filtroEstado = null;

    public TareaDocenteAdapter() {}

    public void setOnClickListener(OnTareaClickListener l) { this.listener = l; }

    public int conteo() { return lista.size(); }

    public int totalBloque() { return listaCompleta.size(); }

    public int conteoEstado(String estado) {
        int c = 0;
        for (Tarea t : listaCompleta) {
            if (estado.equals(t.estado)) c++;
        }
        return c;
    }

    public void setListaCompleta(List<Tarea> tareas) {
        listaCompleta.clear();
        listaCompleta.addAll(tareas);
        aplicarFiltros();
    }

    public void filtrar(@Nullable String tipo, @Nullable String estado) {
        this.filtroTipo   = tipo;
        this.filtroEstado = estado;
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        lista.clear();
        for (Tarea t : listaCompleta) {
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
        Tarea t = lista.get(position);
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

        void bind(Tarea t) {
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
                case Tarea.TIPO_TRABAJO:  drawable = R.drawable.bg_chip_pendiente;  label = "TRABAJO";  break;
                case Tarea.TIPO_EXAMEN:   drawable = R.drawable.bg_chip_admin;      label = "EXAMEN";   break;
                case Tarea.TIPO_PROYECTO: drawable = R.drawable.bg_chip_estudiante; label = "PROYECTO"; break;
                default:                  drawable = R.drawable.bg_chip_docente;    label = "TAREA";    break;
            }
            b.tvChipTipoTarea.setBackgroundResource(drawable);
            b.tvChipTipoTarea.setText(label);
        }

        private void aplicarChipEstado(String estado) {
            int drawable;
            String label;
            switch (estado) {
                case Tarea.EST_VENCIDA:    drawable = R.drawable.bg_chip_admin;      label = "VENCIDA";    break;
                case Tarea.EST_COMPLETADA: drawable = R.drawable.bg_chip_estudiante; label = "COMPLETADA"; break;
                default:                   drawable = R.drawable.bg_chip_docente;    label = "EN CURSO";   break;
            }
            b.tvChipEstadoTarea.setBackgroundResource(drawable);
            b.tvChipEstadoTarea.setText(label);
        }

        private void aplicarEstadoVisual(Tarea t) {
            int colorRes;
            switch (t.estado) {
                case Tarea.EST_COMPLETADA: colorRes = R.color.colorChipEstudiante; break;
                case Tarea.EST_VENCIDA:    colorRes = R.color.colorError;          break;
                default:                   colorRes = R.color.colorPrimary;        break;
            }

            int color = ContextCompat.getColor(b.getRoot().getContext(), colorRes);

            ((MaterialCardView) b.getRoot()).setStrokeColor(ColorStateList.valueOf(color));

            int pct = t.totalAlumnos > 0 ? (t.entregadas * 100 / t.totalAlumnos) : 0;
            b.pbEntregasTarea.setProgress(pct);
            b.pbEntregasTarea.setProgressTintList(ColorStateList.valueOf(color));
            b.pbEntregasTarea.setProgressBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(b.getRoot().getContext(), R.color.colorDivider)));
        }
    }
}

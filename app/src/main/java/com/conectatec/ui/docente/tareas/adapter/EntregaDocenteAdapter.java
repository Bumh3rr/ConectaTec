package com.conectatec.ui.docente.tareas.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;
import com.conectatec.data.model.Entrega;
import com.conectatec.databinding.ItemEntregaDocenteBinding;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class EntregaDocenteAdapter
        extends RecyclerView.Adapter<EntregaDocenteAdapter.ViewHolder> {

    public interface OnEntregaClickListener {
        void onClick(Entrega entrega);
    }

    private final List<Entrega> lista         = new ArrayList<>();
    private final List<Entrega> listaCompleta = new ArrayList<>();
    private OnEntregaClickListener listener;
    private Integer filtroEstado = null;

    public EntregaDocenteAdapter() { }

    public void setOnClickListener(OnEntregaClickListener l) {
        this.listener = l;
    }

    public int conteo() { return lista.size(); }
    public int conteoTotal() { return listaCompleta.size(); }

    public int conteoEntregadas() {
        int c = 0;
        for (Entrega e : listaCompleta) {
            if (e.estado == Entrega.ESTADO_ENTREGADA || e.estado == Entrega.ESTADO_CALIFICADA) c++;
        }
        return c;
    }

    public int conteoPendientes() {
        return conteoTotal() - conteoEntregadas();
    }

    public void setListaCompleta(List<Entrega> entregas) {
        listaCompleta.clear();
        listaCompleta.addAll(entregas);
        aplicarFiltros();
    }

    public void filtrar(@Nullable Integer estado) {
        this.filtroEstado = estado;
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        lista.clear();
        for (Entrega e : listaCompleta) {
            if (filtroEstado == null || filtroEstado == e.estado) {
                lista.add(e);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemEntregaDocenteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Entrega e = lista.get(position);
        h.bind(e);
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(e);
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemEntregaDocenteBinding b;

        ViewHolder(ItemEntregaDocenteBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        void bind(Entrega e) {
            b.tvInicialesEntrega.setText(e.alumnoIniciales);
            b.tvNombreEntrega.setText(e.alumnoNombre);

            int chipDrawable;
            String chipLabel;
            String fechaTxt;
            int strokeColorRes;

            switch (e.estado) {
                case Entrega.ESTADO_ENTREGADA:
                    chipDrawable = R.drawable.bg_chip_estudiante;
                    chipLabel = "ENTREGADA";
                    fechaTxt = "Entregada: " + (e.fechaEntrega != null ? e.fechaEntrega : "—");
                    strokeColorRes = R.color.colorChipEstudiante;
                    break;
                case Entrega.ESTADO_CALIFICADA:
                    chipDrawable = R.drawable.bg_chip_docente;
                    chipLabel = "CALIFICADA";
                    fechaTxt = "Entregada: " + (e.fechaEntrega != null ? e.fechaEntrega : "—");
                    strokeColorRes = R.color.colorPrimary;
                    break;
                case Entrega.ESTADO_BORRADOR:
                    chipDrawable = R.drawable.bg_chip_pendiente;
                    chipLabel = "BORRADOR";
                    fechaTxt = "Sin envío";
                    strokeColorRes = R.color.colorChipPendiente;
                    break;
                default:
                    chipDrawable = R.drawable.bg_chip_admin;
                    chipLabel = "SIN ENTREGAR";
                    fechaTxt = "—";
                    strokeColorRes = R.color.colorError;
                    break;
            }

            b.tvChipEstadoEntrega.setBackgroundResource(chipDrawable);
            b.tvChipEstadoEntrega.setText(chipLabel);
            b.tvFechaEntrega.setText(fechaTxt);

            int strokeColor = ContextCompat.getColor(b.getRoot().getContext(), strokeColorRes);
            ((MaterialCardView) b.getRoot()).setStrokeColor(ColorStateList.valueOf(strokeColor));

            boolean tieneCalificacion = e.calificacion != null;
            b.dividerCalificacion.setVisibility(tieneCalificacion ? View.VISIBLE : View.GONE);
            b.rowCalificacion.setVisibility(tieneCalificacion ? View.VISIBLE : View.GONE);
            if (tieneCalificacion) {
                b.tvCalificacionEntrega.setText(e.calificacion + "/100");
            }
        }
    }
}

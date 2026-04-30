package com.conectatec.ui.docente.tareas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;
import com.conectatec.databinding.ItemEntregaDocenteBinding;

import java.util.ArrayList;
import java.util.List;

public class EntregaDocenteAdapter
        extends RecyclerView.Adapter<EntregaDocenteAdapter.ViewHolder> {

    public static final int ESTADO_BORRADOR     = 0;
    public static final int ESTADO_ENTREGADA    = 1;
    public static final int ESTADO_CALIFICADA   = 2;
    public static final int ESTADO_SIN_ENTREGAR = 3;

    public static class EntregaDummyDocente {
        public final int alumnoId;
        public final String alumnoNombre;
        public final String alumnoIniciales;
        public final int estado;
        public final String fechaEntrega;
        public final Integer calificacion;

        public EntregaDummyDocente(int alumnoId, String alumnoNombre, String alumnoIniciales,
                                   int estado, String fechaEntrega, Integer calificacion) {
            this.alumnoId = alumnoId;
            this.alumnoNombre = alumnoNombre;
            this.alumnoIniciales = alumnoIniciales;
            this.estado = estado;
            this.fechaEntrega = fechaEntrega;
            this.calificacion = calificacion;
        }
    }

    public interface OnEntregaClickListener {
        void onClick(EntregaDummyDocente entrega);
    }

    private final List<EntregaDummyDocente> lista = new ArrayList<>();
    private final List<EntregaDummyDocente> listaCompleta = new ArrayList<>();
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
        for (EntregaDummyDocente e : listaCompleta) {
            if (e.estado == ESTADO_ENTREGADA || e.estado == ESTADO_CALIFICADA) c++;
        }
        return c;
    }

    public int conteoPendientes() {
        return conteoTotal() - conteoEntregadas();
    }

    public void cargarParaTarea(int tareaId) {
        listaCompleta.clear();

        TareaDocenteAdapter.TareaDummyDocente t =
                TareaDocenteAdapter.buscarPorId(tareaId);
        int grupoId = (t != null) ? t.grupoId : 1;

        String[] nombres = nombresParaGrupo(grupoId);
        String[] iniciales = inicialesParaGrupo(grupoId);
        int baseId = grupoId * 100;

        int[] estados = {
                ESTADO_ENTREGADA, ESTADO_ENTREGADA,
                ESTADO_CALIFICADA, ESTADO_CALIFICADA,
                ESTADO_BORRADOR, ESTADO_SIN_ENTREGAR
        };
        String[] fechas = {
                "05/05/2026", "06/05/2026",
                "03/05/2026", "04/05/2026",
                "—", null
        };
        Integer[] notas = { null, null, 92, 78, null, null };

        for (int i = 0; i < 6; i++) {
            listaCompleta.add(new EntregaDummyDocente(
                    baseId + (i + 1),
                    nombres[i], iniciales[i],
                    estados[i], fechas[i], notas[i]));
        }

        aplicarFiltros();
    }

    public void filtrar(@Nullable Integer estado) {
        this.filtroEstado = estado;
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        lista.clear();
        for (EntregaDummyDocente e : listaCompleta) {
            if (filtroEstado == null || filtroEstado == e.estado) {
                lista.add(e);
            }
        }
        notifyDataSetChanged();
    }

    public static List<EntregaDummyDocente> obtenerListaActual(EntregaDocenteAdapter a) {
        return new ArrayList<>(a.listaCompleta);
    }

    private static String[] nombresParaGrupo(int grupoId) {
        switch (grupoId) {
            case 1: return new String[]{
                    "Ana López", "Bruno García", "Carla Méndez",
                    "Diego Ruiz", "Elena Torres", "Fernando Vega" };
            case 2: return new String[]{
                    "Gabriela Luna", "Héctor Pérez", "Isabel Romero",
                    "Javier Castro", "Karla Soto", "Luis Mendoza" };
            case 3: return new String[]{
                    "María Reyes", "Néstor Aguilar", "Olivia Cano",
                    "Pablo Núñez", "Quetzal Ríos", "Rocío Salinas" };
            default: return new String[]{
                    "Alumno 1", "Alumno 2", "Alumno 3",
                    "Alumno 4", "Alumno 5", "Alumno 6" };
        }
    }

    private static String[] inicialesParaGrupo(int grupoId) {
        String[] nombres = nombresParaGrupo(grupoId);
        String[] inis = new String[nombres.length];
        for (int i = 0; i < nombres.length; i++) {
            inis[i] = calcularIniciales(nombres[i]);
        }
        return inis;
    }

    private static String calcularIniciales(String nombre) {
        String[] partes = nombre.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(2, partes.length); i++) {
            if (!partes[i].isEmpty()) sb.append(partes[i].charAt(0));
        }
        return sb.toString().toUpperCase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemEntregaDocenteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        EntregaDummyDocente e = lista.get(position);
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

        void bind(EntregaDummyDocente e) {
            b.tvInicialesEntrega.setText(e.alumnoIniciales);
            b.tvNombreEntrega.setText(e.alumnoNombre);

            int chipDrawable;
            String chipLabel;
            String fechaTxt;
            switch (e.estado) {
                case ESTADO_ENTREGADA:
                    chipDrawable = R.drawable.bg_chip_estudiante;
                    chipLabel = "ENTREGADA";
                    fechaTxt = "Entregada: " + (e.fechaEntrega != null ? e.fechaEntrega : "—");
                    break;
                case ESTADO_CALIFICADA:
                    chipDrawable = R.drawable.bg_chip_estudiante;
                    chipLabel = "CALIFICADA";
                    fechaTxt = "Entregada: " + (e.fechaEntrega != null ? e.fechaEntrega : "—");
                    break;
                case ESTADO_BORRADOR:
                    chipDrawable = R.drawable.bg_chip_pendiente;
                    chipLabel = "BORRADOR";
                    fechaTxt = "Sin envío";
                    break;
                default:
                    chipDrawable = R.drawable.bg_chip_admin;
                    chipLabel = "SIN ENTREGAR";
                    fechaTxt = "—";
                    break;
            }
            b.tvChipEstadoEntrega.setBackgroundResource(chipDrawable);
            b.tvChipEstadoEntrega.setText(chipLabel);
            b.tvFechaEntrega.setText(fechaTxt);

            if (e.calificacion != null) {
                b.tvCalificacionEntrega.setVisibility(View.VISIBLE);
                b.tvCalificacionEntrega.setText(e.calificacion + "/100");
            } else {
                b.tvCalificacionEntrega.setVisibility(View.GONE);
            }
        }
    }
}

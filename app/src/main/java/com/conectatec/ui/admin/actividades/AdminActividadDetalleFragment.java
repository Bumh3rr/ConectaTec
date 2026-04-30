package com.conectatec.ui.admin.actividades;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentAdminActividadDetalleBinding;

public class AdminActividadDetalleFragment extends Fragment {

    private FragmentAdminActividadDetalleBinding binding;
    private int actividadId;
    private int grupoId;

    // ── Modelo enriquecido para la vista de detalle ───────────────────────────

    private static class DetalleActividad {
        final int    id;
        final int    grupoId;
        final String titulo;
        final String grupo;
        final String tipo;
        final String docente;
        final String fechaPublicacion;
        final String fechaVencimiento;
        final String estado;
        final int    entregasRecibidas;
        final int    totalMiembros;
        final String descripcion;
        final String[] miembros;
        final boolean[] entregado;

        DetalleActividad(int id, int grupoId, String titulo, String grupo, String tipo,
                         String docente, String fechaPublicacion, String fechaVencimiento,
                         String estado, int entregasRecibidas, int totalMiembros,
                         String descripcion, String[] miembros, boolean[] entregado) {
            this.id                = id;
            this.grupoId           = grupoId;
            this.titulo            = titulo;
            this.grupo             = grupo;
            this.tipo              = tipo;
            this.docente           = docente;
            this.fechaPublicacion  = fechaPublicacion;
            this.fechaVencimiento  = fechaVencimiento;
            this.estado            = estado;
            this.entregasRecibidas = entregasRecibidas;
            this.totalMiembros     = totalMiembros;
            this.descripcion       = descripcion;
            this.miembros          = miembros;
            this.entregado         = entregado;
        }
    }

    private static final DetalleActividad[] DATOS = {
        new DetalleActividad(1, 1,
            "Proyecto Final de Semestre",
            "Ingeniería de Software – 6A",
            "Proyecto",
            "Prof. Carlos Bautista",
            "01/03/2025", "30/05/2025",
            "EN_CURSO", 18, 24,
            "Desarrollar un sistema de gestión académica utilizando patrones de diseño MVC/MVVM. El sistema debe incluir módulos de autenticación, registro de estudiantes y seguimiento de materias. Se entrega como repositorio en GitHub con documentación.",
            new String[]{"María González", "Jorge Hernández", "Ana Laura Méndez", "Roberto Sánchez"},
            new boolean[]{true, false, true, false}),

        new DetalleActividad(2, 2,
            "Parcial 2 – Bases de Datos",
            "Bases de Datos – 4B",
            "Examen",
            "Prof. Ana Ramírez",
            "01/04/2025", "15/04/2025",
            "COMPLETADA", 22, 22,
            "Examen parcial que cubre normalización (1FN, 2FN, 3FN), consultas SQL avanzadas con JOINs y subconsultas, procedimientos almacenados y triggers. Duración: 90 minutos. No se permiten apuntes.",
            new String[]{"Luis Pérez", "Sofia Torres", "Diego Martínez", "Valeria Cruz"},
            new boolean[]{true, true, true, true}),

        new DetalleActividad(3, 3,
            "Práctica de Laboratorio #3",
            "Redes de Computadoras – 3A",
            "Práctica",
            "Prof. Luis Torres",
            "20/04/2025", "05/05/2025",
            "EN_CURSO", 8, 20,
            "Configuración y análisis de una red LAN en el laboratorio. Incluye subnetting con VLSM, configuración de routers y switches Cisco en Packet Tracer, y análisis de tráfico con Wireshark. Entregar reporte en PDF.",
            new String[]{"Carlos Ruiz", "Fernanda López", "Miguel Ángel Díaz", "Patricia Morales"},
            new boolean[]{true, false, true, false}),

        new DetalleActividad(4, 4,
            "Ensayo de Ética Profesional",
            "Humanidades – 5C",
            "Ensayo",
            "Prof. Marta García",
            "15/04/2025", "20/05/2025",
            "PENDIENTE", 0, 18,
            "Redactar un ensayo de 1500–2000 palabras sobre las responsabilidades éticas del ingeniero en la era digital. Debe incluir al menos 2 casos de estudio reales y seguir el formato APA 7ma edición.",
            new String[]{"Isabel Hernández", "Ricardo Jiménez", "Claudia Reyes", "Andrés Vargas"},
            new boolean[]{false, false, false, false}),

        new DetalleActividad(5, 4,
            "Examen Final Cálculo II",
            "Cálculo Diferencial – 2B",
            "Examen",
            "Prof. Jorge López",
            "01/03/2025", "10/04/2025",
            "VENCIDA", 15, 25,
            "Evaluación final que abarca derivadas, integrales definidas e indefinidas, límites y aplicaciones del cálculo en problemas de ingeniería. Calculadora científica permitida. No se admiten entregas después de la fecha.",
            new String[]{"Andrea Castro", "Héctor Mendoza", "Lucía Flores", "Pablo Guerrero"},
            new boolean[]{true, true, false, true}),

        new DetalleActividad(6, 1,
            "Reporte de Investigación",
            "Ingeniería de Software – 6A",
            "Reporte",
            "Prof. Carlos Bautista",
            "10/04/2025", "25/05/2025",
            "EN_CURSO", 12, 24,
            "Investigar y documentar el estado del arte de un tema de Ingeniería de Software a elección. Formato IEEE, mínimo 10 referencias de los últimos 5 años. Extensión: 8–12 páginas. Entregar en Overleaf (LaTeX).",
            new String[]{"María González", "Jorge Hernández", "Daniela Ortiz", "Samuel Vega"},
            new boolean[]{true, false, true, true}),

        new DetalleActividad(7, 5,
            "Taller de Programación OO",
            "POO – 1A",
            "Taller",
            "Prof. Sandra Martínez",
            "01/03/2025", "20/03/2025",
            "COMPLETADA", 30, 30,
            "Sesión práctica de programación orientada a objetos. Implementar los patrones de diseño Factory Method, Observer y Strategy en Java. El código debe pasar todas las pruebas unitarias proporcionadas y estar subido a GitHub.",
            new String[]{"Gabriela Luna", "Óscar Domínguez", "Natalia Ramírez", "Emmanuel Torres"},
            new boolean[]{true, true, true, true}),

        new DetalleActividad(8, 1,
            "Diseño de Arquitectura SW",
            "Ingeniería de Software – 6A",
            "Proyecto",
            "Prof. Carlos Bautista",
            "01/05/2025", "15/06/2025",
            "PENDIENTE", 0, 24,
            "Diseñar la arquitectura de software para un sistema de e-commerce. Entregar diagramas UML (clases, secuencia, despliegue), decisiones de diseño documentadas y justificación técnica de la arquitectura elegida. Trabajo individual.",
            new String[]{"María González", "Jorge Hernández", "Ana Laura Méndez", "Roberto Sánchez"},
            new boolean[]{false, false, false, false}),
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminActividadDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        actividadId = getArguments() != null ? getArguments().getInt("actividadId", 1) : 1;
        DetalleActividad detalle = buscarPorId(actividadId);
        grupoId = detalle.grupoId;

        cargarDatos(detalle);
        setupListeners();
    }

    private DetalleActividad buscarPorId(int id) {
        for (DetalleActividad d : DATOS) {
            if (d.id == id) return d;
        }
        return DATOS[0];
    }

    private void cargarDatos(DetalleActividad d) {
        // Info general
        binding.tvTituloDetActiv.setText(d.titulo);
        binding.tvGrupoDetActiv.setText(d.grupo);
        binding.tvTipoDetActiv.setText("Tipo: " + d.tipo);
        binding.tvDocenteDetActiv.setText(d.docente);
        binding.tvFechaPublicacion.setText(d.fechaPublicacion);
        binding.tvFechaVenceDetActiv.setText(d.fechaVencimiento);

        // Chip de estado
        aplicarEstadoChip(d.estado);

        // Progreso
        int porcentaje = d.totalMiembros > 0
                ? (int) (d.entregasRecibidas * 100f / d.totalMiembros) : 0;
        binding.progressEntregas.setProgress(porcentaje);
        binding.tvStatTotal.setText(String.valueOf(d.totalMiembros));
        binding.tvStatEntregadas.setText(String.valueOf(d.entregasRecibidas));
        binding.tvStatPendientes.setText(String.valueOf(d.totalMiembros - d.entregasRecibidas));

        // Descripción
        binding.tvDescripcionDetActiv.setText(d.descripcion);

        // Miembros
        cargarMiembros(d);
    }

    private void aplicarEstadoChip(String estado) {
        String label;
        int chipDrawable;

        switch (estado) {
            case "COMPLETADA":
                label = "COMPLETADA";
                chipDrawable = R.drawable.bg_chip_estudiante;
                break;
            case "VENCIDA":
                label = "VENCIDA";
                chipDrawable = R.drawable.bg_chip_admin;
                break;
            case "PENDIENTE":
                label = "PENDIENTE";
                chipDrawable = R.drawable.bg_chip_pendiente;
                break;
            default:
                label = "EN CURSO";
                chipDrawable = R.drawable.bg_chip_docente;
                break;
        }

        binding.tvChipEstadoDetActiv.setText(label);
        binding.tvChipEstadoDetActiv.setBackgroundResource(chipDrawable);
    }

    private void cargarMiembros(DetalleActividad d) {
        binding.containerMiembrosDetActiv.removeAllViews();
        int preview = Math.min(d.miembros.length, 4);

        for (int i = 0; i < preview; i++) {
            LinearLayout fila = new LinearLayout(requireContext());
            fila.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = dpToPx(10);
            fila.setLayoutParams(params);

            // Nombre del miembro
            TextView tvNombre = new TextView(requireContext());
            tvNombre.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            tvNombre.setText(d.miembros[i]);
            tvNombre.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnSurface));
            tvNombre.setTextSize(13f);

            // Etiqueta de estado de entrega
            TextView tvEstadoEntrega = new TextView(requireContext());
            tvEstadoEntrega.setText(d.entregado[i] ? "Entregado" : "Pendiente");
            tvEstadoEntrega.setTextSize(12f);
            tvEstadoEntrega.setTextColor(ContextCompat.getColor(requireContext(),
                    d.entregado[i] ? R.color.colorSuccess : R.color.colorWarning));

            fila.addView(tvNombre);
            fila.addView(tvEstadoEntrega);
            binding.containerMiembrosDetActiv.addView(fila);
        }
    }

    private void setupListeners() {
        binding.btnVolverActividad.setOnClickListener(v ->
                requireActivity().onBackPressed());

        binding.tvVerTodosMiembrosDetActiv.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", grupoId);
            args.putInt("actividadId", actividadId);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_actividad_detalle_to_miembros, args);
        });
    }

    private int dpToPx(int dp) {
        float density = requireContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

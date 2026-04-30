package com.conectatec.ui.docente.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocenteDashboardBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Dashboard del Docente.
 *
 * Muestra:
 *  - Resumen con 2 cards (Grupos activos, Alumnos totales).
 *  - Card de "Tareas recientes" (3 entradas dummy hardcodeadas en el XML).
 *  - Card de "Avisos del día" (3 entradas dummy hardcodeadas en el XML).
 *
 * Las dos cards de resumen navegan a la pestaña Grupos. El "Ver todas" de tareas
 * navega a la pestaña Tareas.
 */
@AndroidEntryPoint
public class DocenteDashboardFragment extends Fragment {

    private FragmentDocenteDashboardBinding binding;

    /** Tareas recientes mostradas en el dashboard (dummy estático). */
    private static final String[][] TAREAS_RECIENTES = {
            // {tipo, titulo, grupo, fechaVence, tiempoRelativo}
            {"PROYECTO", "Proyecto Final POO", "Prog. Móvil 6A", "03/05/2026", "hace 2h"},
            {"EXAMEN",   "Examen parcial 2",   "BD 4B",          "02/05/2026", "hace 5h"},
            {"TAREA",    "Tarea 5: Integrales", "Cálculo 2A",    "30/04/2026", "ayer"}
    };

    /** Avisos del día mostrados en el dashboard (dummy estático). */
    private static final String[][] AVISOS_DEL_DIA = {
            // {titulo, grupo, tiempoRelativo}
            {"Cambio de horario",            "Prog. Móvil 6A", "hace 30 min"},
            {"Material adicional subido",    "BD 4B",          "hace 2h"},
            {"Recordatorio examen",          "Cálculo 2A",     "hace 4h"}
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListeners();
    }

    private void setupListeners() {
        binding.cardGruposActivosDocente.setOnClickListener(v ->
                navigateToTab(v, R.id.docenteGruposFragment));

        binding.cardAlumnosTotales.setOnClickListener(v ->
                navigateToTab(v, R.id.docenteGruposFragment));

        binding.tvVerTodasTareasDashboard.setOnClickListener(v ->
                navigateToTab(v, R.id.docenteTareasFragment));
    }

    /** Navega a una pestaña raíz preservando estado y evitando apilar copias. */
    private void navigateToTab(View v, int destId) {
        NavController nav = Navigation.findNavController(v);
        NavOptions opts = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(nav.getGraph().getStartDestinationId(), false, true)
                .build();
        nav.navigate(destId, null, opts);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

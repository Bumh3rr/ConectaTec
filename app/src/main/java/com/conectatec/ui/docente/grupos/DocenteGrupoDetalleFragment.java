package com.conectatec.ui.docente.grupos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocenteGrupoDetalleBinding;
import com.conectatec.ui.docente.grupos.adapter.GrupoDocenteAdapter.GrupoDummyDocente;
import com.conectatec.ui.docente.grupos.adapter.GrupoDocenteAdapter;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Detalle de un grupo del docente.
 *
 * Muestra: información del grupo, preview de miembros, avisos y total de tareas.
 * Navega a: lista completa de miembros y lista de tareas del grupo.
 */
@AndroidEntryPoint
public class DocenteGrupoDetalleFragment extends Fragment {

    // miembros dummy (preview) por grupo
    private static final String[][] MIEMBROS_PREVIEW = {
            // grupo 1
            {"Ana García", "Laura Méndez", "Carlos Torres"},
            // grupo 2
            {"Pedro Ruiz", "María López", "Jorge Sánchez"},
            // grupo 3
            {"Sofía Castro", "Luis Herrera", "Elena Vargas"}
    };

    // avisos dummy por grupo
    private static final String[][] AVISOS_PREVIEW = {
            {"Cambio de horario – Lunes 8:00 a 9:00", "Material adicional en Classroom"},
            {"Examen parcial el viernes", "Revisar capítulos 5 y 6"},
            {"Entrega del proyecto final – 30/05", "No habrá clase el jueves"}
    };

    // tareas publicadas dummy por grupo
    private static final int[] TOTAL_TAREAS = {5, 4, 3};

    private FragmentDocenteGrupoDetalleBinding binding;
    private int grupoId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteGrupoDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        grupoId = getArguments() != null ? getArguments().getInt("grupoId", 1) : 1;
        cargarDatos();
        setupListeners();
    }

    private void cargarDatos() {
        List<GrupoDummyDocente> grupos = GrupoDocenteAdapter.cargarDatosDummy();
        GrupoDummyDocente grupo = null;
        for (GrupoDummyDocente g : grupos) {
            if (g.id == grupoId) { grupo = g; break; }
        }
        if (grupo == null) return;

        binding.tvNombreDetalleGrupo.setText(grupo.nombre);
        binding.tvMateriaDetalleGrupo.setText(grupo.materia);
        binding.tvCodigoDetalleGrupo.setText(grupo.codigoUnion);
        binding.tvFechaDetalleGrupo.setText(grupo.fechaCreacion);
        binding.tvAlumnosDetalleGrupo.setText(grupo.totalAlumnos + " alumnos");

        int idx = grupoId - 1;
        binding.tvTotalTareasGrupoDocente.setText(String.valueOf(TOTAL_TAREAS[idx]));
        cargarMiembrosPreview(MIEMBROS_PREVIEW[idx]);
        cargarAvisosPreview(AVISOS_PREVIEW[idx]);
    }

    private void cargarMiembrosPreview(String[] nombres) {
        binding.containerMiembrosGrupoDocente.removeAllViews();
        LayoutInflater inf = LayoutInflater.from(requireContext());
        for (String nombre : nombres) {
            TextView tv = new TextView(requireContext());
            tv.setText("• " + nombre);
            tv.setTextColor(getResources().getColor(com.conectatec.R.color.colorOnSurface, null));
            tv.setTextSize(13f);
            tv.setPadding(0, 6, 0, 6);
            binding.containerMiembrosGrupoDocente.addView(tv);
        }
    }

    private void cargarAvisosPreview(String[] avisos) {
        binding.containerAvisosGrupoDocente.removeAllViews();
        for (String aviso : avisos) {
            TextView tv = new TextView(requireContext());
            tv.setText("• " + aviso);
            tv.setTextColor(getResources().getColor(com.conectatec.R.color.colorOnSurface, null));
            tv.setTextSize(13f);
            tv.setPadding(0, 6, 0, 6);
            binding.containerAvisosGrupoDocente.addView(tv);
        }
    }

    private void setupListeners() {
        binding.btnBackDetalleGrupo.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        binding.btnVerQrDetalleDocente.setOnClickListener(v -> {
            boolean visible = binding.layoutQrDetalleDocente.getVisibility() == View.VISIBLE;
            binding.layoutQrDetalleDocente.setVisibility(visible ? View.GONE : View.VISIBLE);
        });

        binding.tvVerTodosMiembrosDocente.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", grupoId);
            Navigation.findNavController(v)
                    .navigate(R.id.action_grupo_detalle_to_miembros, args);
        });

        binding.btnVerTareasGrupo.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", grupoId);
            Navigation.findNavController(v)
                    .navigate(R.id.action_grupo_detalle_to_tareas_grupo, args);
        });

        binding.btnPublicarAviso.setOnClickListener(v -> {
            // TODO: llamar a AvisoService.publicar(grupoId)
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

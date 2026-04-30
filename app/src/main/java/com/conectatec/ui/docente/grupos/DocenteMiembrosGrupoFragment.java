package com.conectatec.ui.docente.grupos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectatec.databinding.FragmentDocenteMiembrosGrupoBinding;
import com.conectatec.ui.docente.grupos.adapter.MiembroGrupoDocenteAdapter;
import com.conectatec.ui.docente.grupos.adapter.MiembroGrupoDocenteAdapter.MiembroDummyDocente;

import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Lista completa de miembros de un grupo del docente.
 *
 * 3 grupos × 6 miembros = 18 entradas dummy.
 */
@AndroidEntryPoint
public class DocenteMiembrosGrupoFragment extends Fragment {

    private static final MiembroDummyDocente[][] MIEMBROS_POR_GRUPO = {
            // Grupo 1 — Programación Móvil 6A
            {
                new MiembroDummyDocente(101, "Ana García",     "AG", "ana.garcia@tec.mx",     "L20010001"),
                new MiembroDummyDocente(102, "Luis Martínez",  "LM", "luis.martinez@tec.mx",  "L20010002"),
                new MiembroDummyDocente(103, "Sofía López",    "SL", "sofia.lopez@tec.mx",    "L20010003"),
                new MiembroDummyDocente(104, "Carlos Ramírez", "CR", "carlos.ramirez@tec.mx", "L20010004"),
                new MiembroDummyDocente(105, "María Torres",   "MT", "maria.torres@tec.mx",   "L20010005"),
                new MiembroDummyDocente(106, "Pedro Sánchez",  "PS", "pedro.sanchez@tec.mx",  "L20010006"),
            },
            // Grupo 2 — Bases de Datos 4B
            {
                new MiembroDummyDocente(201, "Laura Mendoza",  "LM", "laura.mendoza@tec.mx",  "L20020001"),
                new MiembroDummyDocente(202, "Jorge Herrera",  "JH", "jorge.herrera@tec.mx",  "L20020002"),
                new MiembroDummyDocente(203, "Elena Castro",   "EC", "elena.castro@tec.mx",   "L20020003"),
                new MiembroDummyDocente(204, "Andrés Ruiz",    "AR", "andres.ruiz@tec.mx",    "L20020004"),
                new MiembroDummyDocente(205, "Valeria Díaz",   "VD", "valeria.diaz@tec.mx",   "L20020005"),
                new MiembroDummyDocente(206, "Roberto Vargas", "RV", "roberto.vargas@tec.mx", "L20020006"),
            },
            // Grupo 3 — Cálculo Integral 2A
            {
                new MiembroDummyDocente(301, "Daniela Flores", "DF", "daniela.flores@tec.mx", "L20030001"),
                new MiembroDummyDocente(302, "Miguel Ortega",  "MO", "miguel.ortega@tec.mx",  "L20030002"),
                new MiembroDummyDocente(303, "Paola Reyes",    "PR", "paola.reyes@tec.mx",    "L20030003"),
                new MiembroDummyDocente(304, "Iván Morales",   "IM", "ivan.morales@tec.mx",   "L20030004"),
                new MiembroDummyDocente(305, "Alejandra Gil",  "AG", "alejandra.gil@tec.mx",  "L20030005"),
                new MiembroDummyDocente(306, "Fernando Cruz",  "FC", "fernando.cruz@tec.mx",  "L20030006"),
            }
    };

    private static final String[] NOMBRES_GRUPO = {
            "Programación Móvil 6A",
            "Bases de Datos 4B",
            "Cálculo Integral 2A"
    };

    private FragmentDocenteMiembrosGrupoBinding binding;
    private MiembroGrupoDocenteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteMiembrosGrupoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int grupoId = getArguments() != null ? getArguments().getInt("grupoId", 1) : 1;
        setupRecyclerView(grupoId);

        binding.btnBackMiembrosDocente.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());
    }

    private void setupRecyclerView(int grupoId) {
        adapter = new MiembroGrupoDocenteAdapter();
        binding.rvMiembrosGrupoDocente.setAdapter(adapter);

        int idx = grupoId - 1;
        List<MiembroDummyDocente> lista = Arrays.asList(MIEMBROS_POR_GRUPO[idx]);
        adapter.setLista(lista);

        binding.tvSubtituloMiembrosDocente.setText(NOMBRES_GRUPO[idx]);
        binding.tvBadgeMiembrosDocente.setText(adapter.conteo() + " alumnos");

        int total = adapter.conteo();
        binding.rvMiembrosGrupoDocente.setVisibility(total > 0 ? View.VISIBLE : View.GONE);
        binding.emptyStateMiembrosDocente.getRoot().setVisibility(total > 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

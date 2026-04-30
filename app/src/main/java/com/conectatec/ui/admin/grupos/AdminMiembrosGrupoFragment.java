package com.conectatec.ui.admin.grupos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.conectatec.R;
import com.conectatec.databinding.FragmentAdminMiembrosGrupoBinding;
import com.conectatec.ui.admin.grupos.adapter.MiembrosGrupoAdapter;
import com.conectatec.ui.admin.grupos.adapter.MiembrosGrupoAdapter.MiembroDummy;

import java.util.ArrayList;
import java.util.List;

public class AdminMiembrosGrupoFragment extends Fragment {

    private FragmentAdminMiembrosGrupoBinding binding;
    private MiembrosGrupoAdapter adapter;

    // Miembros por grupo (grupoId 1–6)
    private static final MiembroDummy[][] MIEMBROS_POR_GRUPO = {
        // grupoId 1 – Ingeniería de Software – 6A
        {
            new MiembroDummy(1,  "María González",      "MG", "m.gonzalez@tec.mx",   "ESTUDIANTE"),
            new MiembroDummy(2,  "Jorge Hernández",     "JH", "j.hernandez@tec.mx",  "ESTUDIANTE"),
            new MiembroDummy(3,  "Ana Laura Méndez",    "AM", "a.mendez@tec.mx",     "ESTUDIANTE"),
            new MiembroDummy(4,  "Roberto Sánchez",     "RS", "r.sanchez@tec.mx",    "ESTUDIANTE"),
            new MiembroDummy(5,  "Daniela Ortiz",       "DO", "d.ortiz@tec.mx",      "ESTUDIANTE"),
            new MiembroDummy(6,  "Samuel Vega",         "SV", "s.vega@tec.mx",       "ESTUDIANTE"),
            new MiembroDummy(7,  "Fernanda López",      "FL", "f.lopez@tec.mx",      "ESTUDIANTE"),
            new MiembroDummy(8,  "Carlos Ruiz",         "CR", "c.ruiz@tec.mx",       "ESTUDIANTE"),
        },
        // grupoId 2 – Bases de Datos – 4B
        {
            new MiembroDummy(11, "Luis Pérez",          "LP", "l.perez@tec.mx",      "ESTUDIANTE"),
            new MiembroDummy(12, "Sofia Torres",        "ST", "s.torres@tec.mx",     "ESTUDIANTE"),
            new MiembroDummy(13, "Diego Martínez",      "DM", "d.martinez@tec.mx",   "ESTUDIANTE"),
            new MiembroDummy(14, "Valeria Cruz",        "VC", "v.cruz@tec.mx",       "ESTUDIANTE"),
            new MiembroDummy(15, "Pablo Guerrero",      "PG", "p.guerrero@tec.mx",   "ESTUDIANTE"),
            new MiembroDummy(16, "Isabel Hernández",    "IH", "i.hernandez@tec.mx",  "ESTUDIANTE"),
        },
        // grupoId 3 – Redes de Computadoras – 5C
        {
            new MiembroDummy(21, "Carlos Ruiz",         "CR", "c.ruiz2@tec.mx",      "ESTUDIANTE"),
            new MiembroDummy(22, "Fernanda López",      "FL", "f.lopez2@tec.mx",     "ESTUDIANTE"),
            new MiembroDummy(23, "Miguel Ángel Díaz",   "MD", "m.diaz@tec.mx",       "ESTUDIANTE"),
            new MiembroDummy(24, "Patricia Morales",    "PM", "p.morales@tec.mx",    "ESTUDIANTE"),
            new MiembroDummy(25, "Ricardo Jiménez",     "RJ", "r.jimenez@tec.mx",    "ESTUDIANTE"),
            new MiembroDummy(26, "Claudia Reyes",       "CR", "c.reyes@tec.mx",      "ESTUDIANTE"),
        },
        // grupoId 4 – Cálculo Integral – 2A
        {
            new MiembroDummy(31, "Andrea Castro",       "AC", "a.castro@tec.mx",     "ESTUDIANTE"),
            new MiembroDummy(32, "Héctor Mendoza",      "HM", "h.mendoza@tec.mx",    "ESTUDIANTE"),
            new MiembroDummy(33, "Lucía Flores",        "LF", "l.flores@tec.mx",     "ESTUDIANTE"),
            new MiembroDummy(34, "Pablo Guerrero",      "PG", "p.guerrero2@tec.mx",  "ESTUDIANTE"),
            new MiembroDummy(35, "Isabel Hernández",    "IH", "i.hernandez2@tec.mx", "ESTUDIANTE"),
            new MiembroDummy(36, "Ricardo Jiménez",     "RJ", "r.jimenez2@tec.mx",   "ESTUDIANTE"),
            new MiembroDummy(37, "Andrés Vargas",       "AV", "a.vargas@tec.mx",     "ESTUDIANTE"),
        },
        // grupoId 5 – Programación Orientada – 3D
        {
            new MiembroDummy(41, "Gabriela Luna",       "GL", "g.luna@tec.mx",       "ESTUDIANTE"),
            new MiembroDummy(42, "Óscar Domínguez",    "OD", "o.dominguez@tec.mx",  "ESTUDIANTE"),
            new MiembroDummy(43, "Natalia Ramírez",     "NR", "n.ramirez@tec.mx",    "ESTUDIANTE"),
            new MiembroDummy(44, "Emmanuel Torres",     "ET", "e.torres@tec.mx",     "ESTUDIANTE"),
            new MiembroDummy(45, "Andrés Vargas",       "AV", "a.vargas2@tec.mx",    "ESTUDIANTE"),
            new MiembroDummy(46, "Claudia Reyes",       "CR", "c.reyes2@tec.mx",     "ESTUDIANTE"),
        },
        // grupoId 6 – Sistemas Operativos – 5A
        {
            new MiembroDummy(51, "Eduardo Morales",     "EM", "e.morales@tec.mx",    "ESTUDIANTE"),
            new MiembroDummy(52, "Carmen Vázquez",      "CV", "c.vazquez@tec.mx",    "ESTUDIANTE"),
            new MiembroDummy(53, "Rodrigo Fuentes",     "RF", "r.fuentes@tec.mx",    "ESTUDIANTE"),
            new MiembroDummy(54, "Paola Serrano",       "PS", "p.serrano@tec.mx",    "ESTUDIANTE"),
            new MiembroDummy(55, "Antonio Ríos",        "AR", "a.rios@tec.mx",       "ESTUDIANTE"),
            new MiembroDummy(56, "Mariana Peña",        "MP", "m.pena@tec.mx",       "ESTUDIANTE"),
        },
    };

    // Nombre del grupo por grupoId (índice base 0)
    private static final String[] NOMBRES_GRUPO = {
        "Ingeniería de Software – 6A",
        "Bases de Datos – 4B",
        "Redes de Computadoras – 5C",
        "Cálculo Integral – 2A",
        "Programación Orientada – 3D",
        "Sistemas Operativos – 5A",
    };

    // entregasRecibidas por actividadId (índice = actividadId - 1, valor = cuántos entregaron)
    private static final int[] ENTREGAS_POR_ACTIVIDAD = { 18, 22, 8, 0, 15, 12, 30, 0 };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminMiembrosGrupoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int grupoId    = getArguments() != null ? getArguments().getInt("grupoId", 1) : 1;
        int actividadId = getArguments() != null ? getArguments().getInt("actividadId", 0) : 0;

        setupRecyclerView(grupoId, actividadId);
        binding.btnVolverMiembros.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupRecyclerView(int grupoId, int actividadId) {
        adapter = new MiembrosGrupoAdapter();

        int idx = Math.max(0, Math.min(grupoId - 1, MIEMBROS_POR_GRUPO.length - 1));
        MiembroDummy[] plantilla = MIEMBROS_POR_GRUPO[idx];

        // Determinar cuántos entregaron (0 = sin contexto de actividad)
        int entregadas = 0;
        if (actividadId > 0 && actividadId <= ENTREGAS_POR_ACTIVIDAD.length) {
            entregadas = ENTREGAS_POR_ACTIVIDAD[actividadId - 1];
        }

        List<MiembroDummy> lista = new ArrayList<>();
        for (int i = 0; i < plantilla.length; i++) {
            MiembroDummy m = new MiembroDummy(
                    plantilla[i].id,
                    plantilla[i].nombre,
                    plantilla[i].iniciales,
                    plantilla[i].correo,
                    plantilla[i].rol);
            if (actividadId > 0) {
                m.entregado = i < entregadas;
            }
            lista.add(m);
        }

        adapter.setLista(lista);
        adapter.setOnClickListener(m -> {
            Bundle args = new Bundle();
            args.putInt("usuarioId", m.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_miembros_to_alumno_detalle, args);
        });

        binding.rvMiembros.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMiembros.setAdapter(adapter);

        // Header
        String nombreGrupo = idx < NOMBRES_GRUPO.length ? NOMBRES_GRUPO[idx] : "Grupo";
        binding.tvSubtituloMiembros.setText(nombreGrupo);
        binding.tvTotalMiembrosHeader.setText(String.valueOf(adapter.conteo()));

        boolean vacio = adapter.conteo() == 0;
        binding.rvMiembros.setVisibility(vacio ? View.GONE : View.VISIBLE);
        binding.emptyStateMiembros.getRoot().setVisibility(vacio ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

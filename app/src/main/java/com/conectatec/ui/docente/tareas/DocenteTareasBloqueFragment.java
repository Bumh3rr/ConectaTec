package com.conectatec.ui.docente.tareas;

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
import com.conectatec.databinding.FragmentDocenteTareasBloqueBinding;
import com.conectatec.ui.docente.tareas.adapter.TareaDocenteAdapter;
import com.conectatec.ui.docente.tareas.adapter.TareaDocenteAdapter.TareaDummyDocente;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteTareasBloqueFragment extends Fragment {

    private FragmentDocenteTareasBloqueBinding binding;
    private TareaDocenteAdapter adapter;

    private int grupoId;
    private int bloqueId;

    private String filtroTipo = null;
    private String filtroEstado = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteTareasBloqueBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        grupoId  = getArguments() != null ? getArguments().getInt("grupoId", 1) : 1;
        bloqueId = getArguments() != null ? getArguments().getInt("bloqueId", 1) : 1;

        binding.tvSubtituloTareasBloque.setText(
                "Bloque " + bloqueId + " — " + nombreDeGrupo(grupoId));
        binding.btnVolverTareasBloque.setOnClickListener(v ->
                requireActivity().onBackPressed());

        setupRecyclerView();
        setupChipsTipo();
        setupChipsEstado();
        setupBotonCrear();
        actualizarVista();
    }

    private void setupRecyclerView() {
        adapter = new TareaDocenteAdapter();
        adapter.setOnClickListener(t -> {
            Bundle args = new Bundle();
            args.putInt("tareaId", t.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_tareas_bloque_to_entregas, args);
        });
        binding.rvTareasBloque.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTareasBloque.setAdapter(adapter);
        adapter.cargarParaBloque(grupoId, bloqueId);
    }

    private void setupChipsTipo() {
        binding.chipTipoTodas.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroTipo = null; aplicar(); }
        });
        binding.chipTipoTarea.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroTipo = TareaDocenteAdapter.TIPO_TAREA; aplicar(); }
        });
        binding.chipTipoTrabajo.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroTipo = TareaDocenteAdapter.TIPO_TRABAJO; aplicar(); }
        });
        binding.chipTipoExamen.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroTipo = TareaDocenteAdapter.TIPO_EXAMEN; aplicar(); }
        });
        binding.chipTipoProyecto.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroTipo = TareaDocenteAdapter.TIPO_PROYECTO; aplicar(); }
        });
    }

    private void setupChipsEstado() {
        binding.chipEstadoTodas.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroEstado = null; aplicar(); }
        });
        binding.chipEstadoEnCurso.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroEstado = TareaDocenteAdapter.EST_EN_CURSO; aplicar(); }
        });
        binding.chipEstadoVencida.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroEstado = TareaDocenteAdapter.EST_VENCIDA; aplicar(); }
        });
        binding.chipEstadoCompletada.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroEstado = TareaDocenteAdapter.EST_COMPLETADA; aplicar(); }
        });
    }

    private void setupBotonCrear() {
        binding.btnCrearTareaBloque.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", grupoId);
            args.putInt("bloqueId", bloqueId);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_tareas_bloque_to_crear, args);
        });
    }

    private void aplicar() {
        adapter.filtrar(filtroTipo, filtroEstado);
        actualizarVista();
    }

    private void actualizarVista() {
        boolean vacio = adapter.conteo() == 0;
        binding.rvTareasBloque.setVisibility(vacio ? View.GONE : View.VISIBLE);
        binding.emptyStateTareasBloque.getRoot().setVisibility(vacio ? View.VISIBLE : View.GONE);
    }

    private String nombreDeGrupo(int id) {
        switch (id) {
            case 1: return "Programación Móvil 6A";
            case 2: return "Bases de Datos 4B";
            case 3: return "Cálculo Integral 2A";
            default: return "Grupo";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

package com.conectatec.ui.docente.tareas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.conectatec.R;
import com.conectatec.data.model.Tarea;
import com.conectatec.databinding.FragmentDocenteTareasBloqueBinding;
import com.conectatec.ui.common.ScrollRevealAnimator;
import com.conectatec.ui.common.UiState;
import com.conectatec.ui.docente.tareas.adapter.TareaDocenteAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteTareasBloqueFragment extends Fragment {

    private FragmentDocenteTareasBloqueBinding binding;
    private TareaDocenteAdapter adapter;
    private ScrollRevealAnimator scrollRevealAnimator;
    private DocenteTareasBloqueViewModel viewModel;

    private int grupoId;
    private int bloqueId;

    private String filtroTipo   = null;
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
        grupoId  = getArguments() != null ? getArguments().getInt("grupoId",  1) : 1;
        bloqueId = getArguments() != null ? getArguments().getInt("bloqueId", 1) : 1;

        binding.tvSubtituloTareasBloque.setText(
                "Bloque " + bloqueId + " — " + nombreDeGrupo(grupoId));
        binding.btnVolverTareasBloque.setOnClickListener(v ->
                requireActivity().onBackPressed());

        setupRecyclerView();
        scrollRevealAnimator = new ScrollRevealAnimator(binding.rvTareasBloque);
        setupFiltros();
        setupBotonCrear();

        viewModel = new ViewModelProvider(this).get(DocenteTareasBloqueViewModel.class);
        observeViewModel();
        viewModel.cargarTareas(grupoId, bloqueId);
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
    }

    private void observeViewModel() {
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            binding.progressBarTareasBloque.setVisibility(
                    state instanceof UiState.Loading ? View.VISIBLE : View.GONE);
            if (state instanceof UiState.Success) {
                List<Tarea> tareas = ((UiState.Success<List<Tarea>>) state).data;
                adapter.setListaCompleta(tareas);
                actualizarVista();
                scrollRevealAnimator.triggerInicial();
            } else if (state instanceof UiState.Error) {
                Snackbar.make(binding.getRoot(),
                        ((UiState.Error<?>) state).mensaje, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setupFiltros() {
        binding.chipGroupTipo.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int id = checkedIds.get(0);
            if      (id == R.id.chipTipoTarea)    filtroTipo = Tarea.TIPO_TAREA;
            else if (id == R.id.chipTipoTrabajo)  filtroTipo = Tarea.TIPO_TRABAJO;
            else if (id == R.id.chipTipoExamen)   filtroTipo = Tarea.TIPO_EXAMEN;
            else if (id == R.id.chipTipoProyecto) filtroTipo = Tarea.TIPO_PROYECTO;
            else                                   filtroTipo = null;
            aplicar();
        });

        binding.chipGroupEstado.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int id = checkedIds.get(0);
            if      (id == R.id.chipEstadoEnCurso)    filtroEstado = Tarea.EST_EN_CURSO;
            else if (id == R.id.chipEstadoVencida)    filtroEstado = Tarea.EST_VENCIDA;
            else if (id == R.id.chipEstadoCompletada) filtroEstado = Tarea.EST_COMPLETADA;
            else                                       filtroEstado = null;
            aplicar();
        });
    }

    private void setupBotonCrear() {
        binding.btnCrearTareaBloque.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("grupoId",  grupoId);
            args.putInt("bloqueId", bloqueId);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_tareas_bloque_to_crear, args);
        });
    }

    private void aplicar() {
        adapter.filtrar(filtroTipo, filtroEstado);
        actualizarVista();
        scrollRevealAnimator.triggerInicial();
    }

    private void actualizarVista() {
        int filtradas = adapter.conteo();
        int total     = adapter.totalBloque();

        boolean vacio = filtradas == 0;
        binding.rvTareasBloque.setVisibility(vacio ? View.GONE : View.VISIBLE);
        binding.emptyStateTareasBloque.getRoot().setVisibility(vacio ? View.VISIBLE : View.GONE);

        if (filtradas == total) {
            binding.tvStatsResumen.setText(total + " tarea" + (total != 1 ? "s" : ""));
        } else {
            binding.tvStatsResumen.setText(filtradas + " de " + total + " tarea" + (total != 1 ? "s" : ""));
        }
        binding.tvStatsEnCurso.setText(
                adapter.conteoEstado(Tarea.EST_EN_CURSO) + " en curso");
        int completadas = adapter.conteoEstado(Tarea.EST_COMPLETADA);
        binding.tvStatsCompletadas.setText(
                completadas + " completada" + (completadas != 1 ? "s" : ""));
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

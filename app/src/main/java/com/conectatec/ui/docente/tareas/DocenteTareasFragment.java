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
import com.conectatec.data.model.Grupo;
import com.conectatec.databinding.FragmentDocenteTareasBinding;
import com.conectatec.ui.common.ScrollRevealAnimator;
import com.conectatec.ui.common.UiState;
import com.conectatec.ui.docente.tareas.adapter.GrupoTareasDocenteAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteTareasFragment extends Fragment {

    private FragmentDocenteTareasBinding binding;
    private GrupoTareasDocenteAdapter adapter;
    private ScrollRevealAnimator scrollRevealAnimator;
    private DocenteTareasViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteTareasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        scrollRevealAnimator = new ScrollRevealAnimator(binding.rvTareasGrupos);
        viewModel = new ViewModelProvider(this).get(DocenteTareasViewModel.class);
        observeViewModel();
        viewModel.cargarDatos();
    }

    private void setupRecyclerView() {
        adapter = new GrupoTareasDocenteAdapter();
        adapter.setOnClickListener(g -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", g.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_tareas_to_bloques, args);
        });
        binding.rvTareasGrupos.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTareasGrupos.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            binding.progressBarTareasGrupos.setVisibility(
                    state instanceof UiState.Loading ? View.VISIBLE : View.GONE);
            if (state instanceof UiState.Success) {
                List<Grupo> grupos = ((UiState.Success<List<Grupo>>) state).data;
                adapter.setLista(grupos);
                int total = adapter.conteo();
                binding.tvHeaderTotalGruposTareas.setText(total + " grupos");
                binding.rvTareasGrupos.setVisibility(total > 0 ? View.VISIBLE : View.GONE);
                binding.emptyStateTareasGrupos.getRoot()
                        .setVisibility(total > 0 ? View.GONE : View.VISIBLE);
                scrollRevealAnimator.triggerInicial();
            } else if (state instanceof UiState.Error) {
                Snackbar.make(binding.getRoot(),
                        ((UiState.Error<?>) state).mensaje, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

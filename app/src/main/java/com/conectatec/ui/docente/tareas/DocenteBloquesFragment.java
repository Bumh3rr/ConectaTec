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
import com.conectatec.data.model.Bloque;
import com.conectatec.databinding.FragmentDocenteBloquesBinding;
import com.conectatec.ui.common.ScrollRevealAnimator;
import com.conectatec.ui.common.UiState;
import com.conectatec.ui.docente.tareas.adapter.BloqueDocenteAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteBloquesFragment extends Fragment {

    private FragmentDocenteBloquesBinding binding;
    private BloqueDocenteAdapter adapter;
    private ScrollRevealAnimator scrollRevealAnimator;
    private DocenteBloquesViewModel viewModel;
    private int grupoId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteBloquesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        grupoId = getArguments() != null ? getArguments().getInt("grupoId", 1) : 1;

        binding.tvSubtituloBloques.setText(nombreDeGrupo(grupoId));
        binding.btnVolverBloques.setOnClickListener(v ->
                requireActivity().onBackPressed());

        setupRecyclerView();
        scrollRevealAnimator = new ScrollRevealAnimator(binding.rvBloques);
        viewModel = new ViewModelProvider(this).get(DocenteBloquesViewModel.class);
        observeViewModel();
        viewModel.cargarBloques(grupoId);
        binding.fabCrearBloque.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", grupoId);
            args.putInt("siguienteNumero", adapter.getItemCount() + 1);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_bloques_to_crear_bloque, args);
        });
    }

    private void setupRecyclerView() {
        adapter = new BloqueDocenteAdapter();
        adapter.setOnClickListener(b -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", grupoId);
            args.putInt("bloqueId", b.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_bloques_to_tareas_bloque, args);
        });
        binding.rvBloques.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvBloques.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            binding.progressBarBloques.setVisibility(
                    state instanceof UiState.Loading ? View.VISIBLE : View.GONE);
            if (state instanceof UiState.Success) {
                List<Bloque> bloques = ((UiState.Success<List<Bloque>>) state).data;
                adapter.setLista(bloques);
                binding.rvBloques.setVisibility(View.VISIBLE);
                scrollRevealAnimator.triggerInicial();
            } else if (state instanceof UiState.Error) {
                Snackbar.make(binding.getRoot(),
                        ((UiState.Error<?>) state).mensaje, Snackbar.LENGTH_LONG).show();
            }
        });
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

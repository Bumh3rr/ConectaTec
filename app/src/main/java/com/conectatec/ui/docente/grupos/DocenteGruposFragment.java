package com.conectatec.ui.docente.grupos;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.data.model.Grupo;
import com.conectatec.databinding.FragmentDocenteGruposBinding;
import com.conectatec.ui.common.ScrollRevealAnimator;
import com.conectatec.ui.common.UiState;
import com.conectatec.ui.docente.grupos.adapter.GrupoDocenteAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteGruposFragment extends Fragment {

    private FragmentDocenteGruposBinding binding;
    private GrupoDocenteAdapter adapter;
    private ScrollRevealAnimator scrollRevealAnimator;
    private DocenteGruposViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteGruposBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DocenteGruposViewModel.class);
        setupRecyclerView();
        scrollRevealAnimator = new ScrollRevealAnimator(binding.rvGruposDocente);
        setupSearch();
        setupListeners();
        observeViewModel();
        viewModel.cargarDatos();
    }

    private void setupRecyclerView() {
        adapter = new GrupoDocenteAdapter(new ArrayList<>(), grupo -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", grupo.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_grupos_to_detalle, args);
        });
        binding.rvGruposDocente.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            binding.progressBarGrupos.setVisibility(
                    state instanceof UiState.Loading ? View.VISIBLE : View.GONE);
            if (state instanceof UiState.Success) {
                List<Grupo> grupos = ((UiState.Success<List<Grupo>>) state).data;
                adapter.setLista(grupos);
                actualizarUI();
                scrollRevealAnimator.triggerInicial();
            } else if (state instanceof UiState.Error) {
                Snackbar.make(binding.getRoot(),
                        ((UiState.Error<?>) state).mensaje, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setupSearch() {
        binding.etBuscarGrupoDocente.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                adapter.filtrar(s.toString());
                actualizarUI();
                scrollRevealAnimator.triggerInicial();
            }
        });
    }

    private void setupListeners() {
        binding.btnCrearGrupo.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_grupos_to_crear));
    }

    private void actualizarUI() {
        int total = adapter.conteo();
        binding.tvHeaderTotalGruposDocente.setText(total + " grupos");
        binding.rvGruposDocente.setVisibility(total > 0 ? View.VISIBLE : View.GONE);
        binding.emptyStateGruposDocente.getRoot().setVisibility(total > 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

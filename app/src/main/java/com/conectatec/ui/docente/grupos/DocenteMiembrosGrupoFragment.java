package com.conectatec.ui.docente.grupos;

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
import com.conectatec.data.model.Miembro;
import com.conectatec.databinding.FragmentDocenteMiembrosGrupoBinding;
import com.conectatec.ui.common.ScrollRevealAnimator;
import com.conectatec.ui.common.UiState;
import com.conectatec.ui.docente.grupos.adapter.MiembroGrupoDocenteAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteMiembrosGrupoFragment extends Fragment {

    private static final String[] NOMBRES_GRUPO = {
            "Programación Móvil 6A",
            "Bases de Datos 4B",
            "Cálculo Integral 2A"
    };

    private FragmentDocenteMiembrosGrupoBinding binding;
    private MiembroGrupoDocenteAdapter adapter;
    private ScrollRevealAnimator scrollRevealAnimator;
    private DocenteMiembrosViewModel viewModel;
    private int grupoId;

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
        grupoId = getArguments() != null ? getArguments().getInt("grupoId", 1) : 1;

        int idx = Math.max(0, Math.min(grupoId - 1, NOMBRES_GRUPO.length - 1));
        binding.tvSubtituloMiembrosDocente.setText(NOMBRES_GRUPO[idx]);

        setupRecyclerView();
        scrollRevealAnimator = new ScrollRevealAnimator(binding.rvMiembrosGrupoDocente);

        viewModel = new ViewModelProvider(this).get(DocenteMiembrosViewModel.class);
        observeViewModel();
        viewModel.cargarMiembros(grupoId);

        binding.btnBackMiembrosDocente.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());
    }

    private void setupRecyclerView() {
        adapter = new MiembroGrupoDocenteAdapter();
        binding.rvMiembrosGrupoDocente.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMiembrosGrupoDocente.setAdapter(adapter);

        adapter.setListener(new MiembroGrupoDocenteAdapter.OnMiembroActionListener() {
            @Override
            public void onVerPerfil(Miembro m) {
                Bundle args = new Bundle();
                args.putInt("alumnoId", m.id);
                args.putString("nombre", m.nombre);
                args.putString("iniciales", m.iniciales);
                args.putString("correo", m.correo);
                args.putString("matricula", m.matricula);
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_miembros_to_perfil_alumno, args);
            }

            @Override
            public void onMensaje(Miembro m) {
                Bundle args = new Bundle();
                args.putInt("salaId", 4);
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_miembros_to_conversacion, args);
            }
        });
    }

    private void observeViewModel() {
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            binding.progressBarMiembros.setVisibility(
                    state instanceof UiState.Loading ? View.VISIBLE : View.GONE);
            if (state instanceof UiState.Success) {
                List<Miembro> miembros = ((UiState.Success<List<Miembro>>) state).data;
                adapter.setLista(miembros);
                int total = adapter.conteo();
                binding.tvBadgeMiembrosDocente.setText(total + " alumnos");
                binding.rvMiembrosGrupoDocente.setVisibility(total > 0 ? View.VISIBLE : View.GONE);
                binding.emptyStateMiembrosDocente.getRoot()
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

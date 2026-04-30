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
import com.conectatec.databinding.FragmentDocenteBloquesBinding;
import com.conectatec.ui.docente.tareas.adapter.BloqueDocenteAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteBloquesFragment extends Fragment {

    private FragmentDocenteBloquesBinding binding;
    private BloqueDocenteAdapter adapter;
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
        adapter.cargarParaGrupo(grupoId);
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

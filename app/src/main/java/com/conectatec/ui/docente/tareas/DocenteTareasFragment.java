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
import com.conectatec.databinding.FragmentDocenteTareasBinding;
import com.conectatec.ui.docente.tareas.adapter.GrupoTareasDocenteAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteTareasFragment extends Fragment {

    private FragmentDocenteTareasBinding binding;
    private GrupoTareasDocenteAdapter adapter;

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

        binding.tvHeaderTotalGruposTareas.setText(String.valueOf(adapter.conteo()));

        boolean vacio = adapter.conteo() == 0;
        binding.rvTareasGrupos.setVisibility(vacio ? View.GONE : View.VISIBLE);
        binding.emptyStateTareasGrupos.getRoot().setVisibility(vacio ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

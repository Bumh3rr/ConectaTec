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
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocenteGruposBinding;
import com.conectatec.ui.docente.grupos.adapter.GrupoDocenteAdapter;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Lista de grupos del docente.
 *
 * Muestra:
 *  - Header con total de grupos (badge).
 *  - Botón "Crear grupo" → fragment crear grupo.
 *  - Buscador que filtra la lista.
 *  - RecyclerView con 3 grupos dummy.
 *  - Empty state cuando no hay resultados.
 */
@AndroidEntryPoint
public class DocenteGruposFragment extends Fragment {

    private FragmentDocenteGruposBinding binding;
    private GrupoDocenteAdapter adapter;

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
        setupRecyclerView();
        setupSearch();
        setupListeners();
    }

    private void setupRecyclerView() {
        adapter = new GrupoDocenteAdapter(
                GrupoDocenteAdapter.cargarDatosDummy(),
                grupo -> {
                    Bundle args = new Bundle();
                    args.putInt("grupoId", grupo.id);
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_grupos_to_detalle, args);
                });
        binding.rvGruposDocente.setAdapter(adapter);
        actualizarUI();
    }

    private void setupSearch() {
        binding.etBuscarGrupoDocente.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                adapter.filtrar(s.toString());
                actualizarUI();
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

package com.conectatec.ui.admin.grupos;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.conectatec.R;
import com.conectatec.databinding.FragmentAdminGruposBinding;
import com.conectatec.ui.admin.grupos.adapter.GrupoAdminAdapter;

public class AdminGruposFragment extends Fragment {

    private FragmentAdminGruposBinding binding;
    private GrupoAdminAdapter adapter;

    // Estado de los tres filtros combinados
    private @Nullable String   docenteFiltro = null;
    private @Nullable Boolean  activoFiltro  = null;
    private @NonNull  String   queryFiltro   = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminGruposBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String argDocente = getArguments() != null
                ? getArguments().getString("docenteNombre", "") : "";

        setupRecyclerView();
        setupChipsEstado();
        setupBusqueda();

        if (argDocente != null && !argDocente.isEmpty()) {
            aplicarFiltroDocente(argDocente);
        }
    }

    private void setupRecyclerView() {
        adapter = new GrupoAdminAdapter((grupo, pos) -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", grupo.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_grupos_to_detalle, args);
        });
        binding.rvGrupos.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvGrupos.setAdapter(adapter);
        actualizarUI();
    }

    private void setupChipsEstado() {
        binding.chipGruposTodos.setOnClickListener(v        -> { activoFiltro = null;  aplicarFiltros(); });
        binding.chipGruposActivos.setOnClickListener(v      -> { activoFiltro = true;  aplicarFiltros(); });
        binding.chipGruposDesactivados.setOnClickListener(v -> { activoFiltro = false; aplicarFiltros(); });
    }

    private void setupBusqueda() {
        binding.etBuscarGrupo.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                queryFiltro = s != null ? s.toString() : "";
                aplicarFiltros();
            }
        });
    }

    // Llamado cuando llega un filtro por docente desde fuera (user detail)
    private void aplicarFiltroDocente(String nombre) {
        docenteFiltro = nombre;
        aplicarFiltros();

        binding.bannerFiltroUsuario.setVisibility(View.VISIBLE);
        binding.chipFiltroDocente.setText("Grupos de " + nombre);
        binding.chipFiltroDocente.setOnCloseIconClickListener(v -> limpiarFiltroDocente());
        binding.chipGruposTodos.setChecked(true);
        activoFiltro = null;
    }

    private void limpiarFiltroDocente() {
        docenteFiltro = null;
        binding.bannerFiltroUsuario.setVisibility(View.GONE);
        binding.chipGruposTodos.setChecked(true);
        activoFiltro = null;
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        adapter.setFiltros(docenteFiltro, activoFiltro, queryFiltro);
        actualizarUI();
    }

    private void actualizarUI() {
        boolean vacio = adapter.getItemCount() == 0;
        binding.rvGrupos.setVisibility(vacio ? View.GONE : View.VISIBLE);
        binding.emptyStateGrupos.getRoot().setVisibility(vacio ? View.VISIBLE : View.GONE);
        binding.tvHeaderTotalGrupos.setText(String.valueOf(adapter.getItemCount()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

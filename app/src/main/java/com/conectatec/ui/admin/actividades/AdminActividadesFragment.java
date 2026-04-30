package com.conectatec.ui.admin.actividades;

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
import com.conectatec.databinding.FragmentAdminActividadesBinding;
import com.conectatec.ui.admin.actividades.adapter.ActividadAdminAdapter;
import com.conectatec.ui.admin.actividades.adapter.ActividadAdminAdapter.ActividadDummy;

public class AdminActividadesFragment extends Fragment {

    private FragmentAdminActividadesBinding binding;
    private ActividadAdminAdapter adapter;

    private String filtroEstadoActivo = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminActividadesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        setupChips();
        setupSearch();
    }

    private void setupRecyclerView() {
        adapter = new ActividadAdminAdapter();
        adapter.setOnClickListener(actividad -> {
            Bundle args = new Bundle();
            args.putInt("actividadId", actividad.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_actividades_to_detalle, args);
        });
        binding.rvActividades.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        binding.rvActividades.setAdapter(adapter);
        actualizarBadge();
    }

    private void setupChips() {
        binding.chipActividadesTodas.setOnCheckedChangeListener((chip, checked) -> {
            if (checked) {
                filtroEstadoActivo = null;
                aplicarFiltro();
            }
        });
        binding.chipActividadesEnCurso.setOnCheckedChangeListener((chip, checked) -> {
            if (checked) {
                filtroEstadoActivo = ActividadDummy.EN_CURSO;
                aplicarFiltro();
            }
        });
        binding.chipActividadesPendiente.setOnCheckedChangeListener((chip, checked) -> {
            if (checked) {
                filtroEstadoActivo = ActividadDummy.PENDIENTE;
                aplicarFiltro();
            }
        });
        binding.chipActividadesCompletada.setOnCheckedChangeListener((chip, checked) -> {
            if (checked) {
                filtroEstadoActivo = ActividadDummy.COMPLETADA;
                aplicarFiltro();
            }
        });
        binding.chipActividadesVencida.setOnCheckedChangeListener((chip, checked) -> {
            if (checked) {
                filtroEstadoActivo = ActividadDummy.VENCIDA;
                aplicarFiltro();
            }
        });
    }

    private void setupSearch() {
        binding.etBuscarActividad.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                aplicarFiltro();
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void aplicarFiltro() {
        String query = binding.etBuscarActividad.getText() != null
                ? binding.etBuscarActividad.getText().toString() : "";
        adapter.filtrar(filtroEstadoActivo, query);

        boolean vacio = adapter.conteo() == 0;
        binding.rvActividades.setVisibility(vacio ? View.GONE : View.VISIBLE);
        binding.emptyStateActividades.getRoot().setVisibility(vacio ? View.VISIBLE : View.GONE);
        actualizarBadge();
    }

    private void actualizarBadge() {
        binding.tvHeaderTotalActividades.setText(
                String.valueOf(adapter.listaCompleta.size()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

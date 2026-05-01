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
import com.conectatec.databinding.FragmentDocenteEntregasBinding;
import com.conectatec.ui.common.ScrollRevealAnimator;
import com.conectatec.ui.docente.tareas.adapter.EntregaDocenteAdapter;
import com.conectatec.ui.docente.tareas.adapter.TareaDocenteAdapter;
import com.conectatec.ui.docente.tareas.adapter.TareaDocenteAdapter.TareaDummyDocente;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteEntregasFragment extends Fragment {

    private FragmentDocenteEntregasBinding binding;
    private EntregaDocenteAdapter adapter;
    private ScrollRevealAnimator scrollRevealAnimator;
    private int tareaId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteEntregasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tareaId = getArguments() != null ? getArguments().getInt("tareaId", 1) : 1;

        TareaDummyDocente t = TareaDocenteAdapter.buscarPorId(tareaId);
        binding.tvSubtituloEntregas.setText(t != null ? t.titulo : "Tarea");

        binding.btnVolverEntregas.setOnClickListener(v ->
                requireActivity().onBackPressed());

        setupRecyclerView();
        scrollRevealAnimator = new ScrollRevealAnimator(binding.rvEntregas);
        setupChips();
        actualizarResumen();
        scrollRevealAnimator.triggerInicial();
    }

    private void setupRecyclerView() {
        adapter = new EntregaDocenteAdapter();
        adapter.setOnClickListener(e -> {
            Bundle args = new Bundle();
            args.putInt("tareaId", tareaId);
            args.putInt("alumnoId", e.alumnoId);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_entregas_to_calificar, args);
        });
        binding.rvEntregas.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvEntregas.setAdapter(adapter);
        adapter.cargarParaTarea(tareaId);
    }

    private void setupChips() {
        binding.chipGroupEntregas.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int id = checkedIds.get(0);
            if      (id == R.id.chipEntregasTodas)        adapter.filtrar(null);
            else if (id == R.id.chipEntregasEntregadas)   adapter.filtrar(EntregaDocenteAdapter.ESTADO_ENTREGADA);
            else if (id == R.id.chipEntregasCalificadas)  adapter.filtrar(EntregaDocenteAdapter.ESTADO_CALIFICADA);
            else if (id == R.id.chipEntregasPendientes)   adapter.filtrar(EntregaDocenteAdapter.ESTADO_BORRADOR);
            else if (id == R.id.chipEntregasSinEntregar)  adapter.filtrar(EntregaDocenteAdapter.ESTADO_SIN_ENTREGAR);
            actualizarVista();
            scrollRevealAnimator.triggerInicial();
        });
    }

    private void actualizarResumen() {
        int total = adapter.conteoTotal();
        int entregadas = adapter.conteoEntregadas();
        int pendientes = adapter.conteoPendientes();
        int progreso = total > 0 ? (int) (entregadas * 100f / total) : 0;

        binding.tvStatTotalEntregas.setText(String.valueOf(total));
        binding.tvStatEntregadasEntregas.setText(String.valueOf(entregadas));
        binding.tvStatPendientesEntregas.setText(String.valueOf(pendientes));
        binding.progressEntregasResumen.setProgress(progreso);

        actualizarVista();
    }

    private void actualizarVista() {
        int filtradas = adapter.conteo();
        int total = adapter.conteoTotal();

        boolean vacio = filtradas == 0;
        binding.rvEntregas.setVisibility(vacio ? View.GONE : View.VISIBLE);
        binding.emptyStateEntregas.getRoot().setVisibility(vacio ? View.VISIBLE : View.GONE);

        if (filtradas == total) {
            binding.tvStatsResumenEntregas.setText(
                    total + " entrega" + (total != 1 ? "s" : ""));
        } else {
            binding.tvStatsResumenEntregas.setText(
                    filtradas + " de " + total + " entrega" + (total != 1 ? "s" : ""));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

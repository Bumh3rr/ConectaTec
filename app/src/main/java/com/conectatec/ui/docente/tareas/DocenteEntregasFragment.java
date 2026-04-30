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
import com.conectatec.ui.docente.tareas.adapter.EntregaDocenteAdapter;
import com.conectatec.ui.docente.tareas.adapter.TareaDocenteAdapter;
import com.conectatec.ui.docente.tareas.adapter.TareaDocenteAdapter.TareaDummyDocente;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteEntregasFragment extends Fragment {

    private FragmentDocenteEntregasBinding binding;
    private EntregaDocenteAdapter adapter;
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
        setupChips();
        actualizarResumen();
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
        binding.chipEntregasTodas.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { adapter.filtrar(null); actualizarVista(); }
        });
        binding.chipEntregasPendientes.setOnCheckedChangeListener((c, checked) -> {
            if (checked) {
                adapter.filtrar(EntregaDocenteAdapter.ESTADO_BORRADOR);
                actualizarVista();
            }
        });
        binding.chipEntregasEntregadas.setOnCheckedChangeListener((c, checked) -> {
            if (checked) {
                adapter.filtrar(EntregaDocenteAdapter.ESTADO_ENTREGADA);
                actualizarVista();
            }
        });
        binding.chipEntregasCalificadas.setOnCheckedChangeListener((c, checked) -> {
            if (checked) {
                adapter.filtrar(EntregaDocenteAdapter.ESTADO_CALIFICADA);
                actualizarVista();
            }
        });
        binding.chipEntregasSinEntregar.setOnCheckedChangeListener((c, checked) -> {
            if (checked) {
                adapter.filtrar(EntregaDocenteAdapter.ESTADO_SIN_ENTREGAR);
                actualizarVista();
            }
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
        boolean vacio = adapter.conteo() == 0;
        binding.rvEntregas.setVisibility(vacio ? View.GONE : View.VISIBLE);
        binding.emptyStateEntregas.getRoot().setVisibility(vacio ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

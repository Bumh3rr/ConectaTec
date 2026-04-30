package com.conectatec.ui.admin.usuarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.conectatec.databinding.FragmentAdminPendientesBinding;
import com.conectatec.ui.admin.usuarios.adapter.UsuarioPendienteAdapter;
import com.google.android.material.snackbar.Snackbar;

/**
 * Tab de usuarios con rol PENDIENTE.
 * Muestra una card por usuario con botones de asignación directa.
 */
public class AdminPendientesFragment extends Fragment {

    private FragmentAdminPendientesBinding binding;
    private UsuarioPendienteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminPendientesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        actualizarEstadoVacio();
    }

    private void setupRecyclerView() {
        adapter = new UsuarioPendienteAdapter(new UsuarioPendienteAdapter.OnPendienteAccionListener() {
            @Override
            public void onAsignarDocente(com.conectatec.ui.admin.usuarios.adapter.UsuarioAdminAdapter.UsuarioDummy u, int pos) {
                // TODO: llamar al repositorio cuando esté implementado
                Snackbar.make(binding.getRoot(),
                        "Asignado como Docente: " + u.nombre, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onAsignarEstudiante(com.conectatec.ui.admin.usuarios.adapter.UsuarioAdminAdapter.UsuarioDummy u, int pos) {
                Snackbar.make(binding.getRoot(),
                        "Asignado como Estudiante: " + u.nombre, Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.rvPendientes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPendientes.setAdapter(adapter);
    }

    private void actualizarEstadoVacio() {
        boolean sinPendientes = adapter.conteo() == 0;
        binding.rvPendientes.setVisibility(sinPendientes ? View.GONE : View.VISIBLE);
        binding.layoutSinPendientes.setVisibility(sinPendientes ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

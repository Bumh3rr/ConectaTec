package com.conectatec.ui.admin.usuarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.conectatec.R;
import com.conectatec.databinding.FragmentAdminUsuariosBinding;
import com.conectatec.ui.admin.usuarios.adapter.UsuarioAdminAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Pantalla principal del módulo de usuarios del Administrador.
 * Incluye buscador, chips de filtro por rol y TabLayout (Todos / Pendientes).
 */
public class AdminUsuariosFragment extends Fragment {

    private FragmentAdminUsuariosBinding binding;
    private UsuarioAdminAdapter adapter;

    // Lista original sin filtrar
    private List<UsuarioAdminAdapter.UsuarioDummy> listaCompleta = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminUsuariosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupTabs();
        setupRecyclerView();
        setupFiltros();
        setupBusqueda();
    }

    private void setupTabs() {
        binding.tabLayoutUsuarios.addTab(
                binding.tabLayoutUsuarios.newTab().setText(getString(R.string.tab_todos)));
        binding.tabLayoutUsuarios.addTab(
                binding.tabLayoutUsuarios.newTab()
                        .setText(getString(R.string.tab_pendientes_admin) + "  3")); // badge dummy

        binding.tabLayoutUsuarios.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    // Mostrar sólo pendientes
                    filtrarPorRol("PENDIENTE");
                } else {
                    filtrarPorRol(null);
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupRecyclerView() {
        adapter = new UsuarioAdminAdapter(new UsuarioAdminAdapter.OnUsuarioClickListener() {
            @Override
            public void onClick(UsuarioAdminAdapter.UsuarioDummy usuario, int position) {
                // Navega al detalle pasando el índice como ID dummy
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_usuarios_to_detalle);
            }

            @Override
            public void onOverflowClick(View anchor, UsuarioAdminAdapter.UsuarioDummy usuario, int position) {
                mostrarPopupMenu(anchor, usuario);
            }
        });

        binding.rvUsuarios.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvUsuarios.setAdapter(adapter);

        // Guardar lista completa para filtros
        listaCompleta = new ArrayList<>(adapter.lista);

        actualizarEstadoVacio();
    }

    private void setupFiltros() {
        binding.chipTodos.setOnClickListener(v      -> filtrarPorRol(null));
        binding.chipPendiente.setOnClickListener(v  -> filtrarPorRol("PENDIENTE"));
        binding.chipDocente.setOnClickListener(v    -> filtrarPorRol("DOCENTE"));
        binding.chipEstudiante.setOnClickListener(v -> filtrarPorRol("ESTUDIANTE"));
        binding.chipAdmin.setOnClickListener(v      -> filtrarPorRol("ADMINISTRADOR"));
    }

    private void setupBusqueda() {
        binding.etBuscar.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(android.text.Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase(Locale.getDefault()).trim();
                List<UsuarioAdminAdapter.UsuarioDummy> filtrada = new ArrayList<>();
                for (UsuarioAdminAdapter.UsuarioDummy u : listaCompleta) {
                    if (u.nombre.toLowerCase(Locale.getDefault()).contains(query)
                            || u.correo.toLowerCase(Locale.getDefault()).contains(query)) {
                        filtrada.add(u);
                    }
                }
                adapter.setLista(filtrada);
                actualizarEstadoVacio();
            }
        });
    }

    private void filtrarPorRol(@Nullable String rol) {
        if (rol == null) {
            adapter.setLista(listaCompleta);
        } else {
            List<UsuarioAdminAdapter.UsuarioDummy> filtrada = new ArrayList<>();
            for (UsuarioAdminAdapter.UsuarioDummy u : listaCompleta) {
                if (u.rol.equals(rol)) filtrada.add(u);
            }
            adapter.setLista(filtrada);
        }
        actualizarEstadoVacio();
    }

    private void mostrarPopupMenu(View anchor, UsuarioAdminAdapter.UsuarioDummy usuario) {
        PopupMenu popup = new PopupMenu(requireContext(), anchor);
        popup.getMenu().add("Cambiar rol");
        popup.getMenu().add(usuario.activo ? "Desactivar cuenta" : "Activar cuenta");
        popup.getMenu().add("Eliminar usuario");
        popup.setOnMenuItemClickListener(item -> {
            Snackbar.make(binding.getRoot(), item.getTitle() + ": " + usuario.nombre,
                    Snackbar.LENGTH_SHORT).show();
            return true;
        });
        popup.show();
    }

    private void actualizarEstadoVacio() {
        boolean vacio = adapter.getItemCount() == 0;
        binding.rvUsuarios.setVisibility(vacio ? View.GONE : View.VISIBLE);
        binding.emptyState.getRoot().setVisibility(vacio ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

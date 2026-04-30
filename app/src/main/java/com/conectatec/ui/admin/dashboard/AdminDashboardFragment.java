package com.conectatec.ui.admin.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentAdminDashboardBinding;

/**
 * Dashboard del Administrador.
 * Muestra 4 tarjetas de resumen con datos dummy.
 * Al pulsar las tarjetas navega a la sección correspondiente.
 */
public class AdminDashboardFragment extends Fragment {

    private FragmentAdminDashboardBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListeners();
    }

    private void setupListeners() {
        binding.cardUsuarios.setOnClickListener(v ->
                navigateToTab(v, R.id.adminUsuariosFragment));

        binding.cardPendientes.setOnClickListener(v ->
                navigateToTab(v, R.id.adminUsuariosFragment));

        binding.cardGrupos.setOnClickListener(v ->
                navigateToTab(v, R.id.adminGruposFragment));

        binding.cardTareas.setOnClickListener(v ->
                navigateToTab(v, R.id.adminActividadesFragment));

        binding.tvVerTodoActividad.setOnClickListener(v ->
                navigateToTab(v, R.id.adminActividadesFragment));
    }

    /** Navega a una pestaña raíz con el mismo comportamiento que el pill nav. */
    private void navigateToTab(View v, int destId) {
        androidx.navigation.NavController nav = Navigation.findNavController(v);
        NavOptions opts = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(nav.getGraph().getStartDestinationId(), false, true)
                .build();
        nav.navigate(destId, null, opts);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

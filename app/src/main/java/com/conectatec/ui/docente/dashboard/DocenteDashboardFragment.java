package com.conectatec.ui.docente.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.data.model.DashboardResumen;
import com.conectatec.databinding.FragmentDocenteDashboardBinding;
import com.conectatec.ui.common.UiState;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteDashboardFragment extends Fragment {

    private FragmentDocenteDashboardBinding binding;
    private DocenteDashboardViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepararAnimacion();
        viewModel = new ViewModelProvider(this).get(DocenteDashboardViewModel.class);
        observeViewModel();
        viewModel.cargarDatos();
        setupListeners();
    }

    private void observeViewModel() {
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            binding.progressBarDashboard.setVisibility(
                    state instanceof UiState.Loading ? View.VISIBLE : View.GONE);
            if (state instanceof UiState.Success) {
                DashboardResumen resumen = ((UiState.Success<DashboardResumen>) state).data;
                binding.tvTotalGruposDashboardDocente.setText(String.valueOf(resumen.gruposActivos));
                binding.tvTotalAlumnos.setText(String.valueOf(resumen.alumnosTotales));
                animarEntrada();
            } else if (state instanceof UiState.Error) {
                Snackbar.make(binding.getRoot(),
                        ((UiState.Error<?>) state).mensaje, Snackbar.LENGTH_LONG).show();
                animarEntrada();
            }
        });
    }

    private void setupListeners() {
        binding.cardGruposActivosDocente.setOnClickListener(v ->
                navigateToTab(v, R.id.docenteGruposFragment));

        binding.cardAlumnosTotales.setOnClickListener(v ->
                navigateToTab(v, R.id.docenteGruposFragment));

        binding.tvVerTodasTareasDashboard.setOnClickListener(v ->
                navigateToTab(v, R.id.docenteTareasFragment));
    }

    private void navigateToTab(View v, int destId) {
        NavController nav = Navigation.findNavController(v);
        NavOptions opts = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(nav.getGraph().getStartDestinationId(), false, true)
                .build();
        nav.navigate(destId, null, opts);
    }

    private void prepararAnimacion() {
        binding.cardBienvenidaDocente.setAlpha(0f);
        binding.layoutKpisDocente.setAlpha(0f);
        binding.cardTareasRecientesDashboard.setAlpha(0f);
        binding.cardActividadHoyDashboard.setAlpha(0f);
    }

    private void animarEntrada() {
        float translY = getResources().getDisplayMetrics().density * 32;
        View[] views = {
                binding.cardBienvenidaDocente,
                binding.layoutKpisDocente,
                binding.cardTareasRecientesDashboard,
                binding.cardActividadHoyDashboard
        };
        for (int i = 0; i < views.length; i++) {
            View card = views[i];
            card.setTranslationY(translY);
            card.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setStartDelay(i * 80L)
                    .setDuration(300)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

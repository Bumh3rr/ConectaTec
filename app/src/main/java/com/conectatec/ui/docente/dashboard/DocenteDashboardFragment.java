package com.conectatec.ui.docente.dashboard;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.data.model.DashboardResumen;
import com.conectatec.databinding.FragmentDocenteDashboardBinding;
import com.conectatec.ui.common.EntradaAnimator;
import com.conectatec.ui.common.UiState;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

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
        viewModel = new ViewModelProvider(this).get(DocenteDashboardViewModel.class);
        observeViewModel();
        viewModel.cargarDatos();
        setupListeners();
        configurarGraficas();
    }

    private void observeViewModel() {
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            binding.progressBarDashboard.setVisibility(
                    state instanceof UiState.Loading ? View.VISIBLE : View.GONE);
            if (state instanceof UiState.Success) {
                EntradaAnimator.animar(
                    binding.cardBienvenidaDocente,
                    binding.layoutKpisDocente,
                    binding.cardDonutEntregas,
                    binding.cardBarrasGrupos,
                    binding.cardProgresoTareas,
                    binding.cardTareasRecientesDashboard
                );
            } else if (state instanceof UiState.Error) {
                Snackbar.make(binding.getRoot(),
                        ((UiState.Error<?>) state).mensaje, Snackbar.LENGTH_LONG).show();
                EntradaAnimator.animar(
                    binding.cardBienvenidaDocente,
                    binding.layoutKpisDocente,
                    binding.cardDonutEntregas,
                    binding.cardBarrasGrupos,
                    binding.cardProgresoTareas,
                    binding.cardTareasRecientesDashboard
                );
            }
        });
    }

    private void setupListeners() {
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

    private void configurarGraficas() {
        configurarDonutEntregas(binding.chartDonutEntregas);
        configurarBarrasGrupos(binding.chartBarrasGrupos);
        configurarProgresoTareas();
    }

    private void configurarDonutEntregas(PieChart chart) {
        // Datos consistentes con TareasRepositoryImpl.DATASET (tareas EN_CURSO)
        int calificadas = 14;
        int entregadas  = 16;
        int pendientes  = 9;
        int total = calificadas + entregadas + pendientes;

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(calificadas));
        entries.add(new PieEntry(entregadas));
        entries.add(new PieEntry(pendientes));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(
            ContextCompat.getColor(requireContext(), R.color.colorSuccess),
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            ContextCompat.getColor(requireContext(), R.color.colorWarning)
        );
        dataSet.setValueTextSize(0f);
        dataSet.setValueTextColor(Color.TRANSPARENT);
        dataSet.setSliceSpace(2f);

        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setHoleColor(Color.TRANSPARENT);
        chart.setTransparentCircleColor(Color.TRANSPARENT);

        int pct = total > 0 ? ((calificadas + entregadas) * 100 / total) : 0;
        chart.setCenterText(pct + "%");
        chart.setCenterTextSize(18f);
        chart.setCenterTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnSurface));
        chart.setCenterTextTypeface(android.graphics.Typeface.DEFAULT_BOLD);

        chart.getDescription().setEnabled(false);
        chart.setDrawBorders(false);
        chart.setTouchEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setDrawEntryLabels(false);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.animateY(800, Easing.EaseInOutQuad);
        chart.invalidate();

        // Actualizar leyenda manual
        binding.tvLeyendaCalificadas.setText("Calificadas  " + calificadas);
        binding.tvLeyendaEntregadas.setText("Entregadas  " + entregadas);
        binding.tvLeyendaPendientes.setText("Pendientes  " + pendientes);
    }

    private void configurarBarrasGrupos(BarChart chart) {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 18f)); // PM 6A
        entries.add(new BarEntry(1f, 16f)); // BD 4B
        entries.add(new BarEntry(2f, 13f)); // Cálculo 2A

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(
            ContextCompat.getColor(requireContext(), R.color.colorChipDocente),
            ContextCompat.getColor(requireContext(), R.color.colorChipEstudiante),
            ContextCompat.getColor(requireContext(), R.color.colorChipAdmin)
        );
        dataSet.setValueTextSize(11f);
        dataSet.setValueTextColor(
            ContextCompat.getColor(requireContext(), R.color.colorOnSurface));

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);
        chart.setData(data);

        String[] grupos = {"PM 6A", "BD 4B", "Cálculo"};
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(grupos));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.colorOnSurfaceVariant));
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(false);

        chart.getAxisLeft().setTextColor(
            ContextCompat.getColor(requireContext(), R.color.colorOnSurfaceVariant));
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisLeft().setAxisMaximum(22f);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisRight().setEnabled(false);

        chart.getDescription().setEnabled(false);
        chart.setDrawBorders(false);
        chart.setTouchEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setFitBars(true);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.animateY(700, Easing.EaseInOutQuad);
        chart.invalidate();
    }

    private void configurarProgresoTareas() {
        // Datos: primeras 3 tareas EN_CURSO de TareasRepositoryImpl.DATASET
        // Tarea 1: Práctica 1: Layouts, 12/18 (67%) → colorPrimary
        // Tarea 3: Proyecto Final,       5/18 (28%) → colorWarning
        // Tarea 4: Trabajo investigación,8/18 (44%) → colorPrimary
        setProgreso(binding.pbProgreso1, 12, 18);
        setProgreso(binding.pbProgreso2,  5, 18);
        setProgreso(binding.pbProgreso3,  8, 18);
    }

    private void setProgreso(android.widget.ProgressBar pb, int entregadas, int total) {
        int colorRes;
        int pct = total > 0 ? (entregadas * 100 / total) : 0;
        if      (pct >= 70) colorRes = R.color.colorSuccess;
        else if (pct >= 40) colorRes = R.color.colorPrimary;
        else                colorRes = R.color.colorWarning;
        pb.setMax(total);
        pb.setProgress(entregadas);
        pb.setProgressTintList(ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), colorRes)));
        pb.setProgressBackgroundTintList(ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.colorSurfaceVariant)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

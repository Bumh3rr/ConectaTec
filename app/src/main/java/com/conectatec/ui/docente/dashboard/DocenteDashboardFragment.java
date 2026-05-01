package com.conectatec.ui.docente.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

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
import com.conectatec.ui.common.UiState;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

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
        setupCharts();
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

    private void setupCharts() {
        // TODO: Reemplazar con viewModel.getResumenDocente().observe(...)
        setupDonutTipos();
        setupDonutCalif();
        setupBarraAlumnos();
        setupBarraGrupos();
    }

    private void setupDonutTipos() {
        int colorBlue   = ContextCompat.getColor(requireContext(), R.color.db_blue);
        int colorGreen  = ContextCompat.getColor(requireContext(), R.color.db_green);
        int colorPurple = ContextCompat.getColor(requireContext(), R.color.db_purple);
        int colorOrange = ContextCompat.getColor(requireContext(), R.color.db_orange);
        int colorBg     = ContextCompat.getColor(requireContext(), R.color.db_background);
        int colorText   = ContextCompat.getColor(requireContext(), R.color.db_text_primary);

        // TODO: Reemplazar con viewModel.getResumenDocente().observe(...)
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(4, ""));
        entries.add(new PieEntry(2, ""));
        entries.add(new PieEntry(1, ""));
        entries.add(new PieEntry(1, ""));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colorBlue, colorGreen, colorPurple, colorOrange);
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(2f);

        applyDonutStyle(binding.chartDonutTiposDocente, new PieData(dataSet), "8\ntareas",
                13f, colorText, colorBg, 40f, 45f);
    }

    private void setupDonutCalif() {
        int colorGreen = ContextCompat.getColor(requireContext(), R.color.db_green);
        int colorRed   = ContextCompat.getColor(requireContext(), R.color.db_red);
        int colorBg    = ContextCompat.getColor(requireContext(), R.color.db_background);
        int colorText  = ContextCompat.getColor(requireContext(), R.color.db_text_primary);

        // TODO: Reemplazar con viewModel.getResumenDocente().observe(...)
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(22, ""));
        entries.add(new PieEntry(5,  ""));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colorGreen, colorRed);
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(2f);

        applyDonutStyle(binding.chartDonutCalifDocente, new PieData(dataSet), "81%\ncalificadas",
                13f, colorText, colorBg, 55f, 60f);
    }

    private void setupBarraAlumnos() {
        int colorPurple = ContextCompat.getColor(requireContext(), R.color.db_purple);
        int colorMuted  = ContextCompat.getColor(requireContext(), R.color.db_text_muted);
        int colorText   = ContextCompat.getColor(requireContext(), R.color.db_text_primary);

        // TODO: Reemplazar con viewModel.getTopAlumnos().observe(...)
        String[] nombres = {"D. Morales", "A. Ramírez", "J. Martínez", "L. Torres", "M. López"};

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 5));
        entries.add(new BarEntry(1, 6));
        entries.add(new BarEntry(2, 6));
        entries.add(new BarEntry(3, 7));
        entries.add(new BarEntry(4, 8));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColor(colorPurple);
        dataSet.setDrawValues(true);
        dataSet.setValueTextColor(colorText);
        dataSet.setValueTextSize(11f);

        applyBarraStyle(binding.chartBarraAlumnos, new BarData(dataSet), nombres, colorMuted);
    }

    private void setupBarraGrupos() {
        int colorBlue  = ContextCompat.getColor(requireContext(), R.color.db_blue);
        int colorMuted = ContextCompat.getColor(requireContext(), R.color.db_text_muted);
        int colorText  = ContextCompat.getColor(requireContext(), R.color.db_text_primary);

        // TODO: Reemplazar con viewModel.getActividadPorGrupo().observe(...)
        String[] grupos = {"Cálculo 2A", "BD 4B", "Prog. Móvil 6A"};

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 12));
        entries.add(new BarEntry(1, 18));
        entries.add(new BarEntry(2, 24));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColor(colorBlue);
        dataSet.setDrawValues(true);
        dataSet.setValueTextColor(colorText);
        dataSet.setValueTextSize(11f);

        applyBarraStyle(binding.chartBarraGrupos, new BarData(dataSet), grupos, colorMuted);
    }

    private void applyDonutStyle(PieChart chart, PieData data, String centerText,
                                 float centerTextSize, int centerTextColor,
                                 int holeColor, float holeRadius, float transparentRadius) {
        Description desc = new Description();
        desc.setEnabled(false);
        chart.setDescription(desc);
        chart.getLegend().setEnabled(false);
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(holeRadius);
        chart.setTransparentCircleRadius(transparentRadius);
        chart.setHoleColor(holeColor);
        chart.setTransparentCircleColor(holeColor);
        chart.setTransparentCircleAlpha(110);
        chart.setCenterText(centerText);
        chart.setCenterTextSize(centerTextSize);
        chart.setCenterTextColor(centerTextColor);
        chart.setRotationEnabled(false);
        chart.setTouchEnabled(false);
        chart.setDrawEntryLabels(false);
        chart.setData(data);
        chart.animateY(600);
        chart.invalidate();
    }

    private void configurarPieChart(PieChart chart, String centerText) {
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(50f);
        chart.setTransparentCircleRadius(55f);
        chart.setHoleColor(Color.parseColor("#0D1117"));
        chart.setTransparentCircleColor(Color.parseColor("#0D1117"));
        chart.setTransparentCircleAlpha(80);
        chart.setCenterText(centerText);
        chart.setCenterTextColor(Color.parseColor("#E6EDF3"));
        chart.setCenterTextSize(14f);
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(false);
        chart.setDrawEntryLabels(false);
        chart.setUsePercentValues(false);
        chart.animateY(600);
    }

    private void applyBarraStyle(HorizontalBarChart chart, BarData data,
                                 String[] labels, int labelColor) {
        Description desc = new Description();
        desc.setEnabled(false);
        chart.setDescription(desc);
        chart.getLegend().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.setFitBars(true);
        chart.setTouchEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(false);

        chart.getAxisRight().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(labelColor);
        xAxis.setTextSize(11f);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.length);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        // CLAVE: espacio suficiente para labels largos
        chart.setExtraOffsets(80f, 8f, 20f, 8f); // left, top, right, bottom

        data.setBarWidth(0.5f);
        chart.setData(data);
        chart.invalidate();
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

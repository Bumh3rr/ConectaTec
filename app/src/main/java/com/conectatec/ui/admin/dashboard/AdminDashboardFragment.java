package com.conectatec.ui.admin.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentAdminDashboardBinding;
import com.conectatec.ui.common.EntradaAnimator;
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

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
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
        configurarGraficas();
        EntradaAnimator.animar(
            binding.cardUsuarios,
            binding.cardPendientes,
            binding.cardGrupos,
            binding.cardTareas,
            binding.cardDonutUsuarios,
            binding.cardBarrasActividades
        );
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
    }

    private void configurarGraficas() {
        configurarDonutUsuarios(binding.chartDonutUsuarios);
        configurarBarrasActividades(binding.chartBarrasActividades);
    }

    private void configurarDonutUsuarios(PieChart chart) {
        // Datos de UsuarioAdminAdapter: 1 ADMIN, 2 DOCENTE, 2 ESTUDIANTE, 2 PENDIENTE
        int admin      = 1;
        int docente    = 2;
        int estudiante = 2;
        int pendiente  = 2;
        int total = admin + docente + estudiante + pendiente;

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(admin));
        entries.add(new PieEntry(docente));
        entries.add(new PieEntry(estudiante));
        entries.add(new PieEntry(pendiente));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(
            ContextCompat.getColor(requireContext(), R.color.colorChipAdmin),
            ContextCompat.getColor(requireContext(), R.color.colorChipDocente),
            ContextCompat.getColor(requireContext(), R.color.colorChipEstudiante),
            ContextCompat.getColor(requireContext(), R.color.colorChipPendiente)
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
        chart.setCenterText(String.valueOf(total));
        chart.setCenterTextSize(18f);
        chart.setCenterTextColor(
            ContextCompat.getColor(requireContext(), R.color.colorOnSurface));
        chart.setCenterTextTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setDrawEntryLabels(false);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.animateY(800, Easing.EaseInOutQuad);
        chart.invalidate();

        // Leyenda manual
        binding.tvLeyendaAdmin.setText("Admin  " + admin);
        binding.tvLeyendaDocente.setText("Docente  " + docente);
        binding.tvLeyendaEstudiante.setText("Estudiante  " + estudiante);
        binding.tvLeyendaPendiente.setText("Pendiente  " + pendiente);
    }

    private void configurarBarrasActividades(BarChart chart) {
        // Datos de ActividadAdminAdapter: EN_CURSO=3, COMPLETADA=2, PENDIENTE=2, VENCIDA=1
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 3f)); // EN_CURSO
        entries.add(new BarEntry(1f, 2f)); // COMPLETADA
        entries.add(new BarEntry(2f, 2f)); // PENDIENTE
        entries.add(new BarEntry(3f, 1f)); // VENCIDA

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            ContextCompat.getColor(requireContext(), R.color.colorSuccess),
            ContextCompat.getColor(requireContext(), R.color.colorWarning),
            ContextCompat.getColor(requireContext(), R.color.colorError)
        );
        dataSet.setValueTextSize(11f);
        dataSet.setValueTextColor(
            ContextCompat.getColor(requireContext(), R.color.colorOnSurface));

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);
        chart.setData(data);

        String[] estados = {"En Curso", "Completada", "Pendiente", "Vencida"};
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(estados));
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
        chart.getAxisLeft().setAxisMaximum(5f);
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

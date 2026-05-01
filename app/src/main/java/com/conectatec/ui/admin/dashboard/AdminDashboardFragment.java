package com.conectatec.ui.admin.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.conectatec.R;
import com.conectatec.databinding.FragmentAdminDashboardBinding;
import com.conectatec.ui.dashboard.ChartCarouselAdapter;
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
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.Arrays;
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
        setupCharts();

        // Carrusel de usuarios
        View pageRoles = construirPaginaRoles();
        View pageFotos = construirPaginaFotosIA();
        configurarCarrusel(binding.vpChartUsuarios, binding.dotsUsuarios,
                Arrays.asList(pageRoles, pageFotos));

        // Carrusel de tareas
        View pageTipos = construirPaginaTiposTareas();
        View pageCalif = construirPaginaCalificadas();
        configurarCarrusel(binding.vpChartTareas, binding.dotsTareas,
                Arrays.asList(pageTipos, pageCalif));
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

    private void setupCharts() {
        // TODO: llamar a AdminDashboardService.getResumen() para datos reales
        setupDonutRoles();
        setupDonutTipos();
        setupDonutCalif();
        setupBarraEstados();
        setupDonutFotos();
    }

    // ── Carousel ─────────────────────────────────────────────────────────────

    private void configurarCarrusel(ViewPager2 viewPager, DotsIndicator dotsIndicator,
                                    List<View> paginas) {
        int peekWidth = (int) getResources().getDimension(R.dimen.carousel_peek_width);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setPageTransformer((page, position) -> {
            float offset = peekWidth * position;
            page.setTranslationX(-offset);
        });
        ChartCarouselAdapter adapter = new ChartCarouselAdapter(paginas);
        viewPager.setAdapter(adapter);
        dotsIndicator.attachTo(viewPager);
    }

    private View construirPaginaRoles() {
        LinearLayout page = crearContenedorPagina();
        PieChart chart = crearPieChartBase();
        int colorBlue   = ContextCompat.getColor(requireContext(), R.color.db_blue);
        int colorGreen  = ContextCompat.getColor(requireContext(), R.color.db_green);
        int colorRed    = ContextCompat.getColor(requireContext(), R.color.db_red);
        int colorPurple = ContextCompat.getColor(requireContext(), R.color.db_purple);
        int colorBg     = ContextCompat.getColor(requireContext(), R.color.db_background);
        int colorText   = ContextCompat.getColor(requireContext(), R.color.db_text_primary);

        // TODO: Reemplazar con viewModel.getResumenAdmin().observe(...)
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(31, ""));
        entries.add(new PieEntry(9,  ""));
        entries.add(new PieEntry(4,  ""));
        entries.add(new PieEntry(3,  ""));

        PieDataSet ds = new PieDataSet(entries, "");
        ds.setColors(colorBlue, colorGreen, colorRed, colorPurple);
        ds.setDrawValues(false);
        ds.setSliceSpace(2f);
        applyDonutStyle(chart, new PieData(ds), "47\nusuarios", 14f, colorText, colorBg, 50f, 55f);
        page.addView(chart);
        return page;
    }

    private View construirPaginaFotosIA() {
        LinearLayout page = crearContenedorPagina();
        PieChart chart = crearPieChartBase();
        int colorGreen = ContextCompat.getColor(requireContext(), R.color.db_green);
        int colorRed   = ContextCompat.getColor(requireContext(), R.color.db_red);
        int colorDim   = ContextCompat.getColor(requireContext(), R.color.db_dim);
        int colorBg    = ContextCompat.getColor(requireContext(), R.color.db_background);
        int colorText  = ContextCompat.getColor(requireContext(), R.color.db_text_primary);

        // TODO: Reemplazar con viewModel.getResumenAdmin().observe(...)
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(33, ""));
        entries.add(new PieEntry(5,  ""));
        entries.add(new PieEntry(9,  ""));

        PieDataSet ds = new PieDataSet(entries, "");
        ds.setColors(colorGreen, colorRed, colorDim);
        ds.setDrawValues(false);
        ds.setSliceSpace(2f);
        applyDonutStyle(chart, new PieData(ds), "70%", 14f, colorText, colorBg, 50f, 55f);
        page.addView(chart);
        return page;
    }

    private View construirPaginaTiposTareas() {
        LinearLayout page = crearContenedorPagina();
        PieChart chart = crearPieChartBase();
        int colorBlue   = ContextCompat.getColor(requireContext(), R.color.db_blue);
        int colorGreen  = ContextCompat.getColor(requireContext(), R.color.db_green);
        int colorPurple = ContextCompat.getColor(requireContext(), R.color.db_purple);
        int colorOrange = ContextCompat.getColor(requireContext(), R.color.db_orange);
        int colorBg     = ContextCompat.getColor(requireContext(), R.color.db_background);
        int colorText   = ContextCompat.getColor(requireContext(), R.color.db_text_primary);

        // TODO: Reemplazar con viewModel.getResumenAdmin().observe(...)
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(44, ""));
        entries.add(new PieEntry(24, ""));
        entries.add(new PieEntry(14, ""));
        entries.add(new PieEntry(7,  ""));

        PieDataSet ds = new PieDataSet(entries, "");
        ds.setColors(colorBlue, colorGreen, colorPurple, colorOrange);
        ds.setDrawValues(false);
        ds.setSliceSpace(2f);
        applyDonutStyle(chart, new PieData(ds), "89\ntareas", 13f, colorText, colorBg, 40f, 45f);
        page.addView(chart);
        return page;
    }

    private View construirPaginaCalificadas() {
        LinearLayout page = crearContenedorPagina();
        PieChart chart = crearPieChartBase();
        int colorGreen = ContextCompat.getColor(requireContext(), R.color.db_green);
        int colorRed   = ContextCompat.getColor(requireContext(), R.color.db_red);
        int colorBg    = ContextCompat.getColor(requireContext(), R.color.db_background);
        int colorText  = ContextCompat.getColor(requireContext(), R.color.db_text_primary);

        // TODO: Reemplazar con viewModel.getResumenAdmin().observe(...)
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(48, ""));
        entries.add(new PieEntry(41, ""));

        PieDataSet ds = new PieDataSet(entries, "");
        ds.setColors(colorGreen, colorRed);
        ds.setDrawValues(false);
        ds.setSliceSpace(2f);
        applyDonutStyle(chart, new PieData(ds), "54%\ncalificadas", 13f, colorText, colorBg, 55f, 60f);
        page.addView(chart);
        return page;
    }

    private LinearLayout crearContenedorPagina() {
        LinearLayout page = new LinearLayout(requireContext());
        page.setOrientation(LinearLayout.VERTICAL);
        return page;
    }

    private PieChart crearPieChartBase() {
        PieChart chart = new PieChart(requireContext());
        int height = (int) getResources().getDimension(R.dimen.carousel_chart_height);
        chart.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, height));
        return chart;
    }

    // ── Charts directos (preservados) ────────────────────────────────────────

    private void setupDonutRoles() {
        int colorBlue   = ContextCompat.getColor(requireContext(), R.color.db_blue);
        int colorGreen  = ContextCompat.getColor(requireContext(), R.color.db_green);
        int colorRed    = ContextCompat.getColor(requireContext(), R.color.db_red);
        int colorPurple = ContextCompat.getColor(requireContext(), R.color.db_purple);
        int colorBg     = ContextCompat.getColor(requireContext(), R.color.db_background);
        int colorText   = ContextCompat.getColor(requireContext(), R.color.db_text_primary);

        // TODO: Reemplazar con viewModel.getResumenAdmin().observe(...)
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(31, ""));
        entries.add(new PieEntry(9,  ""));
        entries.add(new PieEntry(4,  ""));
        entries.add(new PieEntry(3,  ""));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colorBlue, colorGreen, colorRed, colorPurple);
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(2f);

        applyDonutStyle(binding.chartDonutRoles, new PieData(dataSet), "47\nusuarios",
                14f, colorText, colorBg, 50f, 55f);
    }

    private void setupDonutTipos() {
        int colorBlue   = ContextCompat.getColor(requireContext(), R.color.db_blue);
        int colorGreen  = ContextCompat.getColor(requireContext(), R.color.db_green);
        int colorPurple = ContextCompat.getColor(requireContext(), R.color.db_purple);
        int colorOrange = ContextCompat.getColor(requireContext(), R.color.db_orange);
        int colorBg     = ContextCompat.getColor(requireContext(), R.color.db_background);
        int colorText   = ContextCompat.getColor(requireContext(), R.color.db_text_primary);

        // TODO: Reemplazar con viewModel.getResumenAdmin().observe(...)
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(44, ""));
        entries.add(new PieEntry(24, ""));
        entries.add(new PieEntry(14, ""));
        entries.add(new PieEntry(7,  ""));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colorBlue, colorGreen, colorPurple, colorOrange);
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(2f);

        applyDonutStyle(binding.chartDonutTipos, new PieData(dataSet), "89\ntareas",
                13f, colorText, colorBg, 40f, 45f);
    }

    private void setupDonutCalif() {
        int colorGreen = ContextCompat.getColor(requireContext(), R.color.db_green);
        int colorRed   = ContextCompat.getColor(requireContext(), R.color.db_red);
        int colorBg    = ContextCompat.getColor(requireContext(), R.color.db_background);
        int colorText  = ContextCompat.getColor(requireContext(), R.color.db_text_primary);

        // TODO: Reemplazar con viewModel.getResumenAdmin().observe(...)
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(48, ""));
        entries.add(new PieEntry(41, ""));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colorGreen, colorRed);
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(2f);

        applyDonutStyle(binding.chartDonutCalif, new PieData(dataSet), "54%\ncalificadas",
                13f, colorText, colorBg, 55f, 60f);
    }

    private void setupBarraEstados() {
        int colorGreen = ContextCompat.getColor(requireContext(), R.color.db_green);
        int colorBlue  = ContextCompat.getColor(requireContext(), R.color.db_blue);
        int colorRed   = ContextCompat.getColor(requireContext(), R.color.db_red);
        int colorDim   = ContextCompat.getColor(requireContext(), R.color.db_dim);
        int colorMuted = ContextCompat.getColor(requireContext(), R.color.db_text_muted);
        int colorText  = ContextCompat.getColor(requireContext(), R.color.db_text_primary);

        // TODO: Reemplazar con viewModel.getResumenAdmin().observe(...)
        String[] labels = {"Borrador", "Sin Entregar", "Entregada", "Calificada"};

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 7));
        entries.add(new BarEntry(1, 8));
        entries.add(new BarEntry(2, 26));
        entries.add(new BarEntry(3, 48));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(colorDim, colorRed, colorBlue, colorGreen);
        dataSet.setDrawValues(true);
        dataSet.setValueTextColor(colorText);
        dataSet.setValueTextSize(11f);

        applyBarraStyle(binding.chartBarraEstados, new BarData(dataSet), labels, colorMuted);
    }

    private void setupDonutFotos() {
        int colorGreen  = ContextCompat.getColor(requireContext(), R.color.db_green);
        int colorRed    = ContextCompat.getColor(requireContext(), R.color.db_red);
        int colorDim    = ContextCompat.getColor(requireContext(), R.color.db_dim);
        int colorBg     = ContextCompat.getColor(requireContext(), R.color.db_background);
        int colorText   = ContextCompat.getColor(requireContext(), R.color.db_text_primary);

        // TODO: Reemplazar con viewModel.getResumenAdmin().observe(...)
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(33, ""));
        entries.add(new PieEntry(5,  ""));
        entries.add(new PieEntry(9,  ""));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colorGreen, colorRed, colorDim);
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(2f);

        applyDonutStyle(binding.chartDonutFotos, new PieData(dataSet), "70%",
                14f, colorText, colorBg, 50f, 55f);
    }

    // ── Helpers de estilo ────────────────────────────────────────────────────

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

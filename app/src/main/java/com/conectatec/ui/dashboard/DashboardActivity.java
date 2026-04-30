package com.conectatec.ui.dashboard;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.conectatec.R;
import com.conectatec.databinding.ActivityDashboardBinding;
import com.conectatec.databinding.ItemKpiCardBinding;
import com.conectatec.model.DashboardStats;
import com.conectatec.model.DocenteStats;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private DashboardViewModel viewModel;
    private ActividadAdapter actividadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        actividadAdapter = new ActividadAdapter();

        binding.rvActividad.setLayoutManager(new LinearLayoutManager(this));
        binding.rvActividad.setItemAnimator(new DefaultItemAnimator());
        binding.rvActividad.setAdapter(actividadAdapter);

        String token = getSharedPreferences("tecconnect_prefs", MODE_PRIVATE)
                .getString("jwt_token", "");
        String[] claims = extraerClaims(token);
        binding.tvNombre.setText(claims[0]);
        binding.tvRol.setText(claims[1]);

        Glide.with(this)
                .load((Object) null)
                .placeholder(R.drawable.bg_avatar_placeholder)
                .into(binding.ivAvatar);

        if ("ADMINISTRADOR".equalsIgnoreCase(claims[1])) {
            viewModel.adminStats.observe(this, this::setupAdminDashboard);
            viewModel.actividadReciente.observe(this, actividadAdapter::setLista);
            viewModel.cargarDatosAdmin(token);
        } else {
            viewModel.docenteStats.observe(this, this::setupDocenteDashboard);
            viewModel.actividadReciente.observe(this, actividadAdapter::setLista);
            viewModel.cargarDatosDocente(token);
        }
    }

    private void setupAdminDashboard(DashboardStats stats) {
        binding.cardChartAdmin.setVisibility(View.VISIBLE);
        binding.cardChartDocente.setVisibility(View.GONE);

        int[] valores   = {stats.usuariosTotales, stats.docentesActivos, stats.gruposActivos,
                           stats.tareasPublicadas, stats.entregasPendientes, stats.mensajesHoy};
        String[] labels = {"Usuarios Totales", "Docentes Activos", "Grupos Activos",
                           "Tareas Publicadas", "Pend. de Revisar", "Mensajes Hoy"};
        int[] iconos    = {R.drawable.ic_people, R.drawable.ic_school, R.drawable.ic_group,
                           R.drawable.ic_assignment, R.drawable.ic_pending, R.drawable.ic_chat};
        int[] colores   = {Color.parseColor("#6C63FF"), Color.parseColor("#10B981"),
                           Color.parseColor("#F59E0B"), Color.parseColor("#EF4444"),
                           Color.parseColor("#3B82F6"), Color.parseColor("#8B5CF6")};

        binding.linearKpis.removeAllViews();
        for (int i = 0; i < 6; i++) {
            agregarKpiCard(String.valueOf(valores[i]), labels[i], iconos[i], colores[i]);
        }
        animarKpiCards();
        configurarBarChart(stats);
    }

    private void setupDocenteDashboard(DocenteStats stats) {
        binding.cardChartAdmin.setVisibility(View.GONE);
        binding.cardChartDocente.setVisibility(View.VISIBLE);

        String[] valores = {
            String.valueOf(stats.misGrupos), String.valueOf(stats.tareasActivas),
            String.valueOf(stats.entregasHoy), String.valueOf(stats.pendientesCalificar),
            String.valueOf(stats.alumnosTotales), String.format("%.1f", stats.promedioGeneral)
        };
        String[] labels  = {"Mis Grupos", "Tareas Activas", "Entregas Hoy",
                            "Pend. Calificar", "Alumnos Totales", "Promedio General"};
        int[] iconos = {R.drawable.ic_class, R.drawable.ic_assignment, R.drawable.ic_inbox,
                        R.drawable.ic_grading, R.drawable.ic_people, R.drawable.ic_bar_chart};
        int[] colores = {Color.parseColor("#6C63FF"), Color.parseColor("#10B981"),
                         Color.parseColor("#F59E0B"), Color.parseColor("#EF4444"),
                         Color.parseColor("#3B82F6"), Color.parseColor("#8B5CF6")};

        binding.linearKpis.removeAllViews();
        for (int i = 0; i < 6; i++) {
            agregarKpiCard(valores[i], labels[i], iconos[i], colores[i]);
        }
        animarKpiCards();
        configurarLineChart(stats);
    }

    private void agregarKpiCard(String valor, String etiqueta, int iconoRes, int color) {
        ItemKpiCardBinding card = ItemKpiCardBinding.inflate(
                getLayoutInflater(), binding.linearKpis, false);
        card.tvKpiValue.setText(valor);
        card.tvKpiLabel.setText(etiqueta);
        card.ivKpiIcon.setImageResource(iconoRes);
        card.ivKpiIcon.setImageTintList(ColorStateList.valueOf(color));
        card.viewStrip.setBackgroundColor(color);
        binding.linearKpis.addView(card.getRoot());
    }

    private void animarKpiCards() {
        for (int i = 0; i < binding.linearKpis.getChildCount(); i++) {
            View card = binding.linearKpis.getChildAt(i);
            card.setAlpha(0f);
            card.setTranslationY(30f);
            ObjectAnimator fade  = ObjectAnimator.ofFloat(card, View.ALPHA, 0f, 1f);
            ObjectAnimator slide = ObjectAnimator.ofFloat(card, View.TRANSLATION_Y, 30f, 0f);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(fade, slide);
            set.setDuration(300);
            set.setStartDelay((long) i * 80);
            set.start();
        }
    }

    private void configurarBarChart(DashboardStats stats) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            entries.add(new BarEntry(i, stats.usuariosPorRol[i]));
        }
        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(Color.parseColor("#10B981"),
                          Color.parseColor("#6C63FF"),
                          Color.parseColor("#F59E0B"));
        dataSet.setDrawValues(false);
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.6f);
        binding.barChartAdmin.setData(data);
        binding.barChartAdmin.getDescription().setEnabled(false);
        binding.barChartAdmin.getLegend().setEnabled(false);
        binding.barChartAdmin.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.barChartAdmin.getXAxis().setValueFormatter(
                new IndexAxisValueFormatter(new String[]{"DOCENTE", "ESTUDIANTE", "PENDIENTE"}));
        binding.barChartAdmin.getXAxis().setGranularity(1f);
        binding.barChartAdmin.getXAxis().setDrawGridLines(false);
        binding.barChartAdmin.getAxisLeft().setDrawGridLines(false);
        binding.barChartAdmin.getAxisRight().setEnabled(false);
        binding.barChartAdmin.animateY(1000);
    }

    private void configurarLineChart(DocenteStats stats) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            entries.add(new Entry(i, stats.entregasPorDia[i]));
        }
        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(50);
        dataSet.setColor(Color.parseColor("#6C63FF"));
        dataSet.setFillColor(Color.parseColor("#6C63FF"));
        dataSet.setCircleColor(Color.parseColor("#6C63FF"));
        dataSet.setLineWidth(2f);
        dataSet.setDrawValues(false);
        LineData data = new LineData(dataSet);
        binding.lineChartDocente.setData(data);
        String[] dias = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        binding.lineChartDocente.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.lineChartDocente.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dias));
        binding.lineChartDocente.getXAxis().setGranularity(1f);
        binding.lineChartDocente.getXAxis().setDrawGridLines(false);
        binding.lineChartDocente.getDescription().setEnabled(false);
        binding.lineChartDocente.getLegend().setEnabled(false);
        binding.lineChartDocente.getAxisLeft().setDrawGridLines(false);
        binding.lineChartDocente.getAxisRight().setEnabled(false);
        binding.lineChartDocente.animateY(800);
    }

    private String[] extraerClaims(String jwt) {
        String[] result = {"Usuario", "DOCENTE"};
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) return result;
            byte[] decoded = android.util.Base64.decode(
                    parts[1], android.util.Base64.URL_SAFE | android.util.Base64.NO_PADDING);
            String payload = new String(decoded, "UTF-8");
            JSONObject json = new JSONObject(payload);
            result[0] = json.optString("nombre", "Usuario");
            result[1] = json.optString("rol", "DOCENTE");
        } catch (Exception ignored) {}
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

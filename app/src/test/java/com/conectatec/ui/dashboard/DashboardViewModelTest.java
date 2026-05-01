package com.conectatec.ui.dashboard;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.conectatec.model.ActividadItem;
import com.conectatec.model.DashboardStats;
import com.conectatec.model.DocenteStats;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DashboardViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private DashboardViewModel viewModel;

    @Before
    public void setup() {
        viewModel = new DashboardViewModel();
    }

    @Test
    public void cargarDatosAdmin_populatesAdminStats() {
        viewModel.cargarDatosAdmin("");
        DashboardStats stats = viewModel.adminStats.getValue();
        assertNotNull(stats);
        assertTrue(stats.usuariosTotales > 0);
        assertNotNull(stats.usuariosPorRol);
        assertEquals(3, stats.usuariosPorRol.length);
        assertNotNull(stats.entregasPorDia);
        assertEquals(7, stats.entregasPorDia.length);
    }

    @Test
    public void cargarDatosAdmin_populatesActividadReciente() {
        viewModel.cargarDatosAdmin("");
        List<ActividadItem> actividad = viewModel.actividadReciente.getValue();
        assertNotNull(actividad);
        assertFalse(actividad.isEmpty());
        for (ActividadItem item : actividad) {
            assertNotNull(item.titulo);
            assertNotNull(item.descripcion);
            assertNotNull(item.timestamp);
        }
    }

    @Test
    public void cargarDatosDocente_populatesDocenteStats() {
        viewModel.cargarDatosDocente("");
        DocenteStats stats = viewModel.docenteStats.getValue();
        assertNotNull(stats);
        assertTrue(stats.misGrupos > 0);
        assertTrue(stats.promedioGeneral > 0f);
        assertNotNull(stats.entregasPorDia);
        assertEquals(7, stats.entregasPorDia.length);
    }

    @Test
    public void cargarDatosDocente_populatesActividadReciente() {
        viewModel.cargarDatosDocente("");
        List<ActividadItem> actividad = viewModel.actividadReciente.getValue();
        assertNotNull(actividad);
        assertFalse(actividad.isEmpty());
    }
}

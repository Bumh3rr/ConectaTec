package com.conectatec.ui.dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.conectatec.model.ActividadItem;
import com.conectatec.model.DashboardStats;
import com.conectatec.model.DocenteStats;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {

    public final MutableLiveData<DashboardStats> adminStats = new MutableLiveData<>();
    public final MutableLiveData<DocenteStats> docenteStats = new MutableLiveData<>();
    public final MutableLiveData<List<ActividadItem>> actividadReciente = new MutableLiveData<>();

    public void cargarDatosAdmin(String token) {
        // TODO: llamar a DashboardService.obtenerEstadisticasAdmin()
        DashboardStats stats = new DashboardStats();
        stats.usuariosTotales    = 142;
        stats.docentesActivos    = 18;
        stats.gruposActivos      = 24;
        stats.tareasPublicadas   = 67;
        stats.entregasPendientes = 9;
        stats.mensajesHoy        = 31;
        stats.usuariosPorRol     = new int[]{18, 105, 19};
        stats.entregasPorDia     = new int[]{12, 8, 15, 6, 20, 10, 14};
        adminStats.setValue(stats);

        List<ActividadItem> actividad = new ArrayList<>();
        actividad.add(new ActividadItem("María González",
                "Se registró como estudiante", "09:14",
                ActividadItem.TIPO_USUARIO, 0xFF6C63FF));
        actividad.add(new ActividadItem("Prog. Móvil 6A",
                "Tarea: Proyecto Final publicada", "08:45",
                ActividadItem.TIPO_TAREA, 0xFF10B981));
        actividad.add(new ActividadItem("Carlos Ruiz",
                "Entrega enviada: Actividad 3", "08:30",
                ActividadItem.TIPO_ENTREGA, 0xFFF59E0B));
        actividad.add(new ActividadItem("Bases de Datos 4B",
                "Grupo actualizado por docente", "07:55",
                ActividadItem.TIPO_GRUPO, 0xFFEF4444));
        actividad.add(new ActividadItem("Soporte",
                "Nuevo mensaje de usuario pendiente", "07:20",
                ActividadItem.TIPO_MENSAJE, 0xFF3B82F6));
        actividad.add(new ActividadItem("Sistema",
                "3 usuarios pendientes de aprobación", "06:00",
                ActividadItem.TIPO_AVISO, 0xFF8B5CF6));
        actividadReciente.setValue(actividad);
    }

    public void cargarDatosDocente(String token) {
        // TODO: llamar a DashboardService.obtenerEstadisticasDocente()
        DocenteStats stats = new DocenteStats();
        stats.misGrupos           = 3;
        stats.tareasActivas       = 8;
        stats.entregasHoy         = 5;
        stats.pendientesCalificar = 12;
        stats.alumnosTotales      = 47;
        stats.promedioGeneral     = 8.2f;
        stats.entregasPorDia      = new int[]{4, 7, 2, 9, 5, 3, 8};
        docenteStats.setValue(stats);

        List<ActividadItem> actividad = new ArrayList<>();
        actividad.add(new ActividadItem("Ana López",
                "Entregó: Actividad 3 — Prog. Móvil 6A", "10:05",
                ActividadItem.TIPO_ENTREGA, 0xFF6C63FF));
        actividad.add(new ActividadItem("Prog. Móvil 6A",
                "Tarea Proyecto Final publicada", "09:30",
                ActividadItem.TIPO_TAREA, 0xFF10B981));
        actividad.add(new ActividadItem("Juan Pérez",
                "Entregó: Parcial 2 — Bases de Datos 4B", "08:50",
                ActividadItem.TIPO_ENTREGA, 0xFFF59E0B));
        actividad.add(new ActividadItem("Laura Torres",
                "Mensaje: ¿Puede ampliar la fecha?", "08:10",
                ActividadItem.TIPO_MENSAJE, 0xFFEF4444));
        actividad.add(new ActividadItem("Cálculo Integral 2A",
                "5 entregas nuevas pendientes", "07:45",
                ActividadItem.TIPO_AVISO, 0xFF3B82F6));
        actividadReciente.setValue(actividad);
    }
}

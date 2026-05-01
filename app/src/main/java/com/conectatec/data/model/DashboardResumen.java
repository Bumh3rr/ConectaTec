package com.conectatec.data.model;

public class DashboardResumen {
    public final int gruposActivos;
    public final int alumnosTotales;
    public final int tareasEnCurso;

    public DashboardResumen(int gruposActivos, int alumnosTotales, int tareasEnCurso) {
        this.gruposActivos = gruposActivos;
        this.alumnosTotales = alumnosTotales;
        this.tareasEnCurso = tareasEnCurso;
    }
}

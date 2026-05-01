package com.conectatec.data.model;

public class Tarea {

    public static final String TIPO_TAREA    = "TAREA";
    public static final String TIPO_TRABAJO  = "TRABAJO";
    public static final String TIPO_EXAMEN   = "EXAMEN";
    public static final String TIPO_PROYECTO = "PROYECTO";

    public static final String EST_EN_CURSO   = "EN_CURSO";
    public static final String EST_VENCIDA    = "VENCIDA";
    public static final String EST_COMPLETADA = "COMPLETADA";

    public final int id;
    public final String titulo;
    public final String tipo;
    public final String estado;
    public final int grupoId;
    public final int bloqueId;
    public final String fechaPublicacion;
    public final String fechaVence;
    public final int totalAlumnos;
    public final int entregadas;

    public Tarea(int id, String titulo, String tipo, String estado,
                 int grupoId, int bloqueId, String fechaPublicacion,
                 String fechaVence, int totalAlumnos, int entregadas) {
        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.estado = estado;
        this.grupoId = grupoId;
        this.bloqueId = bloqueId;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaVence = fechaVence;
        this.totalAlumnos = totalAlumnos;
        this.entregadas = entregadas;
    }
}

package com.conectatec.data.model;

public class Grupo {
    public final int id;
    public final String nombre;
    public final String materia;
    public final int totalAlumnos;
    public final String fechaCreacion;
    public final String codigoUnion;
    public final boolean activo;

    public Grupo(int id, String nombre, String materia, int totalAlumnos,
                 String fechaCreacion, String codigoUnion, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.materia = materia;
        this.totalAlumnos = totalAlumnos;
        this.fechaCreacion = fechaCreacion;
        this.codigoUnion = codigoUnion;
        this.activo = activo;
    }
}

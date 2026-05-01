package com.conectatec.data.model;

public class AlumnoPerfil {
    public final int id;
    public final String nombre;
    public final String iniciales;
    public final String correo;
    public final String matricula;
    public final String semestre;
    public final String carrera;
    public final String fechaInscripcion;
    public final int totalActividades;
    public final int entregadas;
    public final int calificadas;
    public final int pendientes;

    public AlumnoPerfil(int id, String nombre, String iniciales, String correo,
                        String matricula, String semestre, String carrera,
                        String fechaInscripcion, int totalActividades,
                        int entregadas, int calificadas, int pendientes) {
        this.id = id;
        this.nombre = nombre;
        this.iniciales = iniciales;
        this.correo = correo;
        this.matricula = matricula;
        this.semestre = semestre;
        this.carrera = carrera;
        this.fechaInscripcion = fechaInscripcion;
        this.totalActividades = totalActividades;
        this.entregadas = entregadas;
        this.calificadas = calificadas;
        this.pendientes = pendientes;
    }
}
